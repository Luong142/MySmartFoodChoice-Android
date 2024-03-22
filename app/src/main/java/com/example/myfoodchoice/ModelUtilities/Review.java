package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class Review
{
    private String reviewText;
    private int rating;
    private ReviewerType reviewerType;
    // Add any other relevant properties

    // Constructors, getters, and setters


    public Review(String reviewText, int rating, ReviewerType reviewerType)
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ReviewerType getReviewerType() {
        return reviewerType;
    }

    public void setReviewerType(ReviewerType reviewerType) {
        this.reviewerType = reviewerType;
    }
}
