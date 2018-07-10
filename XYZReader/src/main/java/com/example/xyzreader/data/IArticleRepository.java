package com.example.xyzreader.data;

import android.arch.lifecycle.LiveData;

import com.example.xyzreader.data.models.Article;

import java.util.List;

public interface IArticleRepository {

    LiveData<List<Article>> getArticles();

    LiveData<Article> getArticle(int articleId);

    LiveData<DataState> getDataState();

    void triggerFetch();
}
