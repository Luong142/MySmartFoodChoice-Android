package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnHealthTipsClickListener;
import com.example.myfoodchoice.ModelBusiness.HealthTips;
import com.example.myfoodchoice.R;

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

        public myViewHolder(final View itemView, OnHealthTipsClickListener onHealthTipsClickListener)
        {
            super(itemView);
            healthTipName = itemView.findViewById(R.id.healthTipTitleTextView);
            healthTipDescription = itemView.findViewById(R.id.healthTipDescriptionTextView);

            itemView.setOnClickListener(v ->
            {
                if (onHealthTipsClickListener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        onHealthTipsClickListener.onHealthTipsClick(position);
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
                R.layout.health_tips_item_layout,
                parent, false);
        return new myViewHolder(itemView, onHealthTipsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        HealthTips healthTip = healthTips.get(position);
        String name = healthTip.getName();
        String desc = healthTip.getDescription();

        holder.healthTipName.setText(name);
        holder.healthTipDescription.setText(desc);
    }

    @Override
    public int getItemCount()
    {
        return healthTips.size();
    }

}
