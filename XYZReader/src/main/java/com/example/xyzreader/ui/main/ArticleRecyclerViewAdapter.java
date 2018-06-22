package com.example.xyzreader.ui.main;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.ui.common.BindingViewHolder;

import java.util.List;

import javax.inject.Inject;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder<ListItemArticleBinding>> {

    private List<Article> articles;

    @Inject
    public ArticleRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public BindingViewHolder<ListItemArticleBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemArticleBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_article, parent,
                false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ListItemArticleBinding> holder, int position) {
        holder.binding().setArticle(articles.get(position));
        holder.binding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public void swapArticles(List<Article> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }
}
