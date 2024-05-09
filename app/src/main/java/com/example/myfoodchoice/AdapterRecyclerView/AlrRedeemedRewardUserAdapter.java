package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnAlrRedeemedRewardUserItemListener;
import com.example.myfoodchoice.ModelUtilities.Reward;
import com.example.myfoodchoice.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlrRedeemedRewardUserAdapter extends RecyclerView.Adapter<AlrRedeemedRewardUserAdapter.myViewHolder>
{
    private final ArrayList<Reward> rewards;

    private final OnAlrRedeemedRewardUserItemListener onAlrRedeemedRewardUserItemListener;

    public AlrRedeemedRewardUserAdapter(ArrayList<Reward> rewards,
                                        OnAlrRedeemedRewardUserItemListener onAlrRedeemedRewardUserItemListener)
    {
        this.rewards = rewards;
        this.onAlrRedeemedRewardUserItemListener = onAlrRedeemedRewardUserItemListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView rewardImage;

        TextView rewardName;
        TextView rewardDescription;

        TextView nameOnly;


        public myViewHolder(final View itemView, OnAlrRedeemedRewardUserItemListener onAlrRedeemedRewardUserItemListener)
        {
            super(itemView);
            rewardImage = itemView.findViewById(R.id.rewardImageView);
            rewardName = itemView.findViewById(R.id.rewardNameTextView);
            rewardDescription = itemView.findViewById(R.id.rewardDescriptionTextView);
            nameOnly = itemView.findViewById(R.id.nameOnly);

            itemView.setOnClickListener(v ->
            {
                if (onAlrRedeemedRewardUserItemListener != null)
                {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        onAlrRedeemedRewardUserItemListener.onClickRedeemedReward(position);
                    }
                }
            });
        }
    }

    // todo: update here.
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
                    .error(R.drawable.voucher)
                    .into(holder.rewardImage);

            holder.rewardName.setText(reward.getName());
            holder.rewardDescription.setText(reward.getDescription());

            // check here.
            holder.nameOnly.setText("Already redeemed voucher");
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.reward_user_redeemed_item_layout,
                parent, false);
        return new AlrRedeemedRewardUserAdapter.myViewHolder(itemView, onAlrRedeemedRewardUserItemListener);
    }

    @Override
    public int getItemCount()
    {
        return rewards.size();
    }
}
