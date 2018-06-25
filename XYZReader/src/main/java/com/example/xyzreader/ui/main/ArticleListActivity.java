package com.example.xyzreader.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.common.NavigationController;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends DaggerAppCompatActivity {

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
