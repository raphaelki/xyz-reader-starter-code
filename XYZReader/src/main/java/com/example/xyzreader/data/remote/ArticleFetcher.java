package com.example.xyzreader.data.remote;

import android.arch.lifecycle.MutableLiveData;

import com.example.xyzreader.AppExecutors;
import com.example.xyzreader.data.DataState;
import com.example.xyzreader.data.local.ArticleDao;
import com.example.xyzreader.data.models.Article;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import timber.log.Timber;

public class ArticleFetcher implements IArticleFetcher {

    private ArticlesRemoteAPI articlesRemoteAPI;
    private AppExecutors appExecutors;
    private ArticleDao articleDao;

    @Inject
    public ArticleFetcher(ArticlesRemoteAPI articlesRemoteAPI, AppExecutors appExecutors, ArticleDao articleDao) {
        this.articlesRemoteAPI = articlesRemoteAPI;
        this.appExecutors = appExecutors;
        this.articleDao = articleDao;
    }

    @Override
    public void fetchArticles(final MutableLiveData<DataState> dataState) {
        Timber.d("Fetching new articles");
        dataState.setValue(DataState.REFRESHING);
        appExecutors.networkIO().execute(() -> {
            Response<List<Article>> response;
            try {
                response = articlesRemoteAPI.fetchArticles().execute();
                insertArticlesToDao(response.body(), dataState);
            } catch (IOException exception) {
                Timber.e(exception, "Error fetching articles form remote source");
                dataState.postValue(DataState.ERROR);
            }
        });
    }

    private void insertArticlesToDao(List<Article> articles, MutableLiveData<DataState> dataState) {
        appExecutors.diskIO().execute(() -> {
            articleDao.insertArticles(articles);
            dataState.postValue(DataState.SUCCESS);
        });
    }
}
