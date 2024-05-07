package com.example.myfoodchoice.UserFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelNutrition.NutritionMeal;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Locale;


public class UserHomeAlvinFragment extends Fragment
{
    // todo: we need to separate the part where log my meal and
    //  meal history should be separated.
    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meals";

    final static String TAG = "UserHomeAlvinFragment";

    String userID, gender;

    // TODO: declare UI component

    // AlertDialog morningDialog, afternoonDialog, nightDialog;

    TextView caloriesText, cholesterolText, sugarText, saltText;

    double totalCalories, totalCholesterol, totalSugar, totalSalt;

    double maxCalories, maxCholesterol, maxSugar, maxSalt;

    double percentageCalories, percentageCholesterol, percentageSugar, percentageSalt;

    boolean isDiabetes, isHighBloodPressure, isHighCholesterol;

    NutritionMeal nutritionMeal;

    ProgressBar progressBarCalories, progressBarCholesterol, progressBarSugar, progressBarSalt;

    // todo: declare button UI component
    Button logMyMealBtn, mealHistoryBtn;
    TextView caloriesCountText, cholesterolCountText, sugarCountText, saltCountText;
    Bundle bundleStore;

    UserLogMealNutritionAnalysisFragment userLogMealNutritionAnalysisFragment;
    private StringBuilder alertDialogMessage;

    View view;

    private final HashMap<String, NutritionMeal> mealCache = new HashMap<>();

    // todo: migrate the UI from log meal page to this home fragment.

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

        // todo: init button UI component
        /*
        logMyMealBtn = view.findViewById(R.id.logMealBtn);
        mealHistoryBtn = view.findViewById(R.id.mealHistoryBtn);
         */

        // logMyMealBtn.setOnClickListener(onLogMyMealListener());
        // mealHistoryBtn.setOnClickListener(onMealHistoryListener());

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

        // for text view
        caloriesCountText = view.findViewById(R.id.caloriesNumTextView);
        cholesterolCountText = view.findViewById(R.id.cholesterolNumTextView);
        sugarCountText = view.findViewById(R.id.sugarNumTextView);
        saltCountText = view.findViewById(R.id.sodiumNumTextView);

        // todo: init view and string builder
        this.view = view;
        alertDialogMessage = new StringBuilder();
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener onTotalNutritionValueListener()
    {
        return new ChildEventListener()
        {
            // todo: test this tmr pls!
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                NutritionMeal nutritionMeal1 = snapshot.getValue(NutritionMeal.class);
                if (nutritionMeal1 != null)
                {
                    mealCache.put(snapshot.getKey(), nutritionMeal);
                    // Log.d(TAG, "onChildAdded: " + meal1);
                    totalCalories += nutritionMeal1.getTotalCalories();
                    totalCholesterol += nutritionMeal1.getTotalCholesterol();
                    totalSugar += nutritionMeal1.getTotalSugar();
                    totalSalt += nutritionMeal1.getTotalSodium();
                }

                // calculate percentage
                percentageCalories = (totalCalories / maxCalories) * 100;
                percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                percentageSalt = (totalSalt / maxSalt) * 100;
                percentageSugar = (totalSugar / maxSugar) * 100;

                /*
                Log.d(TAG, "onChildAdded: " + percentageCalories);
                Log.d(TAG, "onChildAdded: " + percentageCholesterol);
                Log.d(TAG, "onChildAdded: " + percentageSalt);
                Log.d(TAG, "onChildAdded: " + percentageSugar);
                 */

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

                // todo: improve this system if over 7 days.

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
                    if (oldNutritionMeal != null) {
                        // Subtract the old values
                        totalCalories -= oldNutritionMeal.getTotalCalories();
                        totalCholesterol -= oldNutritionMeal.getTotalCholesterol();
                        totalSugar -= oldNutritionMeal.getTotalSugar();
                        totalSalt -= oldNutritionMeal.getTotalSodium();
                    }
                    // Update the cache with the new meal
                    mealCache.put(snapshot.getKey(), nutritionMeal1);
                    // Add the new values
                    totalCalories += nutritionMeal1.getTotalCalories();
                    totalCholesterol += nutritionMeal1.getTotalCholesterol();
                    totalSugar += nutritionMeal1.getTotalSugar();
                    totalSalt += nutritionMeal1.getTotalSodium();
                }

                // the bug is that we should reset the
                /*
                Log.d(TAG, "onChildChanged totalcalories: " + totalCalories);
                Log.d(TAG, "onChildChanged: totalcholesterol" + totalCholesterol);
                Log.d(TAG, "onChildChanged: totalsugar" + totalSugar);
                Log.d(TAG, "onChildChanged: totalsalt" + totalSalt);
                 */

                // calculate percentage
                percentageCalories = (totalCalories / maxCalories) * 100;
                percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                percentageSalt = (totalSalt / maxSalt) * 100;
                percentageSugar = (totalSugar / maxSugar) * 100;


                // fixme: null pointer exception
                progressBarCalories.setProgress((int) percentageCalories);
                if (percentageCalories == 0)
                {
                    progressBarCalories.setProgress(0);
                }
                caloriesText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCalories));

                progressBarCholesterol.setProgress((int) percentageCholesterol);
                if (percentageCholesterol == 0)
                {
                    progressBarCholesterol.setProgress(0);
                }
                cholesterolText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageCholesterol));

                // fixme: recalculate sodium percentage
                progressBarSalt.setProgress((int) percentageSalt);
                if (percentageSalt == 0)
                {
                    progressBarSalt.setProgress(0);
                }
                saltText.setText(String.format(Locale.ROOT, "%.1f%%",
                        percentageSalt));

                progressBarSugar.setProgress((int) percentageSugar);
                if (totalSugar == 0)
                {
                    progressBarSugar.setProgress(0);
                }
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
                        // Subtract the old values
                        totalCalories -= oldNutritionMeal.getTotalCalories();
                        totalCholesterol -= oldNutritionMeal.getTotalCholesterol();
                        totalSugar -= oldNutritionMeal.getTotalSugar();
                        totalSalt -= oldNutritionMeal.getTotalSodium();
                    }
                    // Remove the meal from the cache
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

                // reset the value whenever the nutrition value is changed.
                totalCalories = 0;
                totalCholesterol = 0;
                totalSugar = 0;
                totalSalt = 0;
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
                    if (gender != null)
                    {
                        if (gender.equals("Male"))
                        {
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
                        }
                        else if (gender.equals("Female"))
                        {
                            maxCalories = 1800;
                            maxCholesterol = 240;
                            maxSugar = 24;
                            maxSalt = 2300; // per mg, should be sodium
                            // display default UI
                            caloriesCountText.setText(String.format(Locale.ROOT, "%d/%d kcal",
                                    0, (int) maxCalories));
                        }
                    }

                    isDiabetes = userProfile.isDiabetes();
                    isHighBloodPressure = userProfile.isHighBloodPressure();
                    isHighCholesterol = userProfile.isHighCholesterol();
                }

                // todo: for health
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

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home_alvin, container, false);
    }
}