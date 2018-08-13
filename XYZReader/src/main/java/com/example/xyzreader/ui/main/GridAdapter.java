package com.example.xyzreader.ui.main;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.view.ViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.Constants;
import com.example.xyzreader.R;
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.databinding.MainGridItemBinding;
import com.example.xyzreader.ui.common.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<BindingViewHolder<MainGridItemBinding>> {

    private List<Article> articles;
    private ArticleSelectionListener callback;
    private MainFragment mainFragment;
    private RequestListener<Drawable> glideRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            mainFragment.startPostponedEnterTransition();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            mainFragment.startPostponedEnterTransition();
            return false;
        }
    };

    public GridAdapter(ArticleSelectionListener callback, MainFragment mainFragment) {
        this.callback = callback;
        this.mainFragment = mainFragment;
    }

    @NonNull
    @Override
    public BindingViewHolder<MainGridItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainGridItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.main_grid_item, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<MainGridItemBinding> holder, int position) {
        holder.binding().setArticle(articles.get(position));
        holder.binding().setGlideRequestListener(glideRequestListener);
        ViewCompat.setTransitionName(holder.binding().gridItemPictureIv,
                Constants.IMAGE_TRANSITION_NAME_PREFIX + position);
        holder.binding().gridItemParentLayout.setOnClickListener(v -> {
            ((Transition)mainFragment.getExitTransition()).excludeTarget(v,true);
            ((Transition)mainFragment.getReenterTransition()).excludeTarget(v,true);
            List<View> sharedElements = new ArrayList<>();
            sharedElements.add(holder.binding().gridItemPictureIv);
            callback.onArticleClicked(position, sharedElements);
        });
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public void swapArticles(List<Article> newArticles){
        if (articles == null){
            articles = newArticles;
            notifyItemRangeChanged(0, newArticles.size());
        }
        else{
            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new ArticleDiffCallback(newArticles, articles));
            articles = newArticles;
            diffResult.dispatchUpdatesTo(this);
        }
    }
}
