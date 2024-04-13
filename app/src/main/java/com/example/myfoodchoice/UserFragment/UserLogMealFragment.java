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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelMeal.Meal;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
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

import java.util.Locale;


public class UserLogMealFragment extends Fragment
{
    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID, gender;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meals";

    final static String TAG = "UserLogMealFragment";

    // TODO: declare UI component
    TextView caloriesText, cholesterolText, sugarText, saltText;

    double totalCalories, totalCholesterol, totalSugar, totalSalt;

    double maxCalories, maxCholesterol, maxSugar, maxSalt;

    double percentageCalories, percentageCholesterol, percentageSugar, percentageSalt;

    boolean isDiabetes, isHighBloodPressure, isHighCholesterol;

    Meal meal;

    ProgressBar progressBarCalories, progressBarCholesterol, progressBarSugar, progressBarSalt;
    // fixme: apply this pie chart instead of progress bar.

    Button morningBtn, afternoonBtn, nightBtn;

    TextView caloriesCountText, cholesterolCountText, sugarCountText, saltCountText, genderText;

    Bundle bundleStore;

    UserLogMealNutritionAnalysisFragment userLogMealNutritionAnalysisFragment;

    View view;

    StringBuilder alertDialogMessage;

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
        meal = new Meal();
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

        // TODO: init UI components
        morningBtn = view.findViewById(R.id.morningButton);
        afternoonBtn = view.findViewById(R.id.afternoonButton);
        nightBtn = view.findViewById(R.id.nightButton);

        // for text view
        caloriesCountText = view.findViewById(R.id.caloriesNumTextView);
        cholesterolCountText = view.findViewById(R.id.cholesterolNumTextView);
        sugarCountText = view.findViewById(R.id.sugarNumTextView);
        saltCountText = view.findViewById(R.id.sodiumNumTextView);
        genderText = view.findViewById(R.id.genderTextView);

        morningBtn.setOnClickListener(onNavToUserMealRecordMorningListener());
        afternoonBtn.setOnClickListener(onNavToUserMealRecordAfternoonListener());
        nightBtn.setOnClickListener(onNavToUserMealRecordNightListener());

