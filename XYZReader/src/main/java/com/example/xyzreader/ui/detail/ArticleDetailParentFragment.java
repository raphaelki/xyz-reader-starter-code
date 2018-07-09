package com.example.xyzreader.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.Constants;
import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentArticleDetailChildBinding;
import com.example.xyzreader.databinding.FragmentArticleDetailParentBinding;
import com.example.xyzreader.ui.MainActivity;
import com.example.xyzreader.ui.common.SharedViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import timber.log.Timber;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link MainActivity} in two-pane mode (on
 * tablets) or a  on handsets.
 */
public class ArticleDetailParentFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    FragmentPagerAdapter adapter;

    private int currentPagerPosition;
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
        Timber.d("Creating ArticleDetailParentFragment");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setupTransition() {
        Transition transition =
                android.transition.TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        setSharedElementEnterTransition(transition);
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Fragment fragment = (Fragment) binding.pager.getAdapter().instantiateItem(binding.pager, binding.pager.getCurrentItem());
                View view = fragment.getView();
                if (view == null) return;
                FragmentArticleDetailChildBinding childBinding = DataBindingUtil.getBinding(view);
                sharedElements.put(names.get(0), childBinding.photo);
                sharedElements.put(names.get(1), childBinding.drawInsetsFrameLayout);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            postponeEnterTransition();
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail_parent, container, false);
        binding.pager.setAdapter(adapter);
        createViewModel();
        setupTransition();
        setupToolbar();
        binding.shareFab.setOnClickListener(v -> startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText("Some sample text")
                .getIntent(), getString(R.string.action_share))));

        return binding.getRoot();
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return false;
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
        viewModel.getArticleCount().observe(this, articleCount -> {
            if (articleCount != null) {
                adapter.setArticleCount(articleCount);
                viewModel.getCurrentPosition().observe(this, currentPosition -> binding.pager.setCurrentItem(currentPosition, false));
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
