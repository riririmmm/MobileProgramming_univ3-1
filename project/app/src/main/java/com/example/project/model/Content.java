package com.example.project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Content {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String category; // 도서, 영화, 전시/공연
    public String imageUrl;
    public String startDate;
    public String endDate;
    public String place;
    public int pageCount;
    public String watchedDate;
    public float rating;
}
