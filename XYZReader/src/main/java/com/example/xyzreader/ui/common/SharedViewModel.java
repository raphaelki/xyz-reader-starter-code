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
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class SharedViewModel extends ViewModel {

    private IArticleRepository articleRepository;
    private MutableLiveData<Integer> position = new MutableLiveData<>();
    private MutableLiveData<Integer> currentPagerItemFabColor = new MutableLiveData<>();

    @Inject
    public SharedViewModel(IArticleRepository articleRepository) {
        Timber.d("Creating SharedViewModel");
        this.articleRepository = articleRepository;
    }

    public void changePosition(int newPosition) {
        position.setValue(newPosition);
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

    public LiveData<Integer> getCurrentPosition() {
        return position;
    }

    public LiveData<Integer> getArticleCount() {
        return Transformations.map(getArticles(), List::size);
    }

    public void setCurrentPagerItemFabColor(int fabColor, int itemPagerPosition){
            currentPagerItemFabColor.setValue(fabColor);
    }

    public LiveData<Integer> getCurrentPagerItemFabColor(){
        return currentPagerItemFabColor;
    }
}
