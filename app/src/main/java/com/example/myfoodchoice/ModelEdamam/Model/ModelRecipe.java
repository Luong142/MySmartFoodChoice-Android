package com.example.myfoodchoice.ModelEdamam.Model;

public class ModelRecipe
{
    private String label;
    private String image;
    private String source;
    private float yield;
    private float calories;

    private float totalWeight;

    public ModelRecipe()
    {
    }

    public ModelRecipe(String label, String image, String source, float yield, float calories, float totalWeight)
    {
        this.label = label;
        this.image = image;
        this.source = source;
        this.yield = yield;
        this.calories = calories;
        this.totalWeight = totalWeight;
    }
}
