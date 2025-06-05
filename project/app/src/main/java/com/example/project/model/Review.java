package com.example.project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Review {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int contentId;  // Content.id와 연결
    public float rating;
    public String comment;
    public String memo;
}
