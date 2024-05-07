package com.example.myfoodchoice.AdapterRecyclerView;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnActionMealListener;
import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.ModelNutrition.NutritionMeal;
import com.example.myfoodchoice.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealMainHistoryAdapter extends RecyclerView.Adapter<MealMainHistoryAdapter.myViewHolder>
{
    private ArrayList<NutritionMeal> nutritionMealArrayList;

    private final OnActionMealListener onActionMealListener;

    public MealMainHistoryAdapter(ArrayList<NutritionMeal> nutritionMealArrayList,
                                  OnActionMealListener onActionMealListener
    )
    {
        this.nutritionMealArrayList = nutritionMealArrayList;
        this.onActionMealListener = onActionMealListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateMeals(@NonNull ArrayList<NutritionMeal> newNutritionMeals)
    {
        this.nutritionMealArrayList = newNutritionMeals;
        // fixme: there is a bug that the meal can be duplicated when it read.
        // fixme: the problem is that array list has been replaced not correctly.

        // fixme this is not efficient, use this for more accurate result.
        this.notifyDataSetChanged();

        // this below algo is not accurate.
        /*

        int oldSize = this.mealArrayList.size();
        int newSize = newMeals.size();
        // Notify the adapter of the changes
        if (oldSize == 0)
        {

        }
        else
        // this is more efficient way
        {
            // If the old list was not empty, notify the adapter of the specific changes
            if (newSize > oldSize)
            {
                // If the new list is larger, notify the adapter of the added items
                this.notifyItemRangeInserted(oldSize, newSize - oldSize);
            }
            else if
            (newSize < oldSize)
            {
                // If the new list is smaller, notify the adapter of the removed items
                this.notifyItemRangeRemoved(newSize, oldSize - newSize);
            }
            else
            {
                // If the list sizes are the same, notify the adapter of the changed items
                this.notifyItemRangeChanged(0, newSize);
            }
        }
         */
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        // todo: init here for more item, I am not sure which attribute should be included.
        public TextView timeText;

        public TextView dateText;

        public TextView mealNumText;

        public RecyclerView foodDetailRecyclerView;

        public myViewHolder(final View itemView, OnActionMealListener onActionMealListener)
        {
            super(itemView);

            // todo: we might need to change this if we don't use the view button
            timeText = itemView.findViewById(R.id.timeMealText);
            dateText = itemView.findViewById(R.id.dateMealText);
            mealNumText = itemView.findViewById(R.id.mealNumText);

            // init the normal adapter
            foodDetailRecyclerView = itemView.findViewById(R.id.foodDetailsRecyclerView);
            // set the adapter
            //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.
                    // getApplicationContext());
            // foodDetailRecyclerView.setLayoutManager(layoutManager);
            foodDetailRecyclerView.setVisibility(View.GONE);

            itemView.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    // do nothing?
                    onActionMealListener.onClickMeal(position);
                    // toggle the visibility of the inner RecyclerView

                    if (foodDetailRecyclerView.getVisibility() == View.GONE)
                    {
                        foodDetailRecyclerView.setVisibility(View.VISIBLE);
                        // optionally, load or update the data for the inner RecyclerView here
                    }
                    else
                    {
                        foodDetailRecyclerView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public MealMainHistoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.meal_history_item_layout,
                parent, false);
        return new MealMainHistoryAdapter.myViewHolder(itemView, onActionMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MealMainHistoryAdapter.myViewHolder holder, int position)
    {
        NutritionMeal nutritionMeal = nutritionMealArrayList.get(position);

        // format time based on Locale.English
        SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        Date date = nutritionMeal.getDate();

        // format both date and time.
        String formattedDate = sdfDate.format(date);
        String formattedTime = sdfTime.format(date);

        // fixme: the problem is that in all meal should display item num correctly?
        if (nutritionMeal.isMorning())
        {
            holder.mealNumText.setText(String.format(Locale.ROOT, "Breakfast %d", position + 1));
        }

        if (nutritionMeal.isAfternoon())
        {
            holder.mealNumText.setText(String.format(Locale.ROOT, "Lunch %d", position + 1));
        }

        if (nutritionMeal.isNight())
        {
            holder.mealNumText.setText(String.format(Locale.ROOT, "Dinner %d", position + 1));

        }

        // set up the inner RecyclerView
        holder.foodDetailRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.foodDetailRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // fixme: the problem is that in all meal should display picture correctly.
        List<FoodItem.Item> items = nutritionMeal.getDishes().getItems();
        for (FoodItem.Item item : items)
        {
            Log.d("MealHistoryAdapter", "Item: " + item);
        }

        MealDetailHistoryAdapter innerAdapter = new MealDetailHistoryAdapter(items);
        holder.foodDetailRecyclerView.setAdapter(innerAdapter);

        holder.dateText.setText(formattedDate);
        holder.timeText.setText(formattedTime);
    }

    @Override
    public int getItemCount()
    {
        return nutritionMealArrayList.size();
    }
}
