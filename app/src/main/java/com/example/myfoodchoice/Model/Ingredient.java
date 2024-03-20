package com.example.myfoodchoice.Model;

import androidx.annotation.NonNull;

public class Ingredient
{
    private int id;
    private String name;
    private double amount;
    private Unit unit;

    // Getters, setters, and constructors


    @NonNull
    @Override
    public String toString()
    {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", unit=" + unit +
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
