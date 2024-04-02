package com.example.myfoodchoice.ModelCaloriesNinja;

import androidx.annotation.NonNull;

import java.util.List;

public class FoodItem
{
    private List<Item> items;

    // Getters and Setters
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // todo: food meal arrayList for meal log
    // todo: array list of dishes is a meal? =>

    @NonNull
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FoodItem{\n");
        sb.append(" items=\n");
        for (Item item : items)
        {
            sb.append("    ").append(item.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public static class Item
    {
        // todo: check attributes from API calories ninja
        private String name;
        private double calories;
        private double serving_size_g;
        private double fat_total_g;
        private double fat_saturated_g; // todo: can these measured in three different levels?
        private double protein_g;
        private double sodium_mg; // todo: this attribute, > 6 grams => warning high-salt, otherwise ok to consume
        private double potassium_mg;
        private double cholesterol_mg; // todo: this attribute
        private double carbohydrates_total_g;
        private double fiber_g;
        private double sugar_g; // todo: this attribute, > 6 grams => warning high-sugar, otherwise ok to consume

        public String classifyContentBasedOnUserHealth(boolean isHighCholesterol,
                                                       boolean isHighBloodPressure,
                                                       boolean isDiabetes)
        {
            StringBuilder recommendation = new StringBuilder();

            // Check for high cholesterol
            if (isHighCholesterol && cholesterol_mg > 0)
            {
                recommendation.append("Warning: High cholesterol detected. Consider reducing cholesterol content. ");
            }

            // Check for high blood pressure
            if (isHighBloodPressure && sodium_mg > 6)
            {
                recommendation.append("Warning: High blood pressure detected. Consider reducing sodium content. ");
            }

            // Check for diabetes
            if (isDiabetes && sugar_g > 12)
            {
                recommendation.append("Warning: Diabetes detected. Consider reducing sugar content. ");
            }

            if (recommendation.length() > 0)
            {
                return recommendation.toString();
            }
            else
            {
                return "It's safe to use this dish.";
            }
        }



        @NonNull
        @Override
        public String toString()
        {
            return "Item{" +
                    "name='" + name + "', " +
                    "calories=" + calories + ", " +
                    "serving_size_g=" + serving_size_g + ", " +
                    "fat_total_g=" + fat_total_g + ", " +
                    "fat_saturated_g=" + fat_saturated_g + ", " +
                    "protein_g=" + protein_g + ", " +
                    "sodium_mg=" + sodium_mg + ", " +
                    "potassium_mg=" + potassium_mg + ", " +
                    "cholesterol_mg=" + cholesterol_mg + ", " +
                    "carbohydrates_total_g=" + carbohydrates_total_g + ", " +
                    "fiber_g=" + fiber_g + ", " +
                    "sugar_g=" + sugar_g + "}";
        }


        public Item(String name, double calories, double serving_size_g, double fat_total_g, double fat_saturated_g, double protein_g, double sodium_mg, double potassium_mg, double cholesterol_mg, double carbohydrates_total_g, double fiber_g, double sugar_g) {
            this.name = name;
            this.calories = calories;
            this.serving_size_g = serving_size_g;
            this.fat_total_g = fat_total_g;
            this.fat_saturated_g = fat_saturated_g;
            this.protein_g = protein_g;
            this.sodium_mg = sodium_mg;
            this.potassium_mg = potassium_mg;
            this.cholesterol_mg = cholesterol_mg;
            this.carbohydrates_total_g = carbohydrates_total_g;
            this.fiber_g = fiber_g;
            this.sugar_g = sugar_g;
        }

        // Getters and Setters for each field
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

        public double getServing_size_g() {
            return serving_size_g;
        }

        public void setServing_size_g(double serving_size_g) {
            this.serving_size_g = serving_size_g;
        }

        public double getFat_total_g() {
            return fat_total_g;
        }

        public void setFat_total_g(double fat_total_g) {
            this.fat_total_g = fat_total_g;
        }

        public double getFat_saturated_g() {
            return fat_saturated_g;
        }

        public void setFat_saturated_g(double fat_saturated_g) {
            this.fat_saturated_g = fat_saturated_g;
        }

        public double getProtein_g() {
            return protein_g;
        }

        public void setProtein_g(double protein_g) {
            this.protein_g = protein_g;
        }

        public double getSodium_mg() {
            return sodium_mg;
        }

        public void setSodium_mg(double sodium_mg) {
            this.sodium_mg = sodium_mg;
        }

        public double getPotassium_mg() {
            return potassium_mg;
        }

        public void setPotassium_mg(double potassium_mg) {
            this.potassium_mg = potassium_mg;
        }

        public double getCholesterol_mg() {
            return cholesterol_mg;
        }

        public void setCholesterol_mg(double cholesterol_mg) {
            this.cholesterol_mg = cholesterol_mg;
        }

        public double getCarbohydrates_total_g() {
            return carbohydrates_total_g;
        }

        public void setCarbohydrates_total_g(double carbohydrates_total_g) {
            this.carbohydrates_total_g = carbohydrates_total_g;
        }

        public double getFiber_g() {
            return fiber_g;
        }

        public void setFiber_g(double fiber_g) {
            this.fiber_g = fiber_g;
        }

        public double getSugar_g() {
            return sugar_g;
        }

        public void setSugar_g(double sugar_g) {
            this.sugar_g = sugar_g;
        }
    }
}
