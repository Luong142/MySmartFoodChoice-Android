package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnRecommendRecipeDietitianListener;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewUserProfileAdapter extends RecyclerView.Adapter<ViewUserProfileAdapter.myViewHolder>
{
    private final ArrayList<UserProfile> userProfile;

    private final OnRecommendRecipeDietitianListener onRecommendRecipeDietitianListener;

    public ViewUserProfileAdapter(ArrayList<UserProfile> userProfile,
                                  OnRecommendRecipeDietitianListener recommendRecipeDietitianListener)
    {
        this.userProfile = userProfile;
        this.onRecommendRecipeDietitianListener = recommendRecipeDietitianListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView userProfileName;

        ImageView userProfileImage; // remember to use Picasso

        CardView cardViewDetailUserProfile;

        TextView userProfileDetail;

        FloatingActionButton addRecommendedRecipeBtn;

        public myViewHolder(@NonNull View itemView,
                            OnRecommendRecipeDietitianListener onRecommendRecipeDietitianListener)
        {
            super(itemView);

            userProfileName = itemView.findViewById(R.id.userProfileName);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            cardViewDetailUserProfile = itemView.findViewById(R.id.cardViewDetailUserProfile);
            userProfileDetail = itemView.findViewById(R.id.userProfileDetail);
            addRecommendedRecipeBtn = itemView.findViewById(R.id.addRecommendedRecipeBtn);

            cardViewDetailUserProfile.setVisibility(View.GONE);

            itemView.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    if (cardViewDetailUserProfile.getVisibility() == View.GONE)
                    {
                        cardViewDetailUserProfile.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        cardViewDetailUserProfile.setVisibility(View.GONE);
                    }
                }
            });

            // override this function to transfer the user profile and establish new recipe based on that user.
            addRecommendedRecipeBtn.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onRecommendRecipeDietitianListener.recommendRecipeDietitian(position);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewUserProfileAdapter.myViewHolder holder, int position)
    {
        UserProfile userProfile = this.userProfile.get(position);

        if (userProfile != null)
        {
            // Log.d("ViewUserProfileAdapter", "onBindViewHolder: " + userProfile.getDetail());
            String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
            holder.userProfileName.setText(fullName);

            Picasso.get().load
                    (Uri.parse(userProfile.getProfileImageUrl())).into
                    (holder.userProfileImage);

            holder.userProfileDetail.setText(userProfile.getDetail());
        }
    }

    @NonNull
    @Override
    public ViewUserProfileAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.dietitian_view_user_profile_layout,
                parent, false);
        return new ViewUserProfileAdapter.myViewHolder(itemView, onRecommendRecipeDietitianListener);
    }

    @Override
    public int getItemCount()
    {
        return userProfile.size();
    }
}
