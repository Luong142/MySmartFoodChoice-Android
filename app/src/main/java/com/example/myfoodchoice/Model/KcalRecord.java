package com.example.myfoodchoice.Model;

import androidx.annotation.NonNull;

import java.util.Locale;
import java.util.Objects;

public class KcalRecord
{
    private static final String labelBreakfast = "Breakfast";

    private static final String labelLunch = "Lunch";

    private static final String labelDinner = "Dinner";

    private static final String labelKcal = "kcal";

    private int breakfastKcal;

    private int lunchKcal;

    private int dinnerKcal;

    private int calories;

    public KcalRecord(int breakfastKcal, int lunchKcal, int dinnerKcal, int calories)
    {
        this.breakfastKcal = breakfastKcal;
        this.lunchKcal = lunchKcal;
        this.dinnerKcal = dinnerKcal;
        this.calories = calories;
    }

    @NonNull
    @Override
    public String toString()
    {
        // TODO: use this toString format for home page.
        return String.format(Locale.ROOT, "%s: %d %s\n\n" +
                "%s: %d %s\n\n" +
                "%s: %d %s",
                labelBreakfast, breakfastKcal, labelKcal, labelLunch, lunchKcal, labelKcal, labelDinner, dinnerKcal, labelKcal);
    }


    @Override
    public int hashCode() {
        return Objects.hash(breakfastKcal, lunchKcal, dinnerKcal, calories);
    }

    public int getBreakfastKcal() {
        return breakfastKcal;
    }

    public void setBreakfastKcal(int breakfastKcal) {
        this.breakfastKcal = breakfastKcal;
    }

    public int getLunchKcal() {
        return lunchKcal;
    }

    public void setLunchKcal(int lunchKcal) {
        this.lunchKcal = lunchKcal;
    }

    public int getDinnerKcal() {
        return dinnerKcal;
    }

    public void setDinnerKcal(int dinnerKcal) {
        this.dinnerKcal = dinnerKcal;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
