package com.example.project.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project.model.Content;
import com.example.project.model.ContentWithReview;
import com.example.project.model.Review;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void insertContent(Content content);

    @Insert
    void insertReview(Review review);

    @Query("SELECT * FROM Content")
    List<Content> getAllContents();

    @Query("SELECT * FROM Review WHERE contentId = :contentId")
    List<Review> getReviewsForContent(int contentId);

    @Query("SELECT * FROM Content WHERE watchedDate = :date")
    List<Content> getContentsByDate(String date);

    @Query("SELECT Content.*, Review.rating, Review.memo " +
            "FROM Content JOIN Review ON Content.id = Review.contentId")
    List<ContentWithReview> getReviewedContent();

    @Query("SELECT * FROM Review WHERE contentId = :contentId")
    List<Review> getReviewsByContentId(int contentId);

    @androidx.room.Update
    void updateReview(Review review);

    @androidx.room.Update
    void updateContent(Content content);

}
