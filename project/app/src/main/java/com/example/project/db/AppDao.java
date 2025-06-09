package com.example.project.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project.model.Content;
import com.example.project.model.ContentWithReview;
import com.example.project.model.ReadDate;
import com.example.project.model.Review;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void insertContent(Content content);

    @Insert
    void insertReview(Review review);

    @Delete
    void deleteContent(Content content);

    @Delete
    void deleteReview(Review review);

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

    @Query("DELETE FROM ReadDate WHERE contentId = :contentId")
    void deleteReadDatesByContentId(int contentId);

    @Query("DELETE FROM Review WHERE contentId = :contentId")
    void deleteReviewsByContentId(int contentId);

    @Query("SELECT Content.* FROM Content JOIN ReadDate ON Content.id = ReadDate.contentId WHERE ReadDate.readDate = :date")
    List<Content> getContentsByReadDate(String date);

    @androidx.room.Update
    void updateReview(Review review);

    @androidx.room.Update
    void updateContent(Content content);

    @Insert
    void insertReadDate(ReadDate readDate);

    @Query("SELECT * FROM ReadDate WHERE contentId = :contentId")
    List<ReadDate> getReadDatesByContentId(int contentId);

    @Query("SELECT * FROM Content WHERE title = :title AND watchedDate = :date LIMIT 1")
    Content findContentByTitleAndDate(String title, String date);

    @Query("SELECT Content.*, Review.rating, Review.memo " +
            "FROM Content LEFT JOIN Review ON Content.id = Review.contentId")
    List<ContentWithReview> getAllContentWithOptionalReview();

    @Delete
    void deleteReadDate(ReadDate readDate);

}
