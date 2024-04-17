package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnRewardItemRedeemClickListener;
import com.example.myfoodchoice.ModelUtilities.Reward;
import com.example.myfoodchoice.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RewardUserAdapter extends RecyclerView.Adapter<RewardUserAdapter.myViewHolder>
{
    private final ArrayList<Reward> rewards;
    private final OnRewardItemRedeemClickListener onRewardItemRedeemClickListener;

    public RewardUserAdapter(ArrayList<Reward> rewards, OnRewardItemRedeemClickListener onRewardItemRedeemClickListener)
    {
        this.rewards = rewards;
        this.onRewardItemRedeemClickListener = onRewardItemRedeemClickListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView rewardImage;
        public TextView rewardName;
        public TextView rewardDescription;

        public TextView rewardPoints;

        public Button redeemButton;

        public myViewHolder(final View itemView, OnRewardItemRedeemClickListener onRewardItemRedeemClickListener)
        {
            super(itemView);
            rewardImage = itemView.findViewById(R.id.rewardImageView);
            rewardName = itemView.findViewById(R.id.rewardNameTextView);
            rewardDescription = itemView.findViewById(R.id.rewardDescriptionTextView);
            rewardPoints = itemView.findViewById(R.id.rewardPointsTextView);
            redeemButton = itemView.findViewById(R.id.redeemButton);

            redeemButton.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onRewardItemRedeemClickListener.onRewardItemRedeemClick(position);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        Reward reward = rewards.get(position);
        // Log.d("RewardAdapter", "onDataChange: " + reward);
        // todo: check here later.
        if (reward != null)
        {
            // set profile picture here
            String rewardImageUrl = reward.getRewardImageUrl();
            Uri rewardUri = Uri.parse(rewardImageUrl);
            // Log.d("RewardAdapter", "onDataChange: " + rewardUri.toString());

            // FIXME: the image doesn't show because the image source is from Gallery within android device.
            // fixme: another problem is that the picture is black color, hence we need to load again
            // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
            Picasso.get()
                    .load(rewardUri)
                    .resize(50, 50)
                    .error(R.drawable.error)
                    .into(holder.rewardImage);
            
            holder.rewardName.setText(reward.getName());
            holder.rewardDescription.setText(reward.getDescription());
            holder.rewardPoints.setText(String.valueOf(reward.getPoints()));
        }
    }

    @NonNull
    @Override
    public RewardUserAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.reward_item_layout,
                parent, false);
        return new RewardUserAdapter.myViewHolder(itemView, onRewardItemRedeemClickListener);
    }

    @Override
    public int getItemCount()
    {
        return rewards.size();
    }
}
