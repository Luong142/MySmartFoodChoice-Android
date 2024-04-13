package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelMeal.Meal;
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

import java.util.Locale;


public class UserHomeAlvinFragment extends Fragment
{
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

    Meal meal;

    ProgressBar progressBarCalories, progressBarCholesterol, progressBarSugar, progressBarSalt;

    // todo: declare button UI component
    Button logMyMealBtn, mealHistoryBtn;
    TextView caloriesCountText, cholesterolCountText, sugarCountText, saltCountText;
    Bundle bundleStore;

    UserLogMealNutritionAnalysisFragment userLogMealNutritionAnalysisFragment;

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
        logMyMealBtn = view.findViewById(R.id.logMealBtn);
        mealHistoryBtn = view.findViewById(R.id.mealHistoryBtn);

        logMyMealBtn.setOnClickListener(onLogMyMealListener());
        mealHistoryBtn.setOnClickListener(onMealHistoryListener());

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

        // for text view
        caloriesCountText = view.findViewById(R.id.caloriesNumTextView);
        cholesterolCountText = view.findViewById(R.id.cholesterolNumTextView);
        sugarCountText = view.findViewById(R.id.sugarNumTextView);
        saltCountText = view.findViewById(R.id.sodiumNumTextView);
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

                    // todo: should warn the user if over
                    if (isDiabetes)
                    {
                        maxCalories *= 0.5; // minus 50%
                    }

                    if (isHighBloodPressure)
                    {
                        maxSalt *= 0.5; // minus 50%

                    }

                    if (isHighCholesterol)
                    {
                        maxCholesterol *= 0.5;
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
    @Contract(pure = true)
    private View.OnClickListener onMealHistoryListener()
    {
        return v ->
        {
            UserMealHistoryFragment userMealHistoryFragment = new UserMealHistoryFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userMealHistoryFragment)
                    .commit();
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onLogMyMealListener()
    {
        return v ->
        {
            UserLogMealFragment userLogMealFragment = new UserLogMealFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userLogMealFragment)
                    .commit();
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