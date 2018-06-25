package com.example.xyzreader.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.xyzreader.data.models.Article;

import java.util.List;

import javax.inject.Inject;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Article> articles;

    @Inject
    public FragmentPagerAdapter(ArticleDetailParentFragment fragment) {
        super(fragment.getChildFragmentManager());
    }

    @Override
    public Fragment getItem(int position) {
        String transitionName = articles.get(position).getTitle();
        return ArticleDetailChildFragment.create(position, transitionName);
    }

    @Override
    public int getCount() {
        return articles == null ? 0 : articles.size();
    }

    public void swapArticles(List<Article> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }
}
