package com.example.myfoodchoice.ModelNutrition;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;

import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class NutritionMeal implements Parcelable
{
    // todo: do the part for log my meal with morning, afternoon, tonight?

    private String key; // todo: firebase key ID

    private boolean isMorning, isAfternoon, isNight;

    private double totalCalories, totalCholesterol, totalSugar, totalSodium;

    // todo: should I change this to isBreakfast, isLunch, isDinner?

    private Date date;

    private FoodItem dishes;

    public NutritionMeal()
    {

    }

    public NutritionMeal(String key, boolean isMorning, boolean isAfternoon,
                         boolean isNight, double totalCalories,
                         double totalCholesterol,
                         double totalSugar, double totalSodium,
                         FoodItem dishes)
    {
        this.key = key;
        this.isMorning = isMorning;
        this.isAfternoon = isAfternoon;
        this.isNight = isNight;
        this.totalCalories = totalCalories;
        this.totalCholesterol = totalCholesterol;
        this.totalSugar = totalSugar;
        this.totalSodium = totalSodium;
        this.dishes = dishes;
    }

    public void startDate()
    {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
        this.date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static final Creator<NutritionMeal> CREATOR = new Creator<NutritionMeal>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public NutritionMeal createFromParcel(Parcel in) {
            return new NutritionMeal(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public NutritionMeal[] newArray(int size) {
            return new NutritionMeal[size];
        }
    };

    public NutritionMeal(@NonNull Parcel in)
    {
        setKey(in.readString());
        this.isMorning = in.readByte() != 0;
        this.isAfternoon = in.readByte() != 0;
        this.isNight = in.readByte() != 0;
        // this will read the object
        this.dishes = in.readParcelable(FoodItem.class.getClassLoader());
        this.date = (Date) in.readSerializable();
        this.totalCalories = in.readDouble();
        this.totalCholesterol = in.readDouble();
        this.totalSugar = in.readDouble();
        this.totalSodium = in.readDouble();
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
        dest.writeParcelable(this.dishes, flags);        // Check if timeStamp is null before calling toString()
        dest.writeSerializable(this.date);
        dest.writeDouble(this.totalCalories);
        dest.writeDouble(this.totalCholesterol);
        dest.writeDouble(this.totalSugar);
        dest.writeDouble(this.totalSodium);
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Meal Details" +
                "{" +
                "Key: '" + key + '\'' +
                ", Meal Time: " + (isMorning? "Morning" : (isAfternoon? "Afternoon" : "Night")) +
                ", Total Calories: " + totalCalories +
                ", Total Cholesterol: " + totalCholesterol +
                ", Total Sugar: " + totalSugar +
                ", Total Sodium: " + totalSodium +
                ", Date: " + date +
                ", Dishes: " + dishes +
                '}';
    }

    public static String formatTime(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
        sdf.setTimeZone(TimeZone.getDefault()); // Set the time zone to UTC for consistency
        return sdf.format(date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getTotalCholesterol() {
        return totalCholesterol;
    }

    public void setTotalCholesterol(double totalCholesterol) {
        this.totalCholesterol = totalCholesterol;
    }

    public double getTotalSugar() {
        return totalSugar;
    }

    public void setTotalSugar(double totalSugar) {
        this.totalSugar = totalSugar;
    }

    public double getTotalSodium() {
        return totalSodium;
    }

    public void setTotalSodium(double totalSodium) {
        this.totalSodium = totalSodium;
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

    public FoodItem getDishes() {
        return dishes;
    }

    public void setDishes(FoodItem dishes) {
        this.dishes = dishes;
    }
}
