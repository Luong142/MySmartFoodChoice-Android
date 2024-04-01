package com.example.myfoodchoice.ModelCaloriesNinja;

import androidx.annotation.NonNull;

public class Recipes
{
    // todo: use this for calories ninja API
    private String title;

    private String ingredients;

    private String servings;

    private String instructions;

    @NonNull
    @Override
    public String toString()
    {
        return "Recipes{ " +
                "title='" + title + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", servings='" + servings + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
