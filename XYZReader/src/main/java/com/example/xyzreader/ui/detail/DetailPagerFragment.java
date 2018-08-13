package com.example.xyzreader.ui.detail;

import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.Constants;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentDetailPagerBinding;
import com.example.xyzreader.databinding.FragmentDetailPagerItemBinding;
import com.example.xyzreader.ui.common.SharedViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DetailPagerFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    PagerItemAdapter pagerItemAdapter;

    private FragmentDetailPagerBinding binding;
    private SharedViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
    }

    private void setupTransitions() {
        TransitionInflater transitionInflater = TransitionInflater.from(getContext());
        Transition enterTransition = transitionInflater.inflateTransition(R.transition.pager_item_enter);
        setEnterTransition(enterTransition);
        Transition returnTransition = transitionInflater.inflateTransition(R.transition.pager_item_return);
        setReturnTransition(returnTransition);
        Transition sharedElementEnterTransition = transitionInflater.inflateTransition(R.transition.detail_shared_element_enter);
        setSharedElementEnterTransition(sharedElementEnterTransition);
        Transition sharedElementReturnTransition = transitionInflater.inflateTransition(R.transition.detail_shared_element_return);
        setSharedElementReturnTransition(sharedElementReturnTransition);

        // remap shared elements to go back to corresponding item of current view pager position
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Fragment fragment = (Fragment) binding.detailViewPager.getAdapter().instantiateItem(binding.detailViewPager, binding.detailViewPager.getCurrentItem());
                View view = fragment.getView();
                if (view == null) return;
                FragmentDetailPagerItemBinding childBinding = DataBindingUtil.getBinding(view);
                sharedElements.put(names.get(0), childBinding.pagerItemArticleIv);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postponeEnterTransition();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_pager, container, false);
        binding.detailViewPager.setAdapter(pagerItemAdapter);
        binding.detailPagerBackButton.setOnClickListener((v) -> getActivity().onBackPressed());
        binding.detailPagerFab.setOnClickListener(v -> startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText("Some sample text")
                .getIntent(), getString(R.string.action_share))));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedViewModel.class);
        viewModel.getArticleCount().observe(this, articleCount -> {
            if (articleCount != null) {
                pagerItemAdapter.setPagerItemCount(articleCount);
                viewModel.getCurrentPosition().observe(this, position -> {
                    binding.detailViewPager.setCurrentItem(position, false);
                });
            }
        });
        if (savedInstanceState == null){
            viewModel.changePosition(getArticlePositionFromArguments());
        }
        binding.detailViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewModel.changePosition(position);
            }
        });
        viewModel.getCurrentPagerItemFabColor().observe(this, fabColor ->{
            if (fabColor != null){
                animateFabColorChange(fabColor);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void animateFabColorChange(int color){
        int currentColor = binding.detailPagerFab.getRippleColor();
        ValueAnimator fabColorAnimator = ValueAnimator.ofArgb(currentColor, color);
        fabColorAnimator.setDuration(1000L);
        fabColorAnimator.addUpdateListener(animation -> {
            binding.detailPagerFab.setBackgroundTintList(ColorStateList.valueOf((int)animation.getAnimatedValue()));
        });
        fabColorAnimator.setInterpolator(new FastOutSlowInInterpolator());
        fabColorAnimator.start();
    }

    private int getArticlePositionFromArguments() {
        if (getArguments() != null && getArguments().containsKey(Constants.ARTICLE_ID_KEY)) {
            return getArguments().getInt(Constants.ARTICLE_ID_KEY);
        }
        return 0;
    }

    public static DetailPagerFragment create(int articleId) {
        DetailPagerFragment detailPagerFragment = new DetailPagerFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.ARTICLE_ID_KEY, articleId);
        detailPagerFragment.setArguments(arguments);
        return detailPagerFragment;
    }
}
