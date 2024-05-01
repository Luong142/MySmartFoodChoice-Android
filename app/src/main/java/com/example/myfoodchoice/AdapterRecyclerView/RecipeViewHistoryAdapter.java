package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class RecipeViewHistoryAdapter extends RecyclerView.Adapter<RecipeViewHistoryAdapter.myViewHolder>
{
    private final ArrayList<Dish> recipeArrayList;

    public RecipeViewHistoryAdapter(ArrayList<Dish> recipeArrayList)
    {
        this.recipeArrayList = recipeArrayList;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView recipeName, recipeCategory
                , recipeCuisine
                , recipeIngredients, recipeInstructions;


        CardView cardViewDetailRecipe;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // init here
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCategory = itemView.findViewById(R.id.recipeCategory);
            recipeCuisine = itemView.findViewById(R.id.recipeCuisine);
            recipeIngredients = itemView.findViewById(R.id.ingredientsView);
            recipeInstructions = itemView.findViewById(R.id.instructionsView);

            // card view
            cardViewDetailRecipe = itemView.findViewById(R.id.cardViewDetailRecipe);
            cardViewDetailRecipe.setVisibility(View.GONE);

            itemView.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    if (cardViewDetailRecipe.getVisibility() == View.GONE)
                    {
                        cardViewDetailRecipe.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        cardViewDetailRecipe.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        Dish recipe = recipeArrayList.get(position);

        // init the arraylist of ingredient
        Dish.Meals meals = recipe.getMeals().get(0);
        if (meals.getIngredientsSearch() == null)
        {

        }

        holder.recipeName.setText(meals.getStrMeal());
        holder.recipeCategory.setText(meals.getStrCategory());
        holder.recipeCuisine.setText(meals.getStrArea());
        holder.recipeIngredients.setText(meals.displayIngredientsSearch());
        holder.recipeInstructions.setText(meals.getStrInstructions());
    }

    @NonNull
    @Override
    public RecipeViewHistoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.dietitian_view_record_recipe_manual_layout,
                parent, false);
        return new RecipeViewHistoryAdapter.myViewHolder(itemView);
    }

    @Override
    public int getItemCount()
    {
        return recipeArrayList.size();
    }
}
