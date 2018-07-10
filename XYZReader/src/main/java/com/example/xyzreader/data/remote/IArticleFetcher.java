package com.example.xyzreader.data.remote;

import android.arch.lifecycle.MutableLiveData;

import com.example.xyzreader.data.DataState;

public interface IArticleFetcher {

    void fetchArticles(MutableLiveData<DataState> dataState);
}
