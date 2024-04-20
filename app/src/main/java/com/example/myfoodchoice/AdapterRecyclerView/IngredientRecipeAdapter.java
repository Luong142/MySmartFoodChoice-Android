package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnActionIngredientListener;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class IngredientRecipeAdapter extends RecyclerView.Adapter<IngredientRecipeAdapter.myViewHolder>
{
    private final ArrayList<String> ingredientArrayList;

    private final OnActionIngredientListener onActionIngredientListener;

    public IngredientRecipeAdapter(ArrayList<String> ingredientArrayList,
                                   OnActionIngredientListener onActionIngredientListener)
    {
        this.ingredientArrayList = ingredientArrayList;
        this.onActionIngredientListener = onActionIngredientListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView ingredientName;

        Button removeIngredientBtn;

        public myViewHolder(final View itemView, OnActionIngredientListener onActionIngredientListener)
        {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            removeIngredientBtn = itemView.findViewById(R.id.removeBtn);

            removeIngredientBtn.setOnClickListener(v ->
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
        String ingredient = ingredientArrayList.get(position);

        if (ingredient != null)
        {
            holder.ingredientName.setText(ingredient);
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
