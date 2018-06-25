package com.example.xyzreader.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.Constants;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentArticleDetailParentBinding;
import com.example.xyzreader.ui.common.SharedViewModel;
import com.example.xyzreader.ui.main.ArticleListActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import timber.log.Timber;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailParentFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    FragmentPagerAdapter adapter;

    private FragmentArticleDetailParentBinding binding;

    public static ArticleDetailParentFragment create(int position) {
        ArticleDetailParentFragment articleDetailParentFragment = new ArticleDetailParentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ARTICLE_ID_KEY, position);
        articleDetailParentFragment.setArguments(bundle);
        return articleDetailParentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        setSharedElementReturnTransition(null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail_parent, container, false);
        binding.pager.setAdapter(adapter);
        return binding.getRoot();
    }

    private void createViewModel() {
        SharedViewModel viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(Constants.ARTICLE_ID_KEY)) {
            int position = arguments.getInt(Constants.ARTICLE_ID_KEY);
            viewModel.changePosition(position);
        }
        binding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewModel.changePosition(position);
            }
        });
        viewModel.getArticles().observe(this, articles -> {
            adapter.swapArticles(articles);
            viewModel.getCurrentPosition().observe(this, binding.pager::setCurrentItem);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Timber.d("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        createViewModel();
    }
}
