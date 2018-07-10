package com.example.xyzreader.ui.main;

import android.view.View;

import java.util.List;

public interface ArticleSelectionListener {

    void onArticleClicked(int position, List<View> sharedElementViews);
}
