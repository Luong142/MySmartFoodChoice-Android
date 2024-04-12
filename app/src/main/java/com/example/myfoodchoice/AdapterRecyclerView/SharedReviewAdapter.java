package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnReviewClickListener;
import com.example.myfoodchoice.ModelUtilities.Review;
import com.example.myfoodchoice.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SharedReviewAdapter extends RecyclerView.Adapter<SharedReviewAdapter.myViewHolder>
{
    private final ArrayList<Review> reviews;
    private final OnReviewClickListener onReviewClickListener;

    public SharedReviewAdapter(ArrayList<Review> reviews, OnReviewClickListener onReviewClickListener)
    {
        this.reviews = reviews;
        this.onReviewClickListener = onReviewClickListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        public TextView reviewText;
        public TextView reviewName;

        public RatingBar  ratingBar;

        public ImageView imageProfile;

        public myViewHolder(final View itemView, OnReviewClickListener onReviewClickListener)
        {
            super(itemView);
            reviewText = itemView.findViewById(R.id.reviewTextView);
            reviewName = itemView.findViewById(R.id.reviewNameTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageProfile = itemView.findViewById(R.id.profileImage);

            itemView.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onReviewClickListener.onReviewClick(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_item_layout,
                parent, false);
        return new SharedReviewAdapter.myViewHolder(itemView, onReviewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        Review review = reviews.get(position);
        if (review != null)
        {
            holder.reviewText.setText(review.getReviewText());
            holder.reviewName.setText(review.getDisplayName());
            holder.ratingBar.setRating(review.getRating());
            Uri image = Uri.parse(review.getProfileImage());
            Picasso.get()
                    .load(image)
                    .resize(150, 150)
                    .error(R.drawable.error)
                    .into(holder.imageProfile);
        }
        else
        {
            // Handle the null object, e.g., by setting default text or hiding the view
            holder.reviewText.setText("No review available");
            holder.reviewName.setText("No name here");
            holder.ratingBar.setRating(0);
            holder.imageProfile.setImageResource(R.drawable.error);
        }
    }

    @Override
    public int getItemCount()
    {
        return reviews.size();
    }
}
