package com.example.xyzreader.ui.common;

import android.content.Context;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.MainActivity;
import com.example.xyzreader.ui.detail.DetailPagerFragment;
import com.example.xyzreader.ui.main.ArticleSelectionListener;
import com.example.xyzreader.ui.main.MainFragment;

import java.util.List;

import javax.inject.Inject;

public class NavigationController implements ArticleSelectionListener {

    private final int FRAGMENT_FRAME_ID = R.id.main_fragment_frame;
    private final String MAIN_FRAGMENT_TAG = MainFragment.class.getSimpleName();
    private Context context;
    private FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity activity, Context context) {
        this.fragmentManager = activity.getSupportFragmentManager();
        this.context = context;
    }

    private void navigateToDetailsPager(int position, List<View> sharedElementViews){
        DetailPagerFragment detailPagerFragment = DetailPagerFragment.create(position);
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true);
        for (View sharedElementView : sharedElementViews){
            fragmentTransaction.addSharedElement(sharedElementView,
                    ViewCompat.getTransitionName(sharedElementView));
        }
        fragmentTransaction
                .replace(FRAGMENT_FRAME_ID, detailPagerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onArticleClicked(int position, List<View> sharedElementViews) {
        navigateToDetailsPager(position, sharedElementViews);
    }

    public void navigateToArticleList() {
        fragmentManager.beginTransaction()
                .add(FRAGMENT_FRAME_ID, new MainFragment(), MAIN_FRAGMENT_TAG)
                .commit();
    }
}
