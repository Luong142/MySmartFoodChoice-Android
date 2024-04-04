package com.example.myfoodchoice.ModelMeal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;

import org.jetbrains.annotations.Contract;

import java.util.List;

public class Meal implements Parcelable
{
    // todo: do the part for log my meal with morning, afternoon, tonight?

    private String key; // todo: firebase key ID

    private boolean isMorning, isAfternoon, isNight;

    private String name;

    private double calories;

    private double sodium_mg; // todo: this attribute, > 6 grams => warning high-salt, otherwise ok to consume

    private double cholesterol_mg; // todo: this attribute

    private double sugar_g; // todo: this attribute, > 6 grams => warning high-sugar, otherwise ok to consume

    private List<FoodItem.Item> dishes;

    public Meal()
    {

    }

    public Meal(String key, boolean isMorning, boolean isAfternoon,
                boolean isNight, String name, double calories,
                double sodium_mg, double cholesterol_mg, double sugar_g)
    {
        this.key = key;
        this.isMorning = isMorning;
        this.isAfternoon = isAfternoon;
        this.isNight = isNight;
        this.name = name;
        this.calories = calories;
        this.sodium_mg = sodium_mg;
        this.cholesterol_mg = cholesterol_mg;
        this.sugar_g = sugar_g;
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public Meal(@NonNull Parcel in)
    {
        setKey(in.readString());
        this.isMorning = in.readByte() != 0;
        this.isAfternoon = in.readByte() != 0;
        this.isNight = in.readByte() != 0;
        this.name = in.readString();
        this.calories = in.readDouble();
        this.sodium_mg = in.readDouble();
        this.cholesterol_mg = in.readDouble();
        this.sugar_g = in.readDouble();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeString(this.key);
        dest.writeByte(this.isMorning ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAfternoon ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNight ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeDouble(this.calories);
        dest.writeDouble(this.sodium_mg);
        dest.writeDouble(this.cholesterol_mg);
        dest.writeDouble(this.sugar_g);
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Meal{" +
                "key='" + key + '\'' +
                ", isMorning=" + isMorning +
                ", isAfternoon=" + isAfternoon +
                ", isNight=" + isNight +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", sodium_mg=" + sodium_mg +
                ", cholesterol_mg=" + cholesterol_mg +
                ", sugar_g=" + sugar_g +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public boolean isAfternoon() {
        return isAfternoon;
    }

    public void setAfternoon(boolean afternoon) {
        isAfternoon = afternoon;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
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

    public double getSodium_mg() {
        return sodium_mg;
    }

    public void setSodium_mg(double sodium_mg) {
        this.sodium_mg = sodium_mg;
    }

    public double getCholesterol_mg() {
        return cholesterol_mg;
    }

    public void setCholesterol_mg(double cholesterol_mg) {
        this.cholesterol_mg = cholesterol_mg;
    }

    public double getSugar_g() {
        return sugar_g;
    }

    public void setSugar_g(double sugar_g) {
        this.sugar_g = sugar_g;
    }
}
