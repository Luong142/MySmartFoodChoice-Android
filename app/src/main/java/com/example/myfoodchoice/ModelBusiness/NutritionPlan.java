package com.example.myfoodchoice.ModelBusiness;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.ModelSignUp.UserProfile;

import java.util.List;

public class NutritionPlan
{
    private int id;
    private UserProfile userProfile;
    private double dailyCalories;
    private double dailyProtein;
    private double dailyCarbs;
    private double dailyFat;
    private List<Recipe> recipes;

    @NonNull
    @Override
    public String toString()
    {
        return "NutritionPlan{" +
                "id=" + id +
                ", userProfile=" + userProfile +
                ", dailyCalories=" + dailyCalories +
                ", dailyProtein=" + dailyProtein +
                ", dailyCarbs=" + dailyCarbs +
                ", dailyFat=" + dailyFat +
                ", recipes=" + recipes +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public double getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(double dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public double getDailyProtein() {
        return dailyProtein;
    }

    public void setDailyProtein(double dailyProtein) {
        this.dailyProtein = dailyProtein;
    }

    public double getDailyCarbs() {
        return dailyCarbs;
    }

    public void setDailyCarbs(double dailyCarbs) {
        this.dailyCarbs = dailyCarbs;
    }

    public double getDailyFat() {
        return dailyFat;
    }

    public void setDailyFat(double dailyFat) {
        this.dailyFat = dailyFat;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
