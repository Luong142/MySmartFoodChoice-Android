package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class MealDetailHistoryAdapter extends RecyclerView.Adapter<MealDetailHistoryAdapter.myViewHolder>
{
    private final List<FoodItem.Item> itemArrayList;


    public MealDetailHistoryAdapter(List<FoodItem.Item> itemArrayList)
    {
        this.itemArrayList = itemArrayList;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        // todo: init here for more item, I am not sure which attribute should be included.

        public ImageView foodImage;
        public TextView foodName;

        public TextView foodDetail;

        public myViewHolder(final View itemView)
        {
            super(itemView);

            // todo: we might need to change this if we don't use the view button
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodNameText);
            foodDetail = itemView.findViewById(R.id.foodDetailsText);

            /*
            itemView.setOnClickListener(v ->
            {
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        // todo: we can decide if we want to click on the item.
                        onActionDetailMealListener.onActionDetailMeal(position); // to view based on expandable view.
                    }
                }
            });
             */
        }
    }
    @NonNull
    @Override
    public MealDetailHistoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.meal_detail_item_layout,
                parent, false);
        return new MealDetailHistoryAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        FoodItem.Item item = itemArrayList.get(position);

        // fixme: the problem is that food image didn't display properly.
        // todo: this is a part where we retrieve the data from firebase and the
        //  Uri.parse(item.getFoodImage()) is not working.
        // fixme: new problem is that the image appear only when

        // todo: the pattern is when we add new meal with uploaded food image for each item,
        //  the food image is ok to display for first time.
        // when we restart the app the food image has error instead.
        if (item != null)
        {
            // Log.d("Test", "onBindViewHolder: " + item.getFoodImage());
            // set the item
            Picasso.get()
                    .load(Uri.parse(item.getFoodImage()))
                    .resize(150, 150)
                    .error(R.drawable.food_placeholder)
                    .into(holder.foodImage);
            holder.foodName.setText(item.getName());

            // todo: this string is to display the detail of food, when it is visible.
            String details = String.format(Locale.ROOT, "Calories: " +
                    "%.2f kcal\n" +
                    "Sodium: %.2f mg\n" +
                    "Cholesterol: %.2f mg\n" +
                    "Sugar: %.2f g",
                    item.getCalories(),
                    item.getSodium_mg(),
                    item.getCholesterol_mg(),
                    item.getSugar_g());

            holder.foodDetail.setText(details);
        }
        else
        {
            holder.foodName.setText("No name here");
            holder.foodName.setText("Error");
            holder.foodDetail.setText("Error");
        }
    }

    @Override
    public int getItemCount()
    {
        return itemArrayList.size();
    }
}