        // init view
        this.view = view;
        alertDialogMessage = new StringBuilder();
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
                Meal meal1 = snapshot.getValue(Meal.class);
                if (meal1 != null)
                {
                    // Log.d(TAG, "onChildAdded: " + meal1);
                    totalCalories += meal1.getTotalCalories();
                    totalCholesterol += meal1.getTotalCholesterol();
                    totalSugar += meal1.getTotalSugar();
                    totalSalt += meal1.getTotalSodium();
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
                caloriesCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f",
                        totalCalories, maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f",
                        totalCholesterol, maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f",
                        totalSugar, maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%.1f/%.1f",
                        totalSalt, maxSalt));

                // todo: warn the user if the current nutrition value is bigger than maximum nutrition value.

                if (totalCalories > maxCalories)
                {
                    alertDialogMessage.append("You have exceeded your daily calorie intake limit. ");
                    progressBarCalories.setBackgroundColor(Color.RED);
                }

                if (totalCholesterol > maxCholesterol)
                {
                    alertDialogMessage.append("You have exceeded your daily cholesterol intake limit. ");
                    progressBarCholesterol.setBackgroundColor(Color.RED);
                }

                if (totalSugar > maxSugar)
                {
                    alertDialogMessage.append("You have exceeded your daily sugar intake limit. ");
                    progressBarSugar.setBackgroundColor(Color.RED);
                }

                if (totalSalt > maxSalt)
                {
                    alertDialogMessage.append("You have exceeded your daily sodium intake limit. ");
                    progressBarSalt.setBackgroundColor(Color.RED);
                }

                if (alertDialogMessage.length() > 0)
                {
                    Snackbar.make(view, alertDialogMessage.toString(), Snackbar.LENGTH_LONG)
                            .setAction("OK", v ->
                            {
                                // do nothing
                            })
                            .show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                Meal meal1 = snapshot.getValue(Meal.class);
                if (meal1 != null)
                {
                    // Log.d(TAG, "onChildAdded: " + meal1);
                    totalCalories += meal1.getTotalCalories();
                    totalCholesterol += meal1.getTotalCholesterol();
                    totalSugar += meal1.getTotalSugar();
                    totalSalt += meal1.getTotalSodium();
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

                // display the counter for calories
                caloriesCountText.setText(String.format(Locale.ROOT, "%d/%d kcal",
                        (int) totalCalories, (int) maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                        (int) totalCholesterol, (int) maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%d/%d g",
                        (int) totalSugar, (int) maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                        (int) totalSalt, (int) maxSalt));

                if (totalCalories > maxCalories)
                {
                    alertDialogMessage.append("You have exceeded your daily calorie intake limit. ");
                    progressBarCalories.setBackgroundColor(Color.RED);
                }

                if (totalCholesterol > maxCholesterol)
                {
                    alertDialogMessage.append("You have exceeded your daily cholesterol intake limit. ");
                    progressBarCholesterol.setBackgroundColor(Color.RED);
                }

                if (totalSugar > maxSugar)
                {
                    alertDialogMessage.append("You have exceeded your daily sugar intake limit. ");
                    progressBarSugar.setBackgroundColor(Color.RED);
                }

                if (totalSalt > maxSalt)
                {
                    alertDialogMessage.append("You have exceeded your daily sodium intake limit. ");
                    progressBarSalt.setBackgroundColor(Color.RED);
                }

                if (alertDialogMessage.length() > 0)
                {
                    Snackbar.make(view, alertDialogMessage.toString(), Snackbar.LENGTH_LONG)
                            .setAction("OK", v ->
                            {
                                // do nothing
                            })
                            .show();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                Meal meal1 = snapshot.getValue(Meal.class);
                if (meal1 != null)
                {
                    // Log.d(TAG, "onChildAdded: " + meal1);
                    totalCalories += meal1.getTotalCalories();
                    totalCholesterol += meal1.getTotalCholesterol();
                    totalSugar += meal1.getTotalSugar();
                    totalSalt += meal1.getTotalSodium();
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

                // display the counter for calories
                caloriesCountText.setText(String.format(Locale.ROOT, "%d/%d kcal",
                        (int) totalCalories, (int) maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                        (int) totalCholesterol, (int) maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%d/%d g",
                        (int) totalSugar, (int) maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                        (int) totalSalt, (int) maxSalt));

                if (totalCalories > maxCalories)
                {
                    alertDialogMessage.append("You have exceeded your daily calorie intake limit. ");
                    progressBarCalories.setBackgroundColor(Color.RED);
                }

                if (totalCholesterol > maxCholesterol)
                {
                    alertDialogMessage.append("You have exceeded your daily cholesterol intake limit. ");
                    progressBarCholesterol.setBackgroundColor(Color.RED);
                }

                if (totalSugar > maxSugar)
                {
                    alertDialogMessage.append("You have exceeded your daily sugar intake limit. ");
                    progressBarSugar.setBackgroundColor(Color.RED);
                }

                if (totalSalt > maxSalt)
                {
                    alertDialogMessage.append("You have exceeded your daily sodium intake limit. ");
                    progressBarSalt.setBackgroundColor(Color.RED);
                }

                if (alertDialogMessage.length() > 0)
                {
                    Snackbar.make(view, alertDialogMessage.toString(), Snackbar.LENGTH_LONG)
                            .setAction("OK", v ->
                            {
                                // do nothing
                            })
                            .show();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                Meal meal1 = snapshot.getValue(Meal.class);
                if (meal1 != null)
                {
                    // Log.d(TAG, "onChildAdded: " + meal1);
                    totalCalories += meal1.getTotalCalories();
                    totalCholesterol += meal1.getTotalCholesterol();
                    totalSugar += meal1.getTotalSugar();
                    totalSalt += meal1.getTotalSodium();
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

                // display the counter for calories
                caloriesCountText.setText(String.format(Locale.ROOT, "%d/%d kcal",
                        (int) totalCalories, (int) maxCalories));

                cholesterolCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                        (int) totalCholesterol, (int) maxCholesterol));

                sugarCountText.setText(String.format(Locale.ROOT, "%d/%d g",
                        (int) totalSugar, (int) maxSugar));

                saltCountText.setText(String.format(Locale.ROOT, "%d/%d mg",
                        (int) totalSalt, (int) maxSalt));
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

                    // display gender
                    genderText.setText(String.format(Locale.ROOT, "Gender: %s", gender));

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

                    // fixme: later we can change the message as it is too long snack bar to show.
                    if (alertSnackBarMessage.length() > 0)
                    {
                        if (view != null)
                        {
                            /*
                            // It's safe to use the view variable here
                            Snackbar.make(view, alertSnackBarMessage.toString(), Snackbar.LENGTH_LONG)
                                    .setAction("OK", v ->
                                    {
                                        // do nothing
                                    })
                                    .show();

                             */
                            // todo: now replace with AlertDialog
                            // Create an AlertDialog Builder
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                            // Set the message to display
                            builder.setMessage(alertSnackBarMessage.toString());

                            // Set the positive button
                            builder.setPositiveButton("OK", (dialog, which) ->
                            {
                                dialog.dismiss();
                            });

                            // Create and show the AlertDialog
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
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
            meal.setMorning(false);
            meal.setAfternoon(false);
            meal.setNight(true);

            // todo: option 1: make a global instance for meal object
            // todo: option 2: setValue() to Firebase realtime database in a temporary path, then
            //  retrieve it later in the next fragment

            bundleStore.putParcelable("meal", meal);

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
            meal.setMorning(false);
            meal.setAfternoon(true);
            meal.setNight(false);

            bundleStore.putParcelable("meal", meal);

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
            meal.setMorning(true);
            meal.setAfternoon(false);
            meal.setNight(false);

            bundleStore.putParcelable("meal", meal);

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