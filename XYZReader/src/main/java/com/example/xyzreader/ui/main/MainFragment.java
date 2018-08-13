package com.example.xyzreader.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.SharedElementCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.DataState;
import com.example.xyzreader.databinding.FragmentMainBinding;
import com.example.xyzreader.databinding.MainGridItemBinding;
import com.example.xyzreader.ui.common.BindingViewHolder;
import com.example.xyzreader.ui.common.NavigationController;
import com.example.xyzreader.ui.common.SharedViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class MainFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    private SharedViewModel viewModel;
    private FragmentMainBinding binding;
    private GridAdapter adapter;
    private int currentItemPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
    }

    private void setupTransitions() {
        TransitionInflater transitionInflater = TransitionInflater.from(getContext());
        Transition exitTransition = transitionInflater.inflateTransition(R.transition.main_exit);
        setExitTransition(exitTransition);
        Transition reenterTransition = transitionInflater.inflateTransition(R.transition.main_reenter);
        setReenterTransition(reenterTransition);

        // remap shared elements to go back to corresponding item of current view pager position
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                binding.mainGridRv.scrollToPosition(currentItemPosition);
                BindingViewHolder viewHolder = (BindingViewHolder) binding.mainGridRv.findViewHolderForAdapterPosition(currentItemPosition);
                if (viewHolder == null) {
                    Timber.w("ViewHolder is null. Cancelling remapping of shared elements");
                    return;
                }
                MainGridItemBinding itemBinding = (MainGridItemBinding) viewHolder.binding();
                sharedElements.put(names.get(0), itemBinding.gridItemPictureIv);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postponeEnterTransition();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        adapter = new GridAdapter(navigationController, this);
        binding.mainGridRv.setAdapter(adapter);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel.getArticles().observe(this, articles -> {
            adapter.swapArticles(articles);
        });
        viewModel.getCurrentPosition().observe(this, position ->{
            if (position != null){
                currentItemPosition = position;
            }
        });
        viewModel.getDataState().observe(this, dataState -> {
            if (dataState != null) {
                onDataStateChanged(dataState);
            }
        });
        binding.mainSwipeRefreshLayout.setOnRefreshListener(viewModel::refreshData);
        super.onActivityCreated(savedInstanceState);
    }

    private void onDataStateChanged(DataState dataState) {
        switch (dataState) {
            case ERROR:
                Snackbar.make(getView(), "Error fetching new articles", Snackbar.LENGTH_LONG).show();
                binding.mainSwipeRefreshLayout.setRefreshing(false);
                break;
            case SUCCESS:
                Snackbar.make(getView(), "Content refreshed", Snackbar.LENGTH_LONG).show();
                binding.mainSwipeRefreshLayout.setRefreshing(false);
                break;
            case REFRESHING:
                binding.mainSwipeRefreshLayout.setRefreshing(true);
                break;
        }
    }
}
