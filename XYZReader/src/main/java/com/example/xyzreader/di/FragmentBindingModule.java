package com.example.xyzreader.di;

import com.example.xyzreader.ui.common.NavigationController;
import com.example.xyzreader.ui.detail.DetailPagerFragment;
import com.example.xyzreader.ui.detail.DetailPagerItemFragment;
import com.example.xyzreader.ui.main.ArticleSelectionListener;
import com.example.xyzreader.ui.main.MainFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract MainFragment mainFragment();

    @ContributesAndroidInjector
    abstract DetailPagerFragment detailPagerFragment();

    @ContributesAndroidInjector
    abstract DetailPagerItemFragment detailPagerItemFragment();

    @Binds
    abstract ArticleSelectionListener bindArticleSelectionListener(NavigationController navigationController);
}
