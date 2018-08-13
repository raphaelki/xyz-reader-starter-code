package com.example.xyzreader.ui.detail;

import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.Constants;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentDetailPagerItemBinding;
import com.example.xyzreader.ui.common.SharedViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailPagerItemFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private FragmentDetailPagerItemBinding binding;
    private SharedViewModel viewModel;
    private Palette palette;
    private boolean isAppBarLogoVisible = false;

    private RequestListener<Drawable> glideRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            getParentFragment().startPostponedEnterTransition();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (palette == null) {
                generatePalette(resource);
            }
            getParentFragment().startPostponedEnterTransition();
            return false;
        }
    };

    private void generatePalette(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                Palette.from(bitmap).generate(palette -> {
                    this.palette = palette;
                    applyPalette();
                });
            }
        }
    }

    private void applyPalette() {
        if (palette != null) {
            Drawable backgroundDrawable = binding.detailItemTitle.getBackground();
            int backgroundColor = Color.WHITE;
            if (backgroundDrawable instanceof ColorDrawable) {
                backgroundColor = ((ColorDrawable) backgroundDrawable).getColor();
            }
            ValueAnimator backgroundColorAnimator = ValueAnimator.ofArgb(backgroundColor, palette.getDarkMutedColor(backgroundColor));
            backgroundColorAnimator.addUpdateListener(animation -> {
                int newBackgroundColor = (int) animation.getAnimatedValue();
                binding.detailItemTitle.setBackgroundColor(newBackgroundColor);
                binding.detailItemSubtitle.setBackgroundColor(newBackgroundColor);
            });
            backgroundColorAnimator.setDuration(1000L);
            backgroundColorAnimator.setInterpolator(new FastOutSlowInInterpolator());
            backgroundColorAnimator.start();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_pager_item, container, false);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        ViewCompat.setTransitionName(binding.pagerItemArticleIv, Constants.IMAGE_TRANSITION_NAME_PREFIX + getArticlePosition());
        binding.setGlideRequestListener(glideRequestListener);
        binding.pagerItemBody.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));
        binding.pagerItemAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                fadeInAppLogo();
            } else {
                fadeOutAppLogo();
            }
        });
        return binding.getRoot();
    }

    private void fadeInAppLogo() {
        if (!isAppBarLogoVisible){
            animateAppLogo(0.0f, 1.0f);
            isAppBarLogoVisible = true;
        }
    }

    private void fadeOutAppLogo() {
        if (isAppBarLogoVisible){
            animateAppLogo(1.0f, 0.0f);
            isAppBarLogoVisible = false;
        }
    }

    private void animateAppLogo(float alphaStart, float alphaEnd) {
        ValueAnimator logoAlphaAnimator = ValueAnimator.ofFloat(alphaStart, alphaEnd);
        logoAlphaAnimator.setDuration(300L);
        logoAlphaAnimator.addUpdateListener(animation -> {
            binding.pagerItemLogoIv.setAlpha((float) animation.getAnimatedValue());
        });
        logoAlphaAnimator.start();
    }

    private int getArticlePosition() {
        if (getArguments() != null && getArguments().containsKey(Constants.ARTICLE_ID_KEY)) {
            return getArguments().getInt(Constants.ARTICLE_ID_KEY);
        }
        return 0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel.getArticles().observe(this, articles -> {
            binding.setArticle(articles.get(getArticlePosition()));
            setupBackgroundImage(articles.get(getArticlePosition()).getPhotoUrl());
        });
        viewModel.getCurrentPosition().observe(this, position -> {
            if (position == getArticlePosition() && palette != null) {
                viewModel.setCurrentPagerItemFabColor(palette.getVibrantColor(Color.BLACK), getArticlePosition());
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    public static DetailPagerItemFragment create(int articleId) {
        DetailPagerItemFragment detailPagerItemFragment = new DetailPagerItemFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.ARTICLE_ID_KEY, articleId);
        detailPagerItemFragment.setArguments(arguments);
        return detailPagerItemFragment;
    }

    private void setupBackgroundImage(String photoUrl) {
        Glide.with(this).load(photoUrl)
                .apply(bitmapTransform(new BlurTransformation(25, 3)))
                .into(binding.pagerItemBlurredImage);
    }
}
