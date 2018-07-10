package com.example.xyzreader.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import javax.inject.Inject;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private int articleCount;

    @Inject
    public FragmentPagerAdapter(ArticleDetailParentFragment fragment) {
        super(fragment.getChildFragmentManager());
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailChildFragment.create(position);
    }

    @Override
    public int getCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
        notifyDataSetChanged();
    }
}
