package com.example.xyzreader.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.xyzreader.data.models.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticles(List<Article> articles);

    @Query("SELECT * FROM articles")
    LiveData<List<Article>> getArticles();

    @Query("SELECT * FROM articles WHERE dbId =:articleId")
    LiveData<Article> getArticle(int articleId);
}
