package com.example.myfoodchoice.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.GuestActivity.GuestMainMenuActivity;
import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.ModelMeal.Meal;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.Locale;

public class UserLogMealActivity extends AppCompatActivity
{
    // todo: declare firebase
    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake,
            databaseReferenceDailyFoodIntakeChild,
            databaseReferenceAccount;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID, gender, accountType;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meal";

    final static String PATH_ACCOUNT = "Registered Accounts";

    final static String TAG = "UserLogMealActivity";

    // TODO: declare UI component
    Button morningBtn, afternoonBtn, nightBtn;

    AlertDialog morningDialog, afternoonDialog, nightDialog;

    TextView caloriesText, cholesterolText, sugarText, saltText;

    Double totalCalories, totalCholesterol, totalSugar, totalSalt;

    boolean isMorning, isAfternoon, isNight;

    Meal meal;

    FoodItem dishes;

    Intent intent, intentNavToBothMainMenu; // based on account type

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_meal);

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
        intent = getIntent();

        totalCalories = 0.0;
        totalCholesterol = 0.0;
        totalSugar = 0.0;
        totalSalt = 0.0;

        if (intent != null)
        {
            // todo: meal here.
            meal = intent.getParcelableExtra("meal");
            if (meal != null)
            {
                Log.d(TAG, "onCreate: " + meal.getDishes());
                //Log.d(TAG, "onCreate: " + meal.getDishes().size());
                dishes = meal.getDishes();

                // get individual nutrition attributes.
                for (FoodItem.Item dish : dishes.getItems())
                {
                    totalCalories += dish.getCalories();
                    totalCholesterol += dish.getCholesterol_mg();
                    totalSugar += dish.getSugar_g();
                    totalSalt += dish.getSodium_mg();
                }
            }

            Log.d(TAG, "onCreate: " + totalCalories);
            Log.d(TAG, "onCreate: " + totalCholesterol);
            Log.d(TAG, "onCreate: " + totalSugar);
            Log.d(TAG, "onCreate: " + totalSalt);
        }

        // todo: init this part UI
        caloriesText = findViewById(R.id.caloriesTextView);
        cholesterolText = findViewById(R.id.cholesterolTextView);
        sugarText = findViewById(R.id.sugarTextView);
        saltText = findViewById(R.id.sodiumTextView);

        // todo: set the text based on the nutrition value

        caloriesText.setText(String.format(Locale.ROOT,
                "Total Calories: %.02f", totalCalories) + " kcal");
        cholesterolText.setText(String.format(Locale.ROOT,
                "Total Cholesterol: %.02f", totalCholesterol) + " mg");
        sugarText.setText(String.format(Locale.ROOT,
                "Total Sugar: %.02f", totalSugar) + " g");
        saltText.setText(String.format(Locale.ROOT,
                "Total Sodium: %.02f", totalSalt) + " mg");

        // TODO: init UI components
        morningBtn = findViewById(R.id.morningButton);
        afternoonBtn = findViewById(R.id.afternoonButton);
        nightBtn = findViewById(R.id.nightButton);

        morningBtn.setOnClickListener(onNavToUserMealRecordMorningListener());
        afternoonBtn.setOnClickListener(onNavToUserMealRecordAfternoonListener());
        nightBtn.setOnClickListener(onNavToUserMealRecordNightListener());
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener onAccountTypeListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Account account = snapshot.getValue(Account.class);
                if (account != null)
                {
                    // the purpose of getting account type is to return the user to either guest or user main menu.
                    accountType = account.getAccountType();
                }
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
    private View.OnClickListener onNavToUserMealRecordNightListener()
    {
        return v ->
        {
            isMorning = true;
            meal.setMorning(isMorning);
            meal.setAfternoon(isAfternoon);
            meal.setNight(isNight);

            // init the child and push and get the key
            databaseReferenceDailyFoodIntakeChild = databaseReferenceDailyFoodIntake.push();
            meal.setKey(databaseReferenceDailyFoodIntakeChild.getKey());

            databaseReferenceDailyFoodIntakeChild.setValue(meal).addOnCompleteListener(onNightMealCompleteListener());
        };
    }

    @NonNull
    @Contract(" -> new")
    private OnCompleteListener<Void> onNightMealCompleteListener()
    {
        return task ->
        {
            if (!task.isSuccessful())
            {
                Log.d(TAG, "onCancelled: " + task.getException());
                return;
            }

            // todo: need to test this
            if (accountType.equals("Guest"))
            {
                intentNavToBothMainMenu = new Intent(UserLogMealActivity.this,
                        GuestMainMenuActivity.class);
            }
            else
            {
                intentNavToBothMainMenu = new Intent(UserLogMealActivity.this,
                        UserMainMenuActivity.class);
            }
            startActivity(intentNavToBothMainMenu);
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordAfternoonListener()
    {
        return v ->
        {
            isAfternoon = true;
            meal.setAfternoon(isAfternoon);
            meal.setNight(isNight);
            meal.setMorning(isMorning);

            // init the child and push and get the key
            databaseReferenceDailyFoodIntakeChild = databaseReferenceDailyFoodIntake.push();
            meal.setKey(databaseReferenceDailyFoodIntakeChild.getKey());

            databaseReferenceDailyFoodIntakeChild.setValue(meal).addOnCompleteListener(onAfternoonMealCompleteListener());

        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onAfternoonMealCompleteListener()
    {
        return task ->
        {
            if (!task.isSuccessful())
            {
                Log.d(TAG, "onCancelled: " + task.getException());
                return;
            }

            // todo: need to test this
            if (accountType.equals("Guest"))
            {
                intentNavToBothMainMenu = new Intent(UserLogMealActivity.this,
                        GuestMainMenuActivity.class);
            }
            else
            {
                intentNavToBothMainMenu = new Intent(UserLogMealActivity.this,
                        UserMainMenuActivity.class);
            }
            startActivity(intentNavToBothMainMenu);
            finish();
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
            meal.setNight(isNight);
            meal.setMorning(isMorning);
            meal.setAfternoon(isAfternoon);

            // init the child and push and get the key
            databaseReferenceDailyFoodIntakeChild = databaseReferenceDailyFoodIntake.push();
            meal.setKey(databaseReferenceDailyFoodIntakeChild.getKey());

            databaseReferenceDailyFoodIntakeChild.setValue(meal).addOnCompleteListener(onMorningMealCompleteListener());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onMorningMealCompleteListener()
    {
        return task ->
        {
            if (!task.isSuccessful())
            {
                Log.d(TAG, "onCancelled: " + task.getException());
                return;
            }

            // todo: need to test this
            if (accountType.equals("Guest"))
            {
                intentNavToBothMainMenu = new Intent(UserLogMealActivity.this,
                        GuestMainMenuActivity.class);
            }
            else
            {
                intentNavToBothMainMenu = new Intent(UserLogMealActivity.this,
                        UserMainMenuActivity.class);
            }
            startActivity(intentNavToBothMainMenu);
            finish();
        };
    }
}