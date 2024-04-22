package com.example.myfoodchoice.ModelFreeFoodAPI;

public class RecipeWrapper
{
    private RecipeCategories.RecipeCategory recipeCategory;
    private RecipeCuisines.RecipeCuisine recipeCuisine;

    public RecipeWrapper()
    {

    }

    public RecipeCategories.RecipeCategory getRecipeCategory()
    {
        return recipeCategory;
    }

    public void setRecipeCategory(RecipeCategories.RecipeCategory recipeCategory)
    {
        this.recipeCategory = recipeCategory;
    }

    public RecipeCuisines.RecipeCuisine getRecipeCuisine()
    {
        return recipeCuisine;
    }

    public void setRecipeCuisine(RecipeCuisines.RecipeCuisine recipeCuisine)
    {
        this.recipeCuisine = recipeCuisine;
    }
}
