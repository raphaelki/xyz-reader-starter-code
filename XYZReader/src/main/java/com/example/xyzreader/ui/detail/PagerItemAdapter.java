package com.example.xyzreader.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import javax.inject.Inject;

public class PagerItemAdapter extends FragmentStatePagerAdapter {

    private int pagerItemCount;

    @Inject
    public PagerItemAdapter(DetailPagerFragment parentFragment) {
        super(parentFragment.getChildFragmentManager());
    }

    @Override
    public Fragment getItem(int position) {
        return DetailPagerItemFragment.create(position);
    }

    @Override
    public int getCount() {
        return pagerItemCount;
    }

    public void setPagerItemCount(int newCount){
        pagerItemCount = newCount;
        notifyDataSetChanged();
    }
}
