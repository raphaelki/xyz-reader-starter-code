package com.example.xyzreader.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.xyzreader.data.local.ArticleDao;
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.data.remote.IArticleFetcher;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ArticleRepository implements IArticleRepository {

    final private IArticleFetcher articleFetcher;
    final private ArticleDao articleDao;
    private MutableLiveData<DataState> dataState = new MutableLiveData<>();
    private MediatorLiveData<List<Article>> observedArticles = new MediatorLiveData<>();

    @Inject
    public ArticleRepository(IArticleFetcher articleFetcher, ArticleDao articleDao) {
        Timber.d("Creating ArticleRepository");
        this.articleFetcher = articleFetcher;
        this.articleDao = articleDao;
    }

    @Override
    public LiveData<List<Article>> getArticles() {
        LiveData<List<Article>> dbArticles = getArticlesFromDao();
        observedArticles.addSource(dbArticles, articles -> {
            observedArticles.removeSource(dbArticles);
            if (articles == null || articles.size() == 0) {
                triggerFetch();
                observedArticles.addSource(dbArticles, observedArticles::setValue);
            } else {
                if (!articles.equals(observedArticles.getValue())) {
                    observedArticles.setValue(articles);
                }
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
