package com.example.xyzreader.ui.main;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.ui.common.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder<ListItemArticleBinding>> {

    private List<Article> articles;
    private ArticleSelectionListener navigationController;

    private ArticleClickListener listener;

    @NonNull
    @Override
    public BindingViewHolder<ListItemArticleBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemArticleBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_article, parent,
                false);
        return new BindingViewHolder<>(binding);
    }

    private GlideRequestListener glideRequestListener;

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    @Inject
    public ArticleRecyclerViewAdapter(ArticleSelectionListener navigationController) {
        Timber.d("Creating ArticleRecyclerViewAdapter");
        this.navigationController = navigationController;
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ListItemArticleBinding> holder, int position) {
        Article article = articles.get(position);
        holder.binding().setArticle(article);
        holder.binding().setPosition(position);
        holder.binding().setGlideRequestListener(glideRequestListener);
        // call executePendingBindings to set the transitionNames on the background and the thumbnail
        holder.binding().executePendingBindings();
        List<View> sharedElementViews = new ArrayList<>();
        sharedElementViews.add(holder.binding().thumbnail);
        sharedElementViews.add(holder.binding().articleCv);
        holder.binding().articleCv.setOnClickListener(
                (View v) -> {
                    navigationController.onArticleClicked(position, sharedElementViews);
                    if (listener != null) listener.onClick(position);
                });
    }

    public void swapArticles(List<Article> newArticles) {
        Timber.d("Swapping articles");
        if (articles == null) {
            articles = newArticles;
            notifyItemRangeChanged(0, newArticles.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ArticleDiffCallback(newArticles, articles));
            articles = newArticles;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    public void setClickListener(ArticleClickListener listener) {
        this.listener = listener;
    }

    public void setGlideRequestListener(GlideRequestListener glideRequestListener) {
        this.glideRequestListener = glideRequestListener;
    }
}
