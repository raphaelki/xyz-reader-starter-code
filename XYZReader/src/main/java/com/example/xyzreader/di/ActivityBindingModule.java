package com.example.xyzreader.di;

import com.example.xyzreader.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = FragmentBindingModule.class)
    abstract MainActivity articleListActivity();
}
