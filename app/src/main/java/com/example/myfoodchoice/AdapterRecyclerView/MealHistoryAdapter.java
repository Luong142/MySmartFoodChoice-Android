package com.example.myfoodchoice.AdapterRecyclerView;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnActionMealListener;
import com.example.myfoodchoice.ModelMeal.Meal;
import com.example.myfoodchoice.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MealHistoryAdapter extends RecyclerView.Adapter<MealHistoryAdapter.myViewHolder>
{
    private final ArrayList<Meal> mealArrayList;

    private final OnActionMealListener onActionMealListener;

    public MealHistoryAdapter(ArrayList<Meal> mealArrayList, OnActionMealListener onActionMealListener)
    {
        this.mealArrayList = mealArrayList;
        this.onActionMealListener = onActionMealListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        // todo: init here for more item, I am not sure which attribute should be included.
        public Button removeBtn;

        public Button viewBtn;
        public TextView timeText;

        public myViewHolder(final View itemView, OnActionMealListener onActionMealListener)
        {
            super(itemView);

            // todo: we might need to change this if we don't use the view button
            removeBtn = itemView.findViewById(R.id.removeBtn);
            timeText = itemView.findViewById(R.id.timeMealText);

            removeBtn.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onActionMealListener.onRemoveMeal(position);
                }
            });

            itemView.setOnClickListener(v ->
            {
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        onActionMealListener.onViewMeal(position);
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public MealHistoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.meal_history_item_layout,
                parent, false);
        return new MealHistoryAdapter.myViewHolder(itemView, onActionMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealHistoryAdapter.myViewHolder holder, int position)
    {
        Meal meal = mealArrayList.get(position);

        // todo: format the time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ROOT);

        // todo: set item here.
        Date date = meal.getDate();

        // Format the date to a string in the "HH:MM" format
        String formattedTime = sdf.format(date);

        holder.timeText.setText(formattedTime);
    }

    @Override
    public int getItemCount()
    {
        return mealArrayList.size();
    }
}
