package com.example.xyzreader.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.xyzreader.XYZReader;
import com.example.xyzreader.data.local.ArticleDao;
import com.example.xyzreader.data.local.ArticleDatabase;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
public abstract class ApplicationModule {

    public static final String DB_NAME = "articles.db";

    @Singleton
    @Provides
    static ArticleDao provideDatabase(Context context) {
        return Room.databaseBuilder(context, ArticleDatabase.class, DB_NAME)
                // no migration strategy implemented
                .fallbackToDestructiveMigration()
                .build().articleDao();
    }

    @Singleton
    @Binds
    abstract Application bindApplication(XYZReader xyzReader);

    @Singleton
    @Binds
    abstract Context bindContext(Application application);
}
