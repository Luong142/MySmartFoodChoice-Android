package com.example.myfoodchoice.ModelUtilities;

import androidx.annotation.NonNull;

public class Review
{
    private String key; // Firebase key
    private String reviewText;
    private float rating;

    private String displayName;

    private String profileImage;

    public Review()
    {
        // for firebase
    }

    public Review(String reviewText, String displayName, float rating)
    {
        this.displayName = displayName;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public Review(String key, String reviewText, String displayName, float rating)
    {
        this.key = key;
        this.reviewText = reviewText;
        this.rating = rating;
        this.displayName = displayName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Review{" +
                "key='" + key + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", displayName='" + displayName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}
