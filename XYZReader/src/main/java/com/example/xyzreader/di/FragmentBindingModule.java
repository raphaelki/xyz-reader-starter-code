package com.example.xyzreader.di;

import com.example.xyzreader.ui.common.NavigationController;
import com.example.xyzreader.ui.detail.ArticleDetailChildFragment;
import com.example.xyzreader.ui.detail.ArticleDetailParentFragment;
import com.example.xyzreader.ui.main.ArticleListFragment;
import com.example.xyzreader.ui.main.ArticleSelectionListener;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract ArticleListFragment articleListFragment();

    @ContributesAndroidInjector
    abstract ArticleDetailChildFragment articleDetailChildFragment();

    @ContributesAndroidInjector
    abstract ArticleDetailParentFragment articleDetailParentFragment();

    @Binds
    abstract ArticleSelectionListener bindArticleSelectionListener(NavigationController navigationController);
}
