package com.example.xyzreader.di;

import com.example.xyzreader.ui.main.ArticleListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = FragmentBindingModule.class)
    abstract ArticleListActivity articleListActivity();
}
