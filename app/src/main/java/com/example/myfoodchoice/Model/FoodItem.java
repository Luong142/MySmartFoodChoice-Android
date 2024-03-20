package com.example.myfoodchoice.Model;

import androidx.annotation.NonNull;

public class FoodItem
{
    private int id;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;

    public FoodItem()
    {

    }

    public FoodItem(int id, String name, double calories, double protein, double carbs, double fat)
    {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }


    @NonNull
    @Override
    public String toString()
    {
        return "FoodItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", protein=" + protein +
                ", carbs=" + carbs +
                ", fat=" + fat +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }
}
