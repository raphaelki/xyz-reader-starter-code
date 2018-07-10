package com.example.xyzreader.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.common.NavigationController;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            navigationController.navigateToArticleList();
        }
    }
}
