package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnCreateRecipeFromSearchListener;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodDetailAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitFreeFoodClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSearchCategoryMainAdapter extends RecyclerView.Adapter<RecipeSearchCategoryMainAdapter.myViewHolder>
{
    private final ArrayList<RecipeCategories.RecipeCategory> recipeArrayList;

    private final OnCreateRecipeFromSearchListener onCreateRecipeFromSearchListener;

    // todo: https://www.youtube.com/watch?v=MWlxFccYit8&ab_channel=larntech , check this tutorial for search

    // use this if we want to use onClick
    public RecipeSearchCategoryMainAdapter(ArrayList<RecipeCategories.RecipeCategory> recipeArrayList,
                                           OnCreateRecipeFromSearchListener onCreateRecipeFromSearchListener)
    {
        this.recipeArrayList = recipeArrayList;
        this.onCreateRecipeFromSearchListener = onCreateRecipeFromSearchListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView recipeName;

        ImageView recipeImage;

        CardView cardViewDetailRecipe;

        TextView recipeDetailText;

        FloatingActionButton createRecipeBtn;

        public myViewHolder(@NonNull View itemView, OnCreateRecipeFromSearchListener
                onCreateRecipeFromSearchListener)
        {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipeText);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            cardViewDetailRecipe = itemView.findViewById(R.id.cardViewDetailRecipe);
            recipeDetailText = itemView.findViewById(R.id.recipeDetailTextView);
            createRecipeBtn = itemView.findViewById(R.id.createRecipeBtn);

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

            createRecipeBtn.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    // do nothing?
                    onCreateRecipeFromSearchListener.onCreateRecipeFromSearch(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecipeSearchCategoryMainAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.dietitian_search_recipe_layout,
                parent, false);
        return new RecipeSearchCategoryMainAdapter.myViewHolder(itemView, onCreateRecipeFromSearchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchCategoryMainAdapter.myViewHolder holder, int position)
    {
        // fixme: the problem is that there are two classes we need to switch or maybe they are the same
        RecipeCategories.RecipeCategory recipeCategory = (RecipeCategories.RecipeCategory)
                recipeArrayList.get(position);

        // get the API for detail
        FreeFoodDetailAPI freeFoodDetailAPI = RetrofitFreeFoodClient.getRetrofitFreeInstance().
                create(FreeFoodDetailAPI.class);


        if (recipeCategory != null)
        {
            String recipeName = recipeCategory.getStrMeal();
            String recipeImage = recipeCategory.getStrMealThumb();
            // Log.d("Adapter", "Value here: " + recipeName + " " + recipeImage);

            holder.recipeName.setText(recipeName);
            Picasso.get()
                    .load(Uri.parse(recipeImage))
                    .resize(150, 150)
                    .error(R.drawable.error)
                    .into(holder.recipeImage);

            freeFoodDetailAPI.searchMealByName(recipeName)
                    .enqueue(new Callback<Dish>()
                    {
                        @Override
                        public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response)
                        {
                            if (response.isSuccessful())
                            {
                                Dish dish = response.body();
                                if (dish != null)
                                {
                                    holder.recipeDetailText.setText(dish.toString());
                                }
                            }
                            else
                            {
                                Log.d("Adapter", "onResponse: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t)
                        {
                            Log.d("Adapter", "onFailure: " + t.getMessage());
                        }
                    });
        }
    }

    @Override
    public int getItemCount()
    {
        return recipeArrayList.size();
    }
}
