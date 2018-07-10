package com.example.xyzreader.ui.main;

import android.support.v4.app.SharedElementCallback;
import android.view.View;

import com.example.xyzreader.databinding.ListItemArticleBinding;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ListSharedElementCallback extends SharedElementCallback {

    private ListItemArticleBinding binding;

    public ListSharedElementCallback(ListItemArticleBinding binding) {
        this.binding = binding;
    }

    public ListSharedElementCallback() {
    }

    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//        int articleId = binding.getArticle().getId();
        Timber.d("onMapSharedElements");
    }

}
