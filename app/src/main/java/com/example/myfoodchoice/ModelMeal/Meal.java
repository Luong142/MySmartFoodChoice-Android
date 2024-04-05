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


    private List<FoodItem.Item> dishes;

    public Meal()
    {

    }

    public Meal(String key, boolean isMorning, boolean isAfternoon, boolean isNight, List<FoodItem.Item> dishes)
    {
        this.key = key;
        this.isMorning = isMorning;
        this.isAfternoon = isAfternoon;
        this.isNight = isNight;
        this.dishes = dishes;
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
        this.dishes = in.createTypedArrayList(FoodItem.Item.CREATOR);
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
        dest.writeList(this.dishes);
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
                ", dishes=" + dishes +
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

    public List<FoodItem.Item> getDishes() {
        return dishes;
    }

    public void setDishes(List<FoodItem.Item> dishes) {
        this.dishes = dishes;
    }
}
