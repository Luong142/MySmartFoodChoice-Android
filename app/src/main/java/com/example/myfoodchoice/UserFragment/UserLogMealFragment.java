package com.example.myfoodchoice.UserFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.ModelMeal.Meal;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;


public class UserLogMealFragment extends Fragment
{
    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake,
            databaseReferenceDailyFoodIntakeChild,
            databaseReferenceAccount;

    // todo: update the UI components
    //ProgressBar progressBarCalories, progressBarCholesterol, progressBarSugar, progressBarSalt;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID, gender, accountType;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meal";

    final static String PATH_ACCOUNT = "Registered Accounts";

    final static String TAG = "UserLogMealFragment";

    // TODO: declare UI component

    AlertDialog morningDialog, afternoonDialog, nightDialog;

    TextView caloriesText, cholesterolText, sugarText, saltText;

    Double totalCalories, totalCholesterol, totalSugar, totalSalt;

    boolean isMorning, isAfternoon, isNight;

    Meal meal;

    FoodItem dishes;

    // TODO: declare UI component
    Button morningBtn, afternoonBtn, nightBtn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // init boolean value
        isMorning = false;
        isAfternoon = false;
        isNight = false;

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

            databaseReferenceAccount = firebaseDatabase.getReference(PATH_ACCOUNT).child(userID);
            databaseReferenceAccount.addValueEventListener(onAccountTypeListener());
        }

        // todo: init objects
        userProfile = new UserProfile();
        dishes = new FoodItem();

        totalCalories = 0.0;
        totalCholesterol = 0.0;
        totalSugar = 0.0;
        totalSalt = 0.0;

        // TODO: init UI components
        morningBtn = view.findViewById(R.id.morningButton);
        afternoonBtn = view.findViewById(R.id.afternoonButton);
        nightBtn = view.findViewById(R.id.nightButton);

        morningBtn.setOnClickListener(onNavToUserMealRecordMorningListener());
        afternoonBtn.setOnClickListener(onNavToUserMealRecordAfternoonListener());
        nightBtn.setOnClickListener(onNavToUserMealRecordNightListener());
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener onAccountTypeListener()
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
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
            isMorning = true;
            UserMainMenuActivity activity = (UserMainMenuActivity) requireActivity();
            activity.navigateToFragment(new UserSearchFoodFragment());
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordAfternoonListener()
    {
        return v ->
        {
            isAfternoon = true;
            UserMainMenuActivity activity = (UserMainMenuActivity) requireActivity();
            activity.navigateToFragment(new UserSearchFoodFragment());
        };
    }

    // TODO: for now it should go to meal record?
    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordMorningListener()
    {
        return v ->
        {
            isNight = true;
            UserMainMenuActivity activity = (UserMainMenuActivity) requireActivity();
            activity.navigateToFragment(new UserSearchFoodFragment());
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_log_meal, container, false);
    }
}