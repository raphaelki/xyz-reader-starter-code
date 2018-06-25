package com.example.xyzreader.ui.main;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
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
    private ArticleSelectionListener navigationController;

    @Inject
    public ArticleRecyclerViewAdapter(ArticleSelectionListener navigationController) {
        this.navigationController = navigationController;
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
        Article article = articles.get(position);
        holder.binding().setArticle(article);
        holder.binding().setCallback(navigationController);
        holder.binding().setPosition(position);
        holder.binding().articleCv.setOnClickListener(
                v -> navigationController.onArticleClicked(
                        position,
                        holder.binding().thumbnail,
                        ViewCompat.getTransitionName(holder.binding().thumbnail)));
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
