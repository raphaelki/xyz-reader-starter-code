package com.example.xyzreader.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.xyzreader.data.DataState;
import com.example.xyzreader.data.IArticleRepository;
import com.example.xyzreader.data.models.Article;

import java.util.List;

import javax.inject.Inject;

public class SharedViewModel extends ViewModel {

    private IArticleRepository articleRepository;
    private MutableLiveData<Integer> articleId = new MutableLiveData<>();

    @Inject
    public SharedViewModel(IArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void changeArticleId(int id) {
        articleId.setValue(id);
    }

    public LiveData<DataState> getDataState() {
        return articleRepository.getDataState();
    }

    public LiveData<List<Article>> getArticles() {
        return articleRepository.getArticles();
    }

    public void refreshData() {
        articleRepository.triggerFetch();
    }

    public LiveData<Article> getArticle() {
        return Transformations.switchMap(articleId, id -> articleRepository.getArticle(id));
    }
}
