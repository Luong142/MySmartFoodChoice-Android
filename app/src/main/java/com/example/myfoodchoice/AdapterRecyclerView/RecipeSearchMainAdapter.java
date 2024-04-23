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

import com.example.myfoodchoice.AdapterInterfaceListener.OnClickExpandRecipeDetailListener;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeWrapper;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class RecipeSearchMainAdapter extends RecyclerView.Adapter<RecipeSearchMainAdapter.myViewHolder>
{
    private ArrayList<?> recipeArrayList;

    private OnClickExpandRecipeDetailListener onClickExpandRecipeDetailListener;

    // todo: https://www.youtube.com/watch?v=MWlxFccYit8&ab_channel=larntech , check this tutorial for search

    public RecipeSearchMainAdapter(ArrayList<?> recipeArrayList,
                                   OnClickExpandRecipeDetailListener onClickExpandRecipeDetailListener)
    {
        this.recipeArrayList = recipeArrayList;
        this.onClickExpandRecipeDetailListener = onClickExpandRecipeDetailListener;
    }

    public RecipeSearchMainAdapter(ArrayList<?> recipeArrayList)
    {
        this.recipeArrayList = recipeArrayList;
    }

    public OnClickExpandRecipeDetailListener getOnClickExpandRecipeDetailListener()
    {
        return onClickExpandRecipeDetailListener;
    }

    public void setOnClickExpandRecipeDetailListener(OnClickExpandRecipeDetailListener
                                                             onClickExpandRecipeDetailListener) {
        this.onClickExpandRecipeDetailListener = onClickExpandRecipeDetailListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView recipeName;

        ImageView recipeImage;

        CardView cardViewDetailRecipe;

        public myViewHolder(@NonNull View itemView,
                            OnClickExpandRecipeDetailListener onClickExpandRecipeDetailListener)
        {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipeText);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            cardViewDetailRecipe = itemView.findViewById(R.id.cardViewDetailRecipe);

            cardViewDetailRecipe.setVisibility(View.GONE);

            itemView.setOnClickListener(v ->
            {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    onClickExpandRecipeDetailListener.onExpandRecipeDetail(position);

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

        RecipeWrapper recipeWrapper = new RecipeWrapper();
        recipeWrapper.setRecipeCategory(recipeWrapper.getRecipeCategory());
        recipeWrapper.setRecipeCuisine(recipeWrapper.getRecipeCuisine());

        String recipeName = recipeWrapper.getRecipeCuisine().getStrMeal();
        String recipeImage = recipeWrapper.getRecipeCuisine().getStrMealThumb();

        if (recipeName == null || recipeName.isEmpty())
        {
            recipeName = recipeWrapper.getRecipeCategory().getStrMeal();
        }

        if (recipeImage == null || recipeImage.isEmpty())
        {
            recipeImage = recipeWrapper.getRecipeCategory().getStrMealThumb();
        }

        Log.d("Adapter", "Value here: " + recipeName + " " + recipeImage);

        Uri convert = Uri.parse(recipeImage);

        holder.recipeName.setText(recipeName);
        holder.recipeImage.setImageURI(convert);

        // set adapter here

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
