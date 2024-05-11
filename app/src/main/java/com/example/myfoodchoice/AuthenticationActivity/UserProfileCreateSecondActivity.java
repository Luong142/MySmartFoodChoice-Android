package com.example.myfoodchoice.AuthenticationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

public class UserProfileCreateSecondActivity extends AppCompatActivity
{
    // TODO: declare database
    // todo: health risks consider, diet type only has vegetarian, non-vegetarian.
    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile;

    final static String PATH_USERPROFILE = "Android User Profile";



    // TODO: declare UI component

    Intent intent, intentToHealthDeclarationActivity;

    Button nextBtn;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_create_second);

        // TODO: initialize database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_USERPROFILE).child(firebaseUser.getUid());
        userProfile = new UserProfile();
         // allergiesArrayList = new ArrayList<>();
        // TODO: init UI component
        /*
            initListAllergies();
            spinnerAllergies = findViewById(R.id.allergiesSpinner);
            AllergiesAdapter allergiesAdapter = new AllergiesAdapter(this, allergiesArrayList);
            spinnerAllergies.setAdapter(allergiesAdapter);
            spinnerAllergies.setOnItemSelectedListener(onItemSelectedAllergiesListener);
         */

        // for progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);

        // for user profile that has been brought over from the first user profile activity.
        intent = getIntent();
        userProfile = intent.getParcelableExtra("userProfile");
        // Log.d(TAG, "Checking user profile pls: " + userProfile);

        // button
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(onSignUpListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            // set progress bar on
            progressBar.setVisibility(ProgressBar.VISIBLE);
            nextBtn.setVisibility(Button.GONE);

            // for now we define it as a string.


            // TODO: create user profile through this and add it inside of the firebase
            // TODO: do this tmr
            //databaseReferenceUserProfile.setValue(userProfile)
                    //.addOnCompleteListener(onSignUpCompleteListener());

            intentToHealthDeclarationActivity = new Intent(UserProfileCreateSecondActivity.this,
                    UserProfileHealthDeclarationActivity.class);
            intentToHealthDeclarationActivity.putExtra("userProfile", userProfile);
            startActivity(intentToHealthDeclarationActivity);
            finish(); // to close this page.
        };
    }
}