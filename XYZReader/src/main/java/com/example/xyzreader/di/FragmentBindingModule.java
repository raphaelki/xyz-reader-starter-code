package com.example.xyzreader.di;

import com.example.xyzreader.ui.main.ArticleListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract ArticleListFragment articleListFragment();
}
