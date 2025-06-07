package com.example.project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ReadDate {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int contentId;
    public String readDate;
}
