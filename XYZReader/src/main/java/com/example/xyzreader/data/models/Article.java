package com.example.xyzreader.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "articles")
public class Article {

    @PrimaryKey
    @SerializedName("id")
    private final int id;
    @SerializedName("title")
    private final String title;
    @SerializedName("author")
    private final String author;
    @SerializedName("body")
    private final String body;
    @SerializedName("thumb")
    private final String thumbUrl;
    @SerializedName("photo")
    private final String photoUrl;
    @SerializedName("aspect_ratio")
    private final float aspectRatio;
    @SerializedName("published_date")
    private final String publishedDate;

    public Article(int id, String title, String author, String body, String thumbUrl, String photoUrl, float aspectRatio, String publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.body = body;
        this.thumbUrl = thumbUrl;
        this.photoUrl = photoUrl;
        this.aspectRatio = aspectRatio;
        this.publishedDate = publishedDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;
        Article article = (Article) obj;
        return author.equals(article.author)
                && body.equals(article.body)
                && id == article.id
                && thumbUrl.equals(article.thumbUrl)
                && photoUrl.equals(article.photoUrl)
                && aspectRatio == article.aspectRatio
                && publishedDate.equals(article.publishedDate)
                && title.equals(article.title);

    }
}
