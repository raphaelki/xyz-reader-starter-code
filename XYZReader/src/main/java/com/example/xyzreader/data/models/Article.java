package com.example.xyzreader.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "articles")
public class Article {

    @PrimaryKey(autoGenerate = true)
    private final int dbId;
    @SerializedName("id")
    private final int serverId;
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

    public Article(int dbId, int serverId, String title, String author, String body, String thumbUrl, String photoUrl, float aspectRatio, String publishedDate) {
        this.dbId = dbId;
        this.serverId = serverId;
        this.title = title;
        this.author = author;
        this.body = body;
        this.thumbUrl = thumbUrl;
        this.photoUrl = photoUrl;
        this.aspectRatio = aspectRatio;
        this.publishedDate = publishedDate;
    }

    public int getDbId() {
        return dbId;
    }

    public int getServerId() {
        return serverId;
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
}
