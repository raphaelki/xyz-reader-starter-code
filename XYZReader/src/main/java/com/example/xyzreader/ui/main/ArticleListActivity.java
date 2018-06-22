package com.example.xyzreader.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.detail.ArticleDetailActivity;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment articleListFragment = new ArticleListFragment();
            Transition enterTransition =
                    TransitionInflater.from(this).inflateTransition(R.transition.article_list_enter);
            enterTransition.addTarget(R.id.recycler_view);
            articleListFragment.setEnterTransition(enterTransition);
            getSupportFragmentManager()
                    .beginTransaction()
                    // reordering needs to be set for fragment enter transitions to work
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment_frame, articleListFragment)
                    .commit();
        }
    }
}
