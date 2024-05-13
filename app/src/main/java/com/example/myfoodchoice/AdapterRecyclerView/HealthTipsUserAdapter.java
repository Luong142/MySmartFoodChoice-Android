package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnHealthTipsClickListener;
import com.example.myfoodchoice.ModelDietitian.HealthTips;
import com.example.myfoodchoice.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HealthTipsUserAdapter extends RecyclerView.Adapter<HealthTipsUserAdapter.myViewHolder>
{
    private final ArrayList<HealthTips> healthTips;
    private final OnHealthTipsClickListener onHealthTipsClickListener;

    public HealthTipsUserAdapter(ArrayList<HealthTips> healthTips, OnHealthTipsClickListener onHealthTipsClickListener)
    {
        this.healthTips = healthTips;
        this.onHealthTipsClickListener = onHealthTipsClickListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        public TextView healthTipName;
        public TextView healthTipDescription;

        public TextView dietitianProfileInfo;

        public ImageView dietitianProfileImage;

        public CardView cardViewHealthContent;

        public CardView dietitianLayout;

        public myViewHolder(final View itemView, OnHealthTipsClickListener onHealthTipsClickListener)
        {
            super(itemView);
            healthTipName = itemView.findViewById(R.id.healthTipTitleTextView);
            healthTipDescription = itemView.findViewById(R.id.healthTipDescriptionTextView);
            cardViewHealthContent = itemView.findViewById(R.id.cardViewHealthContent);
            dietitianProfileInfo = itemView.findViewById(R.id.dietitianProfileInfo);
            dietitianProfileImage = itemView.findViewById(R.id.dietitianProfileImage);
            dietitianLayout = itemView.findViewById(R.id.dietitianLayout);

            cardViewHealthContent.setVisibility(View.GONE);
            dietitianLayout.setVisibility(View.GONE);

            itemView.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    if (cardViewHealthContent.getVisibility() == View.GONE &&
                            onHealthTipsClickListener != null)
                    {
                        onHealthTipsClickListener.onHealthTipsClick(position);
                        dietitianLayout.setVisibility(View.VISIBLE);
                        cardViewHealthContent.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        cardViewHealthContent.setVisibility(View.GONE);
                        dietitianLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public HealthTipsUserAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_view_health_tips_item_layout,
                parent, false);
        return new myViewHolder(itemView, onHealthTipsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        HealthTips healthTip = healthTips.get(position);
        String name = healthTip.getTitle();
        String desc = healthTip.getContent();

        String titleFormat = String.format("Health Title\n%s", name);
        String descFormat = String.format("Health Content\n%s", desc);

        holder.healthTipName.setText(titleFormat);
        holder.healthTipDescription.setText(descFormat);

        Picasso.get().load(healthTip.getDietitianProfileImage()).into(holder.dietitianProfileImage);
        holder.dietitianProfileInfo.setText(healthTip.getDietitianInfo());
    }

    @Override
    public int getItemCount()
    {
        return healthTips.size();
    }
}
