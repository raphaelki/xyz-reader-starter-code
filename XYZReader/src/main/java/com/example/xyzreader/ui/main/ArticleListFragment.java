package com.example.xyzreader.ui.main;

import android.arch.lifecycle.Observer;
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
import com.example.xyzreader.data.models.Article;
import com.example.xyzreader.databinding.FragmentArticleListBinding;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.ui.common.BindingViewHolder;
import com.example.xyzreader.ui.common.SharedViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class ArticleListFragment extends DaggerFragment implements ArticleClickListener {

    @Inject
    ArticleRecyclerViewAdapter adapter;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentArticleListBinding binding;
    private final Observer<List<Article>> articleObserver = new Observer<List<Article>>() {
        @Override
        public void onChanged(@Nullable List<Article> articles) {
            if (articles != null) adapter.swapArticles(articles);
        }
    };
    private final GlideRequestListener glideRequestListener = this::startPostponedEnterTransition;
    private int currentItemPosition = -1;
    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Creating view for ArticleListFragment");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false);
        binding.recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.setGlideRequestListener(glideRequestListener);
        createViewModel();
        setupTransition();
        return binding.getRoot();
    }

    private void setupTransition() {
        Transition exitTransition = TransitionInflater.from(getContext())
                .inflateTransition(R.transition.article_list_exit_transition);
        exitTransition.setDuration(375);
        setExitTransition(exitTransition);
        postponeEnterTransition();
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                binding.recyclerView.scrollToPosition(currentItemPosition);
                BindingViewHolder viewHolder = (BindingViewHolder) binding.recyclerView.findViewHolderForAdapterPosition(currentItemPosition);
                if (viewHolder == null) {
                    Timber.w("ViewHolder is null. Cancelling remapping of shared elements");
                    return;
                }
                ListItemArticleBinding itemBinding = (ListItemArticleBinding) viewHolder.binding();
                sharedElements.put(names.get(0), itemBinding.thumbnail);
                sharedElements.put(names.get(1), itemBinding.articleCv);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("Creating ArticleListFragment");
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void createViewModel() {
        viewModel.getArticles().removeObserver(articleObserver);
        viewModel.getArticles().observe(this, articleObserver);
        viewModel.getDataState().observe(this, dataState -> {
            if (dataState != null) {
                onDataStateChanged(dataState);
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(viewModel::refreshData);
        viewModel.getCurrentPosition().observe(this, position -> {
            if (position != null) {
                currentItemPosition = position;
            }
        });
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
    public void onClick(int position) {
        viewModel.changePosition(position);
    }
}
