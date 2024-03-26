package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class Review
{
    private String reviewText;
    private float rating;
    private String reviewerType;
    // Add any other relevant properties

    // Constructors, getters, and setters


    public Review(String reviewText, float rating, String reviewerType)
    {
        this.reviewText = reviewText;
        this.rating = rating;
        this.reviewerType = reviewerType;
    }

    @NonNull
    @Override
    public String toString() {
        return "Review{" +
                "reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", reviewerType=" + reviewerType +
                '}';
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewerType() {
        return reviewerType;
    }

    public void setReviewerType(String reviewerType) {
        this.reviewerType = reviewerType;
    }
}
