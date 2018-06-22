package com.example.xyzreader.data.remote;

import com.example.xyzreader.data.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticlesRemoteAPI {

    @GET("xyz-reader-json")
    Call<List<Article>> fetchArticles();
}
