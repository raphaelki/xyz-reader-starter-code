package com.example.xyzreader.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentArticleListBinding;
import com.example.xyzreader.ui.common.SharedViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ArticleListFragment extends DaggerFragment {

    @Inject
    ArticleRecyclerViewAdapter adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentArticleListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false);
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createViewModel();
    }

    private void createViewModel() {
        SharedViewModel viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        viewModel.getArticles().observe(this, adapter::swapArticles);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupEnterTransition(view);
    }

    /**
     * Setup a listener to start the postponed enter transition after the layout has been calculated
     *
     * @param view fragment view
     */
    private void setupEnterTransition(View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }
}
