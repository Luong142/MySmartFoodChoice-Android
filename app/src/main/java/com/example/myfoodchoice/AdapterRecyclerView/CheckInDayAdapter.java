package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnDailyCheckInListener;
import com.example.myfoodchoice.ModelUtilities.CheckInDay;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class CheckInDayAdapter extends RecyclerView.Adapter<CheckInDayAdapter.myViewHolder>
{
    private final OnDailyCheckInListener onDailyCheckInListener;

    private final ArrayList<CheckInDay> checkInDays;

    public CheckInDayAdapter(ArrayList<CheckInDay> checkInDays, OnDailyCheckInListener onDailyCheckInListener) {
        this.onDailyCheckInListener = onDailyCheckInListener;
        this.checkInDays = checkInDays;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView dayImage;
        public TextView dayText;

        public myViewHolder(final View itemView, OnDailyCheckInListener onDailyCheckInListener)
        {
            super(itemView);
            dayImage = itemView.findViewById(R.id.checkInDayImage);
            dayText = itemView.findViewById(R.id.checkInDayText);

            itemView.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onDailyCheckInListener.onCheckInClick(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.check_in_day_item_user_layout,
                parent, false);
        return new CheckInDayAdapter.myViewHolder(itemView, onDailyCheckInListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        CheckInDay checkInDay = checkInDays.get(position);
        holder.dayImage.setImageResource(checkInDay.getImageId());
        holder.dayText.setText(checkInDay.getDay());
    }

    @Override
    public int getItemCount()
    {
        return checkInDays.size();
    }
}
