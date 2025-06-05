package com.example.project.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CultureEventService {
    @GET("B553457/nopenapi/rest/publicperformancedisplays/livelihood")
    Call<ResponseBody> searchCultureEvents(
            @Query(value = "serviceKey", encoded = true) String serviceKey,
            @Query("keyword") String keyword,
            @Query("rows") int rows,
            @Query("cpage") int page
    );

    @GET("B553457/nopenapi/rest/publicperformancedisplays/detail")
    Call<ResponseBody> getEventDetail(
            @Query(value = "serviceKey", encoded = true) String serviceKey,
            @Query("seq") String seq
    );
}

