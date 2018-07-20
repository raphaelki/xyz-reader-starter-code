package com.example.xyzreader.ui.common;

import android.graphics.drawable.Drawable;

public interface GlideRequestListener {

    void onLoadSuccessful(Drawable drawable);

    void onLoadFailed();
}
