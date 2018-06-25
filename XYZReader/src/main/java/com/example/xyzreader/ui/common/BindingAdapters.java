package com.example.xyzreader.ui.common;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;

public class BindingAdapters {
    @BindingAdapter(value = {"imageUrl", "requestListener"})
    public static void loadImageWithPlaceholder(ImageView view, String imageUrl, RequestListener<Drawable> listener) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .listener(listener)
                .into(view);
    }

    @BindingAdapter(value = {"imageUrl", "placeholder"})
    public static void loadImageWithPlaceholder(ImageView view, String imageUrl, Drawable placeholder) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .placeholder(placeholder)
                .into(view);
    }

    @BindingAdapter(value = {"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }
}
