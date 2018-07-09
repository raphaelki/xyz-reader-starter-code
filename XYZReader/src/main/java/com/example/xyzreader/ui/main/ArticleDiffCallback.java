package com.example.xyzreader.ui.main;

import android.support.v7.util.DiffUtil;

import com.example.xyzreader.data.models.Article;

import java.util.List;

public class ArticleDiffCallback extends DiffUtil.Callback {

    private List<Article> newArticles;
    private List<Article> oldArticles;

    public ArticleDiffCallback(List<Article> newArticles, List<Article> oldArticles) {
        this.newArticles = newArticles;
        this.oldArticles = oldArticles;
    }

    @Override
    public int getOldListSize() {
        return oldArticles.size();
    }

    @Override
    public int getNewListSize() {
        return newArticles.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldArticles.get(oldItemPosition).getTitle().equals(newArticles.get(newItemPosition).getTitle());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldArticles.get(oldItemPosition).equals(newArticles.get(newItemPosition));
    }
}
