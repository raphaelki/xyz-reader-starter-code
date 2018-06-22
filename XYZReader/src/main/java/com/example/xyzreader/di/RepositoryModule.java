package com.example.xyzreader.di;

import com.example.xyzreader.data.ArticleRepository;
import com.example.xyzreader.data.IArticleRepository;
import com.example.xyzreader.data.remote.ArticleFetcher;
import com.example.xyzreader.data.remote.ArticlesRemoteAPI;
import com.example.xyzreader.data.remote.IArticleFetcher;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class RepositoryModule {

    @Singleton
    @Provides
    static ArticlesRemoteAPI provideArticleRemoteApi(GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl("https://go.udacity.com/")
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(ArticlesRemoteAPI.class);
    }

    @Singleton
    @Provides
    static GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Binds
    abstract IArticleFetcher bindArticleFetcher(ArticleFetcher articleFetcher);

    @Singleton
    @Binds
    abstract IArticleRepository bindArticleRepository(ArticleRepository articleRepository);
}
