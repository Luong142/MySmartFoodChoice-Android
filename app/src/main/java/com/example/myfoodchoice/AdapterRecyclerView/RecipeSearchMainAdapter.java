package com.example.myfoodchoice.AdapterRecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnClickExpandRecipeDetailListener;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCuisines;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class RecipeSearchMainAdapter extends RecyclerView.Adapter<RecipeSearchMainAdapter.myViewHolder>
{
    private ArrayList<?> recipeArrayList;

    private OnClickExpandRecipeDetailListener onClickExpandRecipeDetailListener;

    public RecipeSearchMainAdapter(ArrayList<?> recipeArrayList,
                                   OnClickExpandRecipeDetailListener onClickExpandRecipeDetailListener)
    {
        this.recipeArrayList = recipeArrayList;
        this.onClickExpandRecipeDetailListener = onClickExpandRecipeDetailListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView recipeName;

        ImageView recipeImage;

        RecyclerView recipeDetailRecyclerView;

        public myViewHolder(@NonNull View itemView,
                            OnClickExpandRecipeDetailListener onClickExpandRecipeDetailListener)
        {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipeText);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeDetailRecyclerView = itemView.findViewById(R.id.recipeDetailRecyclerView);

            recipeDetailRecyclerView.setVisibility(View.GONE);

            itemView.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onClickExpandRecipeDetailListener.onExpandRecipeDetail(position);

                    if (recipeDetailRecyclerView.getVisibility() == View.GONE)
                    {
                        recipeDetailRecyclerView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        recipeDetailRecyclerView.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public RecipeSearchMainAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.search_recipe_dietitian_layout,
                parent, false);
        return new RecipeSearchMainAdapter.myViewHolder(itemView, onClickExpandRecipeDetailListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchMainAdapter.myViewHolder holder, int position)
    {
        // fixme: the problem is that there are two classes we need to switch or maybe they are the same
        // test first, probably I use caster.
        RecipeCategories.RecipeCategory recipeCategories = (RecipeCategories.RecipeCategory)
                recipeArrayList.get(position);
        RecipeCuisines.RecipeCuisine recipeCuisines = (RecipeCuisines.RecipeCuisine)
                recipeArrayList.get(position);

        String recipeName = recipeCategories.getStrMeal();
        String recipeImage = recipeCategories.getStrMealThumb();

        if (recipeName == null || recipeName.isEmpty())
        {
            recipeName = recipeCuisines.getStrMeal();
        }

        if (recipeImage == null || recipeImage.isEmpty())
        {
            recipeImage = recipeCuisines.getStrMealThumb();
        }


        holder.recipeName.setText(recipeName);
        holder.recipeImage.setImageURI((Uri) recipeImage);

    }

    @Override
    public int getItemCount()
    {
        return recipeArrayList.size();
    }

    public void updateRecipeArrayList(ArrayList<?> newRecipeArrayList)
    {
        // use this function to switch and update.
        this.recipeArrayList = newRecipeArrayList;
        this.notifyItemChanged(recipeArrayList.size() - 1);
    }
}
