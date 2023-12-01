package com.akshar.apilearning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecyclerViewHolder> {
    private ArrayList<RecycleData> courseDataArrayList;
    private Context mcontext;
    private OnClickListener onClickListener;

    public RecycleViewAdapter(ArrayList<RecycleData> courseDataArrayList, Context mcontext) {
        this.courseDataArrayList = courseDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

@Override
public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
    RecycleData modal = courseDataArrayList.get(position);
    holder.courseNameTV.setText(modal.getId());
    holder.heightTV.setText(String.valueOf(modal.getHeight()));
    holder.widthTV.setText(String.valueOf(modal.getWidth()));
    Picasso.get()
            .load(modal.getUrl())
            .into(holder.courseIV);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onClick(position, modal);
            }
        }
    });
}


    @Override
    public int getItemCount() {
        return courseDataArrayList.size();
    }

    public void setOnItemClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(int position , RecycleData data);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseNameTV ,heightTV,widthTV;
        private final ImageView courseIV;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);
            heightTV = itemView.findViewById(R.id.heightText);
            widthTV = itemView.findViewById(R.id.widthText);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
