package com.example.xyzreader.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.xyzreader.R;
import com.example.xyzreader.data.DataState;
import com.example.xyzreader.databinding.FragmentArticleListBinding;
import com.example.xyzreader.ui.common.SharedViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ArticleListFragment extends DaggerFragment {

    @Inject
    ArticleRecyclerViewAdapter adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private FragmentArticleListBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false);
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
        viewModel.getDataState().observe(this, dataState -> {
            if (dataState != null) {
                onDataStateChanged(dataState);
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(viewModel::refreshData);
    }

    private void onDataStateChanged(DataState dataState) {
        switch (dataState) {
            case ERROR:
                Snackbar.make(getView(), "Error fetching new articles", Snackbar.LENGTH_LONG).show();
                binding.swipeRefreshLayout.setRefreshing(false);
                break;
            case SUCCESS:
                Snackbar.make(getView(), "Content refreshed", Snackbar.LENGTH_LONG).show();
                binding.swipeRefreshLayout.setRefreshing(false);
                break;
            case REFRESHING:
                binding.swipeRefreshLayout.setRefreshing(true);
                break;
        }
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
