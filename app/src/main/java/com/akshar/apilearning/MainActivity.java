package com.akshar.apilearning;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.InputStream;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView courseRV;
    private ArrayList<RecycleData> recyclerDataArrayList;
    private RecycleViewAdapter recyclerViewAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    RetrofitAPI retrofitAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseRV = findViewById(R.id.idRVCourse);
        progressBar = findViewById(R.id.idPBLoading);
        recyclerDataArrayList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.refreshLayout);

        recyclerViewAdapter = new RecycleViewAdapter(recyclerDataArrayList, this);

        retrofitAPI = RetroFitInstance.getRetrofit().create(RetrofitAPI.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        recyclerViewAdapter.setOnItemClickListener((position, courseData) -> showImageInDialog(position));
        refreshData();
    }


    private void refreshData() {
        retrofitAPI.getPosts(100).enqueue(new Callback<List<RecycleData>>() {
            @Override
            public void onResponse(Call<List<RecycleData>> call, Response<List<RecycleData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerDataArrayList.clear();
                    recyclerDataArrayList.addAll(response.body());
                    setRecyclerViewLayoutManager();
                    courseRV.setAdapter(recyclerViewAdapter);
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<RecycleData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRecyclerViewLayoutManager() {
        // Use StaggeredGridLayoutManager for variable item sizes
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // Set the custom ItemDecoration to add spacing between items
        courseRV.addItemDecoration(new DynamicSpacingItemDecoration());

        // Set the StaggeredGridLayoutManager to the RecyclerView
        courseRV.setLayoutManager(staggeredGridLayoutManager);
    }

    private class DynamicSpacingItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            RecycleData item = recyclerDataArrayList.get(position);

            // Calculate aspect ratio (width / height)
            float aspectRatio = item.getWidth() / (float) item.getHeight();

            // Adjust spacing dynamically based on aspect ratio
            int spacing = (int) (16 * aspectRatio); // Adjust the multiplier as needed

            // Apply spacing to the top and bottom of each item
            outRect.top = spacing;
            outRect.bottom = spacing;
        }
    }
    private void showImageInDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_image_preview, null);
        builder.setView(dialogView);

        ImageView imageView = dialogView.findViewById(R.id.dialogImageView);
        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        ImageButton downloadButton = dialogView.findViewById(R.id.downloadButton);
        AlertDialog dialog = builder.create();

        downloadButton.setOnClickListener(view -> {
            RecycleData item = recyclerDataArrayList.get(position);
            String url = item.getUrl();
            new DownloadImageTask(bitmap -> Toast.makeText(MainActivity.this, "Image saved to Downloads", Toast.LENGTH_SHORT).show()).execute(url);
        });

        closeButton.setOnClickListener(view -> {
            builder.setCancelable(true);
            dialog.dismiss();
        });
        RecycleData item = recyclerDataArrayList.get(position);
        Picasso.get().load(item.getUrl()).into(imageView);

        dialog.show();
    }
}
