package com.example.xyzreader.ui.common;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class BindingAdapters {

    @BindingAdapter(value = {"imageUrl", "requestListener"})
    public static void loadImage(ImageView view, String imageUrl, RequestListener<Drawable> listener) {
        if (imageUrl != null) {
            Glide.with(view)
                    .load(imageUrl)
                    .listener(listener)
                    .into(view);
        }
    }
}
