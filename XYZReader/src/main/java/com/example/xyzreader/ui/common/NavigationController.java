package com.example.xyzreader.ui.common;

import android.content.Context;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.detail.ArticleDetailParentFragment;
import com.example.xyzreader.ui.main.ArticleListActivity;
import com.example.xyzreader.ui.main.ArticleListFragment;
import com.example.xyzreader.ui.main.ArticleSelectionListener;

import javax.inject.Inject;

public class NavigationController implements ArticleSelectionListener {

    private final int FRAGMENT_FRAME_ID = R.id.main_fragment_frame;
    private Context context;
    private FragmentManager fragmentManager;

    @Inject
    public NavigationController(ArticleListActivity activity, Context context) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.context = context;
    }

    public void navigateToDetailsFragment(int position, View view, String transitionName) {
        ArticleDetailParentFragment articleDetailParentFragment = ArticleDetailParentFragment.create(position);
//        Transition enterTransition =
//                TransitionInflater.from(context).inflateTransition(R.transition.article_list_enter);
//        enterTransition.addTarget(R.id.details_frame);
//        articleDetailParentFragment.setEnterTransition(enterTransition);
        fragmentManager.beginTransaction()
                .addSharedElement(view, transitionName)
                .addToBackStack(null)
                .replace(FRAGMENT_FRAME_ID, articleDetailParentFragment)
                .commit();
    }

    public void navigateToArticleList() {
        Fragment articleListFragment = new ArticleListFragment();
        Transition enterTransition =
                TransitionInflater.from(context).inflateTransition(R.transition.article_list_enter);
        enterTransition.addTarget(R.id.app_logo_iv);
        articleListFragment.setEnterTransition(enterTransition);
        fragmentManager.beginTransaction()
                // reordering needs to be set for fragment enter transitions to work
//                .setReorderingAllowed(true)
                .replace(R.id.main_fragment_frame, articleListFragment)
                .commit();
    }

    @Override
    public void onArticleClicked(int position, View sharedView, String transitionName) {
        navigateToDetailsFragment(position, sharedView, transitionName);
    }
}
