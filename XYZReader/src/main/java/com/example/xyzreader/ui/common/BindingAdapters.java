package com.example.xyzreader.ui.common;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.ui.main.GlideRequestListener;

public class BindingAdapters {
    @BindingAdapter(value = {"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    @BindingAdapter(value = {"imageUrl", "requestListener"})
    public static void loadImage(ImageView view, String imageUrl, GlideRequestListener listener) {
        if (imageUrl != null) {
            GlideApp.with(view.getContext())
                    .load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            listener.onLoadingFinished();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            listener.onLoadingFinished();
                            return false;
                        }
                    })
                    .into(view);
        }
    }

    @BindingAdapter(value = {"imageUrl", "requestListener", "paletteListener"})
    public static void loadImage(ImageView view, String imageUrl, GlideRequestListener listener, RequestListener<Drawable> paletteListener) {
        if (imageUrl != null) {
            GlideApp.with(view.getContext())
                    .load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            listener.onLoadingFinished();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            listener.onLoadingFinished();
                            return false;
                        }
                    })
                    .listener(paletteListener)
                    .into(view);
        }
    }
}
