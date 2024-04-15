package com.example.myfoodchoice.AdapterRecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnRecipeItemClickListener;
import com.example.myfoodchoice.ModelBusiness.Recipe;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.myViewHolder>
{
    private final ArrayList<Recipe> recipes;

    private final OnRecipeItemClickListener onRecipeItemClickListener;

    public RecipeItemAdapter(ArrayList<Recipe> recipes,  OnRecipeItemClickListener onRecipeClickListener)
    {
        this.recipes = recipes;
        this.onRecipeItemClickListener = onRecipeClickListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView recipeImage;
        public TextView recipeName;

        public TextView recipeDesc;

        public TextView rowID;

        public myViewHolder(final View itemView, OnRecipeItemClickListener onRecipeClickListener)
        {
            super(itemView);
            // rowID = itemView.findViewById(R.id.row_index_key);
            recipeName = itemView.findViewById(R.id.recipeNameTextView);
            recipeImage = itemView.findViewById(R.id.recipeImageView);
            recipeDesc = itemView.findViewById(R.id.recipeDescriptionTextView);

            itemView.setOnClickListener(v ->
            {
                if (onRecipeClickListener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        onRecipeClickListener.onRecipeItemClick(position);
                        // Log.d("ExhibitionInfoAdapter", "onBindViewHolder: " + position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecipeItemAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recipe_item_user_layout,
                parent, false);
        return new myViewHolder(itemView, onRecipeItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemAdapter.myViewHolder holder, int position)
    {
        // TODO: modify this part for recycler viewer.

        boolean isFocusable = false;

        String info = recipes.get(position).toString();
        String name = recipes.get(position).getName();
        String desc = recipes.get(position).getDescription();
        int imageId = recipes.get(position).getImageId();

        Log.d("ExhibitionInfoAdapter", "onBindViewHolder: " + recipes);
        // holder.rowID.setText("Row #" + (position + 1) + "  ");

        // Log.d("ExhibitionInfoAdapter", "onBindViewHolder: " + imageId);
        holder.recipeName.setText(name); // this one works
        holder.recipeDesc.setText(desc);
        holder.recipeImage.setImageResource(imageId);
        holder.recipeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // set the image resource
        // (drawable, gif, bitmap, etc. - see Resources class for more info)
        // TODO: it is working this recycler view has a scroller! pls scroll down!

        holder.itemView.setFocusable(isFocusable);

    }

    @Override
    public int getItemCount()
    {
        return recipes.size();
    }
}
