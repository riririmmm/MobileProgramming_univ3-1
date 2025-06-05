package com.example.project.api;

import com.example.project.model.KmdbMovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("openapi-data2/wisenut/search_api/search_json2.jsp")
    Call<KmdbMovieResponse> searchMovies(
            @Query("collection") String collection,
            @Query("detail") String detail,
            @Query("ServiceKey") String apiKey,
            @Query("query") String query
    );
}