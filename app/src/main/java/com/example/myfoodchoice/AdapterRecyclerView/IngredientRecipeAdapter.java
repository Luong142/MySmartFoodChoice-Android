package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnActionIngredientListener;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class IngredientRecipeAdapter extends RecyclerView.Adapter<IngredientRecipeAdapter.myViewHolder>
{
    private final ArrayList<Dish.Meals> ingredientArrayList;

    private final OnActionIngredientListener onActionIngredientListener;

    public IngredientRecipeAdapter(ArrayList<Dish.Meals> ingredientArrayList,
                                   OnActionIngredientListener onActionIngredientListener)
    {
        this.ingredientArrayList = ingredientArrayList;
        this.onActionIngredientListener = onActionIngredientListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView ingredientName;

        public myViewHolder(final View itemView, OnActionIngredientListener onActionIngredientListener)
        {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);

            itemView.setOnClickListener(v ->
            {
                if (onActionIngredientListener != null)
                {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        onActionIngredientListener.onDeleteIngredient(position);
                    }
                }
            });
        }
    }

    // todo: update here.
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        Dish.Meals ingredient = ingredientArrayList.get(position);

        if (ingredient != null)
        {
            holder.ingredientName.setText(ingredient.getStrIngredient1());
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ingredient_layout,
                parent, false);
        return new IngredientRecipeAdapter.myViewHolder(itemView, onActionIngredientListener);
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }
}
