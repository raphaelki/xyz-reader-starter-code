package com.example.xyzreader.ui.detail;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.xyzreader.data.ArticleLoader;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Cursor cursor;

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        cursor.moveToPosition(position);
        return ArticleDetailFragment.newInstance(cursor.getLong(ArticleLoader.Query._ID));
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
