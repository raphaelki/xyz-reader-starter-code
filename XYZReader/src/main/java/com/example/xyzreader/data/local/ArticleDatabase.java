package com.example.xyzreader.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.xyzreader.data.models.Article;

@Database(entities = {Article.class}, version = 1)
public abstract class ArticleDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();
}
