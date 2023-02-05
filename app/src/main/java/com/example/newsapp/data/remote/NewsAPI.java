package com.example.newsapp.data.remote;

import com.example.newsapp.models.RootJsonData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {

    @GET("top-headlines")
    Call<RootJsonData> getTopHeadlinesByCountry(@Query("country") String country,
                                                @Query("language") String language,
                                                @Query("apiKey") String apiKey,
                                                @Query("page") int pageNumber,
                                                @Query("pageSize") int pageSize);

    @GET("top-headlines")
    Call<RootJsonData> getTopHeadlinesByLanguage(@Query("language") String language,
                                                 @Query("apiKey") String apiKey,
                                                 @Query("page") int pageNumber,
                                                 @Query("pageSize") int pageSize);

    @GET("top-headlines")
    Call<RootJsonData> getTopHeadlinesByCategory(@Query("category") String category,
                                                 @Query("language") String language,
                                                 @Query("apiKey") String apiKey,
                                                 @Query("page") int pageNumber,
                                                 @Query("pageSize") int pageSize);

    @GET("top-headlines")
    Call<RootJsonData> searchNewsByKeyWord(@Query("q") String keyword,
                                           @Query("sortBy") String sortBy,
                                           @Query("language") String language,
                                           @Query("apiKey") String apiKey,
                                           @Query("page") int pageNumber,
                                           @Query("pageSize") int pageSize);

    @GET("everything")
    Call<RootJsonData> searchArticlesByKeyWord(@Query("q") String keyword,
                                               @Query("sortBy") String sortBy,
                                               @Query("language") String language,
                                               @Query("apiKey") String apiKey,
                                               @Query("page") int pageNumber,
                                               @Query("pageSize") int pageSize);
}
