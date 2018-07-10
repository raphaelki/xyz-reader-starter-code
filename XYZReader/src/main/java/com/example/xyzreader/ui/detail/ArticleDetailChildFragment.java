package com.example.xyzreader.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.xyzreader.AppExecutors;
import com.example.xyzreader.Constants;
import com.example.xyzreader.R;
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.databinding.FragmentArticleDetailChildBinding;
import com.example.xyzreader.ui.common.GlideApp;
import com.example.xyzreader.ui.common.SharedViewModel;
import com.example.xyzreader.ui.main.GlideRequestListener;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ArticleDetailChildFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private final GlideRequestListener glideRequestListener = () -> {
        getParentFragment().startPostponedEnterTransition();
    };
    @Inject
    AppExecutors executors;

    private FragmentArticleDetailChildBinding binding;
    private Article article;

    public static ArticleDetailChildFragment create(int position) {
        ArticleDetailChildFragment fragment = new ArticleDetailChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ARTICLE_POSITION_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail_child, container, false);
        binding.setGlideRequestListener(glideRequestListener);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createViewModel();
    }

    private void createViewModel() {
        SharedViewModel viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        if (getArguments() != null && getArguments().containsKey(Constants.ARTICLE_POSITION_KEY)) {
            int position = getArguments().getInt(Constants.ARTICLE_POSITION_KEY);
            viewModel.getArticles().observe(this, articles -> {
                if (articles != null) {
                    Article article = articles.get(position);
                    bindViews(article);
                }
            });
        }
    }

    private void bindViews(Article newArticle) {
        if (article == null) {
            article = newArticle;
            binding.setArticle(article);
            binding.setGlideRequestListener(glideRequestListener);
            // call executePendingBindings to set the correct transitionName in binding
            binding.executePendingBindings();
            determineProminentPictureColors(newArticle);
//            loadImages(article);
        }
    }

    private void determineProminentPictureColors(Article article) {
        GlideApp.with(this)
                .load(article.getPhotoUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        getParentFragment().startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.photo);
        GlideApp.with(this)
                .asBitmap()
                .load(article.getPhotoUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@NonNull Palette palette) {
                                int defaultColor = getResources().getColor(R.color.cardview_dark_background);
                                int titleBackgroundColor = palette.getMutedColor(defaultColor);
                                binding.metaBar.setBackgroundColor(titleBackgroundColor);
                            }
                        });
                    }
                });

//        GlideApp.with(this)
//                .load(article.getPhotoUrl())
//                .listener()
//                .into(binding.photo);
//        BitmapDrawable fixBitmap = (BitmapDrawable)binding.photo.getDrawable();
//        if (fixBitmap == null) return;
//        Palette.from(fixBitmap.getBitmap()).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(@NonNull Palette palette) {
//                int defaultColor = getResources().getColor(R.color.cardview_dark_background);
//                int titleBackgroundColor = palette.getMutedColor(defaultColor);
//                binding.metaBar.setBackgroundColor(titleBackgroundColor);
//            }
//        });
//        getParentFragment().startPostponedEnterTransition();
    }

//    private void loadImages(Article article) {
//        executors.networkIO().execute(() -> {
//            Bitmap bitmap = null;
//            try {
//                bitmap = GlideApp.with(getContext())
//                        .asBitmap()
//                        .load(article.getPhotoUrl())
//                        .submit(100,100).get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//                Timber.e(e);
//            }
//            if (bitmap != null) {
//                Bitmap finalBitmap = bitmap;
//                executors.mainThread().execute(() -> determineProminentPictureColors(finalBitmap));
//            }
//        });
//    }
}
