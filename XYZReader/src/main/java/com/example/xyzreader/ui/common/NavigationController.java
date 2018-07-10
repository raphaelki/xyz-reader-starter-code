package com.example.xyzreader.ui.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.MainActivity;
import com.example.xyzreader.ui.detail.ArticleDetailParentFragment;
import com.example.xyzreader.ui.main.ArticleListFragment;
import com.example.xyzreader.ui.main.ArticleSelectionListener;

import java.util.List;

import javax.inject.Inject;

public class NavigationController implements ArticleSelectionListener {

    private final int FRAGMENT_FRAME_ID = R.id.main_fragment_frame;
    private Context context;
    private FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity activity, Context context) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.context = context;
    }

    public void navigateToDetailsFragment(int position, List<View> sharedElementViews) {
        ArticleDetailParentFragment articleDetailParentFragment = ArticleDetailParentFragment.create(position);
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true);
        for (View sharedElementView : sharedElementViews) {
            fragmentTransaction.addSharedElement(sharedElementView, ViewCompat.getTransitionName(sharedElementView));
        }
        fragmentTransaction
                .replace(FRAGMENT_FRAME_ID, articleDetailParentFragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToArticleList() {
        Fragment articleListFragment = new ArticleListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.main_fragment_frame, articleListFragment)
                .commit();
    }

    @Override
    public void onArticleClicked(int position, List<View> sharedElementViews) {
        navigateToDetailsFragment(position, sharedElementViews);
    }
}
