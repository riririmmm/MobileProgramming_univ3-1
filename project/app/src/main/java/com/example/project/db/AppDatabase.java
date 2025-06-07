package com.example.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project.model.Content;
import com.example.project.model.ReadDate;
import com.example.project.model.Review;
import com.example.project.util.StringListConverter;

@Database(entities = {Content.class, Review.class, ReadDate.class}, version = 2)
@TypeConverters({StringListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
}
