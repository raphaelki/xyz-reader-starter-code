package com.example.xyzreader.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        return (T) creator.get();
    }
}
