package com.example.myfoodchoice.ModelFreeFoodAPI;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeCuisines implements Serializable
{
  @SerializedName("meals")
  private ArrayList<RecipeCuisine> RecipeCuisine;

  public ArrayList<RecipeCuisine> getRecipeCuisine() {
    return this.RecipeCuisine;
  }

  public void setRecipeCuisine(ArrayList<RecipeCuisine> RecipeCuisine) {
    this.RecipeCuisine = RecipeCuisine;
  }

  @NonNull
  @Override
  public String toString()
  {
    return "RecipeCuisines{" +
            "RecipeCuisine=" + RecipeCuisine +
            '}';
  }

  public static class RecipeCuisine implements Serializable {
    private String strMealThumb;

    private String idMeal;

    private String strMeal;

    @NonNull
    @Override
    public String toString() {
      return "RecipeCuisine{" +
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
