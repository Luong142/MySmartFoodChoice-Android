package com.example.myfoodchoice.UserFragment;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelNutrition.NutritionMeal;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class UserLogMealFragment extends Fragment
{
    private static final int NOTIFICATION_ID = 1;
    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID, gender;

    final static String PATH_USERPROFILE = "Android User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Android Meals";

    final static String TAG = "UserLogMealFragment";

    // TODO: declare UI component
    TextView caloriesText, cholesterolText, sugarText, saltText;

    double totalCalories, totalCholesterol, totalSugar, totalSalt;

    double maxCalories, maxCholesterol, maxSugar, maxSalt;

    double percentageCalories, percentageCholesterol, percentageSugar, percentageSalt;

    boolean isDiabetes, isHighBloodPressure, isHighCholesterol;

    NutritionMeal nutritionMeal;

    Date currentDateMeal, currentRealTimeDate;

    CircularProgressIndicator progressBarCalories, progressBarCholesterol, progressBarSugar, progressBarSalt;
    // fixme: apply this pie chart instead of progress bar.

    Button morningBtn, afternoonBtn, nightBtn;

    TextView caloriesCountText, cholesterolCountText, sugarCountText, saltCountText, timeTextView;

    Bundle bundleStore;

    UserLogMealNutritionAnalysisFragment userLogMealNutritionAnalysisFragment;

    View view;

    StringBuilder alertDialogMessage;

    private final HashMap<String, NutritionMeal> mealCache = new HashMap<>();
    public SimpleDateFormat sdfDate;
    public String formattedCurrentDate, formattedMealDate;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            gender = "Male"; // fixme: by default value
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile =
                    firebaseDatabase.getReference(PATH_USERPROFILE).child(userID);

            databaseReferenceDailyFoodIntake =
                    firebaseDatabase.getReference(PATH_DAILY_FOOD_INTAKE).child(userID);

            // to set the maximum of nutrition value based on gender.
            databaseReferenceUserProfile.addValueEventListener(onGenderHealthValueListener());

            // to retrieve the total nutrition value from user.
            databaseReferenceDailyFoodIntake.addChildEventListener(onTotalNutritionValueListener());
        }

        // todo: init boolean value
        isDiabetes = false;
        isHighBloodPressure = false;
        isHighCholesterol = false;

        // todo: init objects
        userProfile = new UserProfile();
        nutritionMeal = new NutritionMeal();
        bundleStore = new Bundle();
        userLogMealNutritionAnalysisFragment = new UserLogMealNutritionAnalysisFragment();

        // todo: init progress bar
        progressBarCalories = view.findViewById(R.id.progressBarCalories);
        progressBarCholesterol = view.findViewById(R.id.progressBarCholesterol);
        progressBarSugar = view.findViewById(R.id.progressBarSugar);
        progressBarSalt = view.findViewById(R.id.progressBarSodium);

        // for text percentage.
        caloriesText = view.findViewById(R.id.progressCaloriesTextView);
        cholesterolText = view.findViewById(R.id.progressCholesterolTextView);
        sugarText = view.findViewById(R.id.progressSugarTextView);
        saltText = view.findViewById(R.id.progressSodiumTextView);

        totalCalories = 0.0;
        totalCholesterol = 0.0;
        totalSugar = 0.0;
        totalSalt = 0.0;

        maxCalories = 2200;
        maxCholesterol = 300;
        maxSugar = 36;
        maxSalt = 2300;

        // TODO: init UI components
        morningBtn = view.findViewById(R.id.morningButton);
        afternoonBtn = view.findViewById(R.id.afternoonButton);
        nightBtn = view.findViewById(R.id.nightButton);

        // for text view
        caloriesCountText = view.findViewById(R.id.caloriesNumTextView);
        cholesterolCountText = view.findViewById(R.id.cholesterolNumTextView);
        sugarCountText = view.findViewById(R.id.sugarNumTextView);
        saltCountText = view.findViewById(R.id.sodiumNumTextView);
        timeTextView = view.findViewById(R.id.timeTextView);

        // display time here
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
        currentRealTimeDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        sdfDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        formattedCurrentDate = sdfDate.format(currentRealTimeDate);
        timeTextView.setText(formattedCurrentDate);

        morningBtn.setOnClickListener(onNavToUserMealRecordMorningListener());
        afternoonBtn.setOnClickListener(onNavToUserMealRecordAfternoonListener());
        nightBtn.setOnClickListener(onNavToUserMealRecordNightListener());

        // init view
        this.view = view;
        alertDialogMessage = new StringBuilder();
    }

    public void initNotification()
    {
        // Build the Snackbar message
        StringBuilder messageBuilder = new StringBuilder();
        if (totalCalories > maxCalories)
        {
            messageBuilder.append("You have exceeded your daily calorie intake limit.\n");
            totalCalories = maxCalories;
            // display the counter
            caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                    totalCalories, maxCalories));
            caloriesCountText.setTextColor(Color.RED);
        }
        if (totalCholesterol > maxCholesterol)
        {
            messageBuilder.append("You have exceeded your daily cholesterol intake limit.\n");
            totalCholesterol = maxCholesterol;
            cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                    totalCholesterol, maxCalories));
            cholesterolCountText.setTextColor(Color.RED);
        }
        if (totalSugar > maxSugar)
        {
            messageBuilder.append("You have exceeded your daily sugar intake limit.\n");
            totalSugar = maxSugar;
            sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                    totalSugar, maxSugar));
            sugarCountText.setTextColor(Color.RED);
        }
        if (totalSalt > maxSalt)
        {
            messageBuilder.append("You have exceeded your daily sodium intake limit.\n");
            totalSalt = maxSalt;
            saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                    totalSalt, maxSalt));
            saltCountText.setTextColor(Color.RED);
        }

        // Display the Snackbar
        if (messageBuilder.length() > 0)
        {
            if (view != null)
            {
                Snackbar snackbar = Snackbar.make(view, messageBuilder.toString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else
            {
                Log.e(TAG, "view is null");
            }
        }
    }

    private void refreshUIDisplay()
    {
        // init them
        totalCalories = 0;
        totalCholesterol = 0;
        totalSalt = 0;
        totalSugar = 0;

        percentageCalories = 0;
        percentageCholesterol = 0;
        percentageSalt = 0;
        percentageSugar = 0;

        progressBarCalories.setProgress((int) percentageCalories);
        caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                percentageCalories));

        progressBarCholesterol.setProgress((int) percentageCholesterol);
        cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                percentageCholesterol));

        // fixme: recalculate sodium percentage
        progressBarSalt.setProgress((int) percentageSalt);
        saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                percentageSalt));

        progressBarSugar.setProgress((int) percentageSugar);
        sugarText.setText(String.format(Locale.ROOT, "%.1f%%",
                percentageSugar));

        // display the counter
        caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                totalCalories, maxCalories));
        caloriesCountText.setTextColor(Color.BLACK);

        cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                totalCholesterol, maxCholesterol));
        cholesterolCountText.setTextColor(Color.BLACK);

        sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                totalSugar, maxSugar));
        sugarCountText.setTextColor(Color.BLACK);

        saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                totalSalt, maxSalt));
        saltCountText.setTextColor(Color.BLACK);
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener onTotalNutritionValueListener()
    {
        return new ChildEventListener()
        {
            // todo: test this tmr pls!!!
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // Log.d(TAG, currentDate);
                NutritionMeal nutritionMeal1 = snapshot.getValue(NutritionMeal.class);
                if (nutritionMeal1 != null)
                {
                    // for time
                    currentDateMeal = nutritionMeal1.getDate();
                    formattedMealDate = sdfDate.format(currentDateMeal);
                    // Log.d(TAG, currentDateMeal.toString());
                    // fixme: must extract only the date and compare them.

                    // fixme: pls note that this realtime system check haven't tested yet.
                    // still find a way to test this.
                    if (formattedMealDate.equals(formattedCurrentDate))
                    {
                        /*
                        Log.d(TAG, "onChildAdded: yes");
                        Log.d(TAG, currentDateMeal.toString());
                        Log.d(TAG, currentRealTimeDate.toString());
                         */
                        mealCache.put(snapshot.getKey(), nutritionMeal);
                        // Log.d(TAG, "onChildAdded: " + meal1);
                        totalCalories += nutritionMeal1.getTotalCalories();
                        totalCholesterol += nutritionMeal1.getTotalCholesterol();
                        totalSugar += nutritionMeal1.getTotalSugar();
                        totalSalt += nutritionMeal1.getTotalSodium();

                        initNotification();
                    }
                    else
                    {
                        // ignore the data for the meal not the same date.
                        refreshUIDisplay();
                    }
                }

                // calculate percentage
                percentageCalories = (totalCalories / maxCalories) * 100;
                percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                percentageSalt = (totalSalt / maxSalt) * 100;
                percentageSugar = (totalSugar / maxSugar) * 100;


                // fixme: null pointer exception

                progressBarCalories.setProgress((int) percentageCalories);
                caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCalories));

                progressBarCholesterol.setProgress((int) percentageCholesterol);
                cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCholesterol));

                // fixme: recalculate sodium percentage
                progressBarSalt.setProgress((int) percentageSalt);
                saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSalt));

                progressBarSugar.setProgress((int) percentageSugar);
                sugarText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSugar));

                // display the counter
                caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                        totalCalories, maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalCholesterol, maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                        totalSugar, maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalSalt, maxSalt));

                // makeNotification();
                // todo: improve this system if over 1 days.
                /*
                    // reset the value whenever the nutrition value is changed.
                    totalCalories = 0;
                    totalCholesterol = 0;
                    totalSugar = 0;
                    totalSalt = 0;
                 */
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                NutritionMeal nutritionMeal1 = snapshot.getValue(NutritionMeal.class);
                if (nutritionMeal1 != null)
                {
                    NutritionMeal oldNutritionMeal = mealCache.get(snapshot.getKey());
                    if (oldNutritionMeal != null)
                    {
                        /*
                        Log.d(TAG, "onChildChanged: calories " + totalCalories);
                        Log.d(TAG, "onChildChanged: cholesterol " + totalCholesterol);
                        Log.d(TAG, "onChildChanged: sugar " + totalSugar);
                        Log.d(TAG, "onChildChanged: salt " + totalSalt);
                         */
                        if (formattedMealDate.equals(formattedCurrentDate))
                        {
                            /*
                            Log.d(TAG, "onChildAdded: yes");
                            Log.d(TAG, currentDateMeal.toString());
                            Log.d(TAG, currentRealTimeDate.toString());
                             */
                            // Log.d(TAG, "onChildAdded: " + meal1);
                            // Subtract the old values
                            totalCalories -= oldNutritionMeal.getTotalCalories();
                            totalCholesterol -= oldNutritionMeal.getTotalCholesterol();
                            totalSugar -= oldNutritionMeal.getTotalSugar();
                            totalSalt -= oldNutritionMeal.getTotalSodium();

                            // use a Handler to post UI updates on the main thread
                            /*
                            new Handler(Looper.getMainLooper()).post(() ->
                            {
                                // update UI elements here
                                refreshUIDisplay();
                            });
                             */
                        }
                        else
                        {
                            // ignore the data for the meal not the same date.
                            refreshUIDisplay();
                        }
                    }
                    // update the cache with the new meal
                    mealCache.put(snapshot.getKey(), nutritionMeal1);
                }

                // calculate percentage
                percentageCalories = (totalCalories / maxCalories) * 100;
                percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                percentageSalt = (totalSalt / maxSalt) * 100;
                percentageSugar = (totalSugar / maxSugar) * 100;


                // fixme: null pointer exception

                progressBarCalories.setProgress((int) percentageCalories);
                caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCalories));

                progressBarCholesterol.setProgress((int) percentageCholesterol);
                cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCholesterol));

                // fixme: recalculate sodium percentage
                progressBarSalt.setProgress((int) percentageSalt);
                saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSalt));

                progressBarSugar.setProgress((int) percentageSugar);
                sugarText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSugar));

                //notify();

                // display the counter
                caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                        totalCalories, maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalCholesterol, maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                        totalSugar, maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalSalt, maxSalt));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                NutritionMeal nutritionMeal1 = snapshot.getValue(NutritionMeal.class);
                if (nutritionMeal1 != null)
                {
                    NutritionMeal oldNutritionMeal = mealCache.get(snapshot.getKey());
                    if (oldNutritionMeal != null)
                    {
                        if (formattedMealDate.equals(formattedCurrentDate))
                        {
                            totalCalories -= oldNutritionMeal.getTotalCalories();
                            totalCholesterol -= oldNutritionMeal.getTotalCholesterol();
                            totalSugar -= oldNutritionMeal.getTotalSugar();
                            totalSalt -= oldNutritionMeal.getTotalSodium();
                        }
                    }
                    // remove the meal from the cache
                    mealCache.remove(snapshot.getKey());
                }

                // calculate percentage
                percentageCalories = (totalCalories / maxCalories) * 100;
                percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                percentageSalt = (totalSalt / maxSalt) * 100;
                percentageSugar = (totalSugar / maxSugar) * 100;

                // fixme: null pointer exception
                progressBarCalories.setProgress((int) percentageCalories);
                caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCalories));

                progressBarCholesterol.setProgress((int) percentageCholesterol);
                cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCholesterol));

                // fixme: recalculate sodium percentage
                progressBarSalt.setProgress((int) percentageSalt);
                saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSalt));

                progressBarSugar.setProgress((int) percentageSugar);
                sugarText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSugar));

                //notify();

                // display the counter
                caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                        totalCalories, maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalCholesterol, maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                        totalSugar, maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalSalt, maxSalt));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // Meal meal1 = snapshot.getValue(Meal.class);
                // do nothing here, just reupdate
                // calculate percentage

                percentageCalories = (totalCalories / maxCalories) * 100;
                percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                percentageSalt = (totalSalt / maxSalt) * 100;
                percentageSugar = (totalSugar / maxSugar) * 100;


                // fixme: null pointer exception

                progressBarCalories.setProgress((int) percentageCalories);
                caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCalories));

                progressBarCholesterol.setProgress((int) percentageCholesterol);
                cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCholesterol));

                // fixme: recalculate sodium percentage
                progressBarSalt.setProgress((int) percentageSalt);
                saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSalt));

                progressBarSugar.setProgress((int) percentageSugar);
                sugarText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSugar));

                //notify();

                // display the counter
                caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                        totalCalories, maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalCholesterol, maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                        totalSugar, maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                        totalSalt, maxSalt));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener onGenderHealthValueListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!isAdded())
                {
                    // Fragment is not currently added to its activity, so we don't perform any operations
                    return;
                }

                userProfile = snapshot.getValue(UserProfile.class);

                if (userProfile != null)
                {
                    gender = userProfile.getGender();
                    switch(gender)
                    {
                        // todo: set text max plus current nutrition value.
                        // todo: important here this should be a goal or something else?
                        case "Male":
                            maxCalories = 2200; // per calories
                            maxCholesterol = 300; // per mg
                            maxSugar = 36; // per grams
                            maxSalt = 2300; // per mg
                            // display default UI
                            caloriesCountText.setText(String.format(Locale.ROOT, "%d/%d kcal",
                                    0, (int) maxCalories));

                            cholesterolCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                                    0, (int) maxCholesterol));

                            sugarCountText.setText(String.format(Locale.ROOT, "%d/%d g",
                                    0, (int) maxSugar));

                            saltCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                                    0, (int) maxSalt));
                            break;
                        case "Female":
                            maxCalories = 1800;
                            maxCholesterol = 240;
                            maxSugar = 24;
                            maxSalt = 2300; // per mg, should be sodium
                            // display default UI
                            caloriesCountText.setText(String.format(Locale.ROOT, "%d/%d kcal",
                                    0, (int) maxCalories));

                            cholesterolCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                                    0, (int) maxCholesterol));

                            sugarCountText.setText(String.format(Locale.ROOT, "%d/%d g",
                                    0, (int) maxSugar));

                            saltCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                                    0, (int) maxSalt));
                            break;
                        default:
                            // wrong gender no default value.
                            Log.d(TAG, "Unknown gender: " + gender);
                            break;
                    }

                    // todo: for health
                    isDiabetes = userProfile.isDiabetes();
                    isHighBloodPressure = userProfile.isHighBloodPressure();
                    isHighCholesterol = userProfile.isHighCholesterol();

                    StringBuilder alertSnackBarMessage = new StringBuilder();

                    // todo: should warn the user if over
                    if (isDiabetes)
                    {
                        maxCalories *= 0.5; // minus 50%
                        alertSnackBarMessage.append("Diabetes detected. " +
                                        "Your calorie limit has been reduced to ")
                                .append((int) maxCalories)
                                .append(" calories to help manage your condition.\n");
                    }

                    if (isHighBloodPressure)
                    {
                        maxSalt *= 0.5; // minus 50%
                        alertSnackBarMessage.append("High blood pressure detected. " +
                                        "Your salt intake limit has been reduced to ")
                                .append((int) maxSalt)
                                .append(" mg to help manage your condition.\n");
                    }

                    if (isHighCholesterol)
                    {
                        maxCholesterol *= 0.5;
                        alertSnackBarMessage.append("High cholesterol detected. " +
                                        "Your cholesterol limit has been reduced to ")
                                .append((int) maxCholesterol)
                                .append(" mg to help manage your condition.\n");
                    }

                    // calculate percentage
                    percentageCalories = (totalCalories / maxCalories) * 100;
                    percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                    percentageSalt = (totalSalt / maxSalt) * 100;
                    percentageSugar = (totalSugar / maxSugar) * 100;


                    // fixme: null pointer exception

                    progressBarCalories.setProgress((int) percentageCalories);
                    caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                            percentageCalories));

                    progressBarCholesterol.setProgress((int) percentageCholesterol);
                    cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                            percentageCholesterol));

                    // fixme: recalculate sodium percentage
                    progressBarSalt.setProgress((int) percentageSalt);
                    saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                            percentageSalt));

                    progressBarSugar.setProgress((int) percentageSugar);
                    sugarText.setText(String.format(Locale.ROOT, "%.1f%%",
                            percentageSugar));

                    //notify();

                    // display the counter
                    caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f kcal",
                            totalCalories, maxCalories));

                    cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                            totalCholesterol, maxCholesterol));

                    sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f g",
                            totalSugar, maxSugar));

                    saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f mg",
                            totalSalt, maxSalt));

                    // todo: warn the user if the current nutrition value is bigger than maximum nutrition value.
                    if (totalCalories > maxCalories)
                    {
                        alertDialogMessage.append("You have exceeded your daily calorie intake limit.\n");
                        progressBarCalories.setBackgroundColor(Color.RED);
                    }

                    if (totalCholesterol > maxCholesterol)
                    {
                        alertDialogMessage.append("You have exceeded your daily cholesterol intake limit.\n");
                        progressBarCholesterol.setBackgroundColor(Color.RED);
                    }

                    if (totalSugar > maxSugar)
                    {
                        alertDialogMessage.append("You have exceeded your daily sugar intake limit.\n");
                        progressBarSugar.setBackgroundColor(Color.RED);
                    }

                    if (totalSalt > maxSalt)
                    {
                        alertDialogMessage.append("You have exceeded your daily sodium intake limit.\n");
                        progressBarSalt.setBackgroundColor(Color.RED);
                    }

                    // fixme: the error is here view is gone if we use snack bar.
                    if (alertDialogMessage.length() > 0)
                    {
                        if (alertDialogMessage.length() > 0)
                        {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), alertDialogMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                if (!isAdded())
                {
                    // Fragment is not currently added to its activity, so we don't perform any operations
                    return;
                }

                Log.d(TAG, "onCancelled: " + error);
            }
        };
    }

    // todo: our plan is to switch this page before logging the meal first and
    // todo: before the image recognition and nutrition analysis page. DO THIS TMR!

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordNightListener()
    {
        return v ->
        {
            // set the key from unique UID.
            nutritionMeal.setMorning(false);
            nutritionMeal.setAfternoon(false);
            nutritionMeal.setNight(true);

            // todo: option 1: make a global instance for meal object
            // todo: option 2: setValue() to Firebase realtime database in a temporary path, then
            //  retrieve it later in the next fragment

            bundleStore.putParcelable("meal", nutritionMeal);
            userLogMealNutritionAnalysisFragment.setArguments(bundleStore);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userLogMealNutritionAnalysisFragment)
                    .commit();
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordAfternoonListener()
    {
        return v ->
        {
            // set boolean value
            nutritionMeal.setMorning(false);
            nutritionMeal.setAfternoon(true);
            nutritionMeal.setNight(false);

            bundleStore.putParcelable("meal", nutritionMeal);

            userLogMealNutritionAnalysisFragment.setArguments(bundleStore);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userLogMealNutritionAnalysisFragment)
                    .commit();
        };
    }

    // TODO: for now it should go to meal record?
    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordMorningListener()
    {
        return v ->
        {
            // set boolean value
            nutritionMeal.setMorning(true);
            nutritionMeal.setAfternoon(false);
            nutritionMeal.setNight(false);

            bundleStore.putParcelable("meal", nutritionMeal);

            userLogMealNutritionAnalysisFragment.setArguments(bundleStore);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userLogMealNutritionAnalysisFragment)
                    .commit();
        };
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        // clear the reference to the view to avoid memory leaks
        this.view = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_log_meal, container, false);
    }
}