package com.example.project.api;

import com.example.project.model.KakaoBookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BookApiService {
    @GET("v3/search/book")
    Call<KakaoBookResponse> searchBooks(
            @Header("Authorization") String apiKey,
            @Query("query") String query
    );
}
