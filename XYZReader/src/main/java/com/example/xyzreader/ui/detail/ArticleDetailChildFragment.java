package com.example.xyzreader.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.xyzreader.databinding.FragmentArticleDetailChildBinding;
import com.example.xyzreader.ui.common.GlideApp;
import com.example.xyzreader.ui.common.SharedViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ArticleDetailChildFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentArticleDetailChildBinding binding;

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
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(android.transition.TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
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
                    binding.setArticle(article);
                    // call executePedingBindings to set the correct transitionName in binding
                    binding.executePendingBindings();
                    GlideApp.with(this)
                            .load(article.getPhotoUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    startPostponedEnterTransition();
                                    return false;
                                }
                            })
                            .into(binding.photo);
                }
            });
        }
    }
}
