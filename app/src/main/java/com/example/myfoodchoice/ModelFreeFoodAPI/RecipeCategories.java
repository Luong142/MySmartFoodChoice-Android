package com.example.myfoodchoice.ModelFreeFoodAPI;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecipeCategories implements Serializable
{
  @SerializedName("meals") // this is to convert the correct key name.
  private List<RecipeCategory> RecipeCategory;

  public List<RecipeCategory> getRecipeCategory() {
    return this.RecipeCategory;
  }

  public void setRecipeCategory(List<RecipeCategory> RecipeCategory) {
    this.RecipeCategory = RecipeCategory;
  }

  @NonNull
  @Override
  public String toString()
  {
    return "RecipeCategories{" +
            "RecipeCategory=" + RecipeCategory +
            '}';
  }

  public static class RecipeCategory implements Serializable
  {
    private String strMealThumb;

    private String idMeal;

    private String strMeal;

    @NonNull
    @Override
    public String toString() {
      return "RecipeCategory{" +
              "strMealThumb='" + strMealThumb + '\'' +
              ", idMeal='" + idMeal + '\'' +
              ", strMeal='" + strMeal + '\'' +
              '}';
    }

    public String getStrMealThumb() {
      return this.strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
      this.strMealThumb = strMealThumb;
    }

    public String getIdMeal() {
      return this.idMeal;
    }

    public void setIdMeal(String idMeal) {
      this.idMeal = idMeal;
    }

    public String getStrMeal() {
      return this.strMeal;
    }

    public void setStrMeal(String strMeal) {
      this.strMeal = strMeal;
    }
  }
}
