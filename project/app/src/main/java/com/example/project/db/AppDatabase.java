package com.example.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.project.model.Content;
import com.example.project.model.Review;

@Database(entities = {Content.class, Review.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
}
