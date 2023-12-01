package com.akshar.apilearning;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("images/search")
    Call<List<RecycleData>> getPosts(@Query("limit") int limit);
}
