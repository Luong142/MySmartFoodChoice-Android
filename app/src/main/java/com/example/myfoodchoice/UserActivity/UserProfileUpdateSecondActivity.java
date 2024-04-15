package com.example.myfoodchoice.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myfoodchoice.AdapterSpinner.DietTypeAdapter;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class UserProfileUpdateSecondActivity extends AppCompatActivity
{
    // TODO: declare UI components
    // TODO: change of plan, we need to remove this part for only first is remain for lesser update.
    // TODO: this one is not used currently
    ImageView maleUpdateImage, femaleUpdateImage;

    Button updateBtn;

    ProgressBar progressBar;

    EditText weightUpdateProfile, heightUpdateProfile;

    Spinner dietTypeUpdateSpinner;

    // TODO: declare firebase
    DatabaseReference databaseReferenceUserProfile;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID, dietType, weightUpdateString, heightUpdateString, genderUpdateString;

    ArrayList<UserProfile> dietTypeArrayList;

    Intent intent;

    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update_second);

        // TODO: init firebase components
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile = firebaseDatabase.getReference("User Profile").child(userID);
        }
        // TODO: init UI components
        maleUpdateImage = findViewById(R.id.maleUpdateImage);
        femaleUpdateImage = findViewById(R.id.femaleUpdateImage);
        progressBar = findViewById(R.id.progressBar);
        weightUpdateProfile = findViewById(R.id.weightUpdateProfile);
        heightUpdateProfile = findViewById(R.id.heightUpdateProfile);
        updateBtn = findViewById(R.id.updateBtn);

        // set progress bar
        progressBar.setVisibility(ProgressBar.GONE);
        updateBtn.setVisibility(Button.VISIBLE);
        genderUpdateString = "";

        // for spinner
        initListDietType();
        dietTypeUpdateSpinner = findViewById(R.id.dietTypeUpdateSpinner);
        DietTypeAdapter dietTypeAdapter = new DietTypeAdapter(this, dietTypeArrayList);
        dietTypeUpdateSpinner.setAdapter(dietTypeAdapter);
        dietTypeUpdateSpinner.setOnItemSelectedListener(onItemSelectedDietTypeListener);

        // set onClick for gender image
        maleUpdateImage.setOnClickListener(onMaleClickListener());
        femaleUpdateImage.setOnClickListener(onFemaleClickListener());

        // for user profile that has been brought over from the first user profile activity.
        intent = getIntent();
        userProfile = intent.getParcelableExtra("userProfile");

        // set on click
        updateBtn.setOnClickListener(onUpdateListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpdateListener()
    {
        return v ->
        {
            // set progress bar
            progressBar.setVisibility(ProgressBar.VISIBLE);

            // init
            weightUpdateString = weightUpdateProfile.getText().toString();
            heightUpdateString = heightUpdateProfile.getText().toString();

            // validation
            if (genderUpdateString.isEmpty())
            {
                Toast.makeText(UserProfileUpdateSecondActivity.this,
                        "Please select your gender", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weightUpdateString.isEmpty())
            {
                weightUpdateProfile.setError("Please enter your weight.");
                return;
            }

            if (heightUpdateString.isEmpty())
            {
                heightUpdateProfile.setError("Please enter your height.");
                return;
            }

            // update user profile
            userProfile.setGender(genderUpdateString);
            userProfile.setDietType(dietType);
            userProfile.setWeight(weightUpdateString);
            userProfile.setHeight(heightUpdateString);
            Log.d("UserProfileUpdateSecondActivity", "userProfile: " + userProfile.getProfileImageUrl());

            // update the database
            updateUserProfileAndNavigate();
        };
    }

    private void updateUserProfileAndNavigate() // TODO: test this tmr and check it out with team.
    {
        // update here
        databaseReferenceUserProfile.setValue(userProfile).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    // update successful, navigate to the next activity
                    Toast.makeText(UserProfileUpdateSecondActivity.this,
                            "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserProfileUpdateSecondActivity.this,
                            UserMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    // update failed, show an error message
                    Toast.makeText(UserProfileUpdateSecondActivity.this,
                            "Failed to update profile", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.GONE);
                    updateBtn.setVisibility(Button.VISIBLE);
                }
            });
    }

    private final AdapterView.OnItemSelectedListener onItemSelectedDietTypeListener =
            new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id)
        {
            // the purpose is for spinner to select and apply the string dietType to define the user profile
            UserProfile userProfile1 = (UserProfile) parent.getItemAtPosition(position);
            dietType = userProfile1.getDietType();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    };

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onFemaleClickListener()
    {
        return v ->
        {
            maleUpdateImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_notfocused));
            femaleUpdateImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_focused));
            genderUpdateString = "Female";
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onMaleClickListener()
    {
        return v ->
        {
            maleUpdateImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_focused));
            femaleUpdateImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_notfocused));
            genderUpdateString = "Male";
        };
    }

    private void initListDietType()
    {
        dietTypeArrayList = new ArrayList<>();
        /*
         dietTypeArrayList.add(new UserProfile("Vegetarian", R.drawable.vegetarian));
        dietTypeArrayList.add(new UserProfile("Halal", R.drawable.halal));
        dietTypeArrayList.add(new UserProfile("High-Protein", R.drawable.protein));
        dietTypeArrayList.add(new UserProfile("Low-Carb", R.drawable.carb));
        dietTypeArrayList.add(new UserProfile("Low-Fat", R.drawable.fat));
        dietTypeArrayList.add(new UserProfile("Low-Sugar", R.drawable.sugar));
         */
    }
}