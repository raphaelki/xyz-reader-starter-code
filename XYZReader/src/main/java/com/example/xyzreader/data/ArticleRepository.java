package com.example.xyzreader.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.xyzreader.data.local.ArticleDao;
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.data.remote.IArticleFetcher;

import java.util.List;

import javax.inject.Inject;

public class ArticleRepository implements IArticleRepository {

    final private IArticleFetcher articleFetcher;
    final private ArticleDao articleDao;
    private MutableLiveData<DataState> dataState = new MutableLiveData<>();
    private MediatorLiveData<List<Article>> observedArticles = new MediatorLiveData<>();

    @Inject
    public ArticleRepository(IArticleFetcher articleFetcher, ArticleDao articleDao) {
        this.articleFetcher = articleFetcher;
        this.articleDao = articleDao;
    }

    @Override
    public LiveData<List<Article>> getArticles() {
        observedArticles.addSource(getArticlesFromDao(), articles -> {
            if (articles == null || articles.size() == 0) {
                triggerFetch();
                observedArticles.removeSource(getArticlesFromDao());
                observedArticles.addSource(getArticlesFromDao(), observedArticles::setValue);
            } else {
                observedArticles.setValue(articles);
            }
        });
        return observedArticles;
    }

    private LiveData<List<Article>> getArticlesFromDao() {
        return articleDao.getArticles();
    }

    @Override
    public LiveData<Article> getArticle(int articleId) {
        return articleDao.getArticle(articleId);
    }

    @Override
    public LiveData<DataState> getDataState() {
        return dataState;
    }

    @Override
    public void triggerFetch() {
        articleFetcher.fetchArticles(dataState);
    }
}
