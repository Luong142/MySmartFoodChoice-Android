package com.example.myfoodchoice.Model;

import java.util.Date;
import java.util.List;

public class Meal
{
    private int id;
    private String name;
    private Date timestamp;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private List<FoodItem> foodItems;

}
