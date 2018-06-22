package com.example.xyzreader.ui.common;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.xyzreader.data.IArticleRepository;
import com.example.xyzreader.data.models.Article;

import java.util.List;

import javax.inject.Inject;

public class SharedViewModel extends ViewModel {

    private IArticleRepository articleRepository;

    @Inject
    public SharedViewModel(IArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public LiveData<List<Article>> getArticles() {
        return articleRepository.getArticles();
    }
}
