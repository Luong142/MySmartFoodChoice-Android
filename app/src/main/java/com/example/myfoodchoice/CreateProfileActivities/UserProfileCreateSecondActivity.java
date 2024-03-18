package com.example.myfoodchoice.CreateProfileActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.Adapter.DietTypeAdapter;
import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;

public class UserProfileCreateSecondActivity extends AppCompatActivity
{
    // TODO: declare database
    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile;

    final static String TAG = "UserProfileCreateSecondActivity";

    final static String LABEL = "User Profile";

    ArrayList<UserProfile> dietTypeArrayList;

    String dietType;

    EditText editIntHeight, editIntWeight;

    // TODO: declare UI component
    Spinner spinnerDietType;

    Intent intent, intentToLoginActivity;

    Button signUpBtn;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_create_second);

        // TODO: initialize database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferenceUserProfile = firebaseDatabase.getReference(LABEL).child(firebaseUser.getUid());
        userProfile = new UserProfile();
        dietTypeArrayList = new ArrayList<>();

        // TODO: init UI component
        initListDietType();
        spinnerDietType = findViewById(R.id.dietTypeSpinner);
        DietTypeAdapter dietTypeAdapter = new DietTypeAdapter(this, dietTypeArrayList);
        spinnerDietType.setAdapter(dietTypeAdapter);
        spinnerDietType.setOnItemSelectedListener(onItemSelectedDietTypeListener);

        // for progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);

        // for height and weight
        editIntHeight = findViewById(R.id.heightProfile);
        editIntWeight = findViewById(R.id.weightProfile);

        // for user profile that has been brought over from the first user profile activity.
        intent = getIntent();
        userProfile = intent.getParcelableExtra("userProfile");
        // Log.d(TAG, "Checking user profile pls: " + userProfile);

        // button
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(onSignUpListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            // for height, centimeter unit.
            if (TextUtils.isEmpty(editIntHeight.getText().toString()))
            {
                editIntHeight.setError("Please enter your height");
                editIntHeight.requestFocus();
                return;
            }

            int height = Integer.parseInt(editIntHeight.getText().toString());

            if (height < 50 || height > 250)
            {
                editIntHeight.setError("Invalid height value cm");
                editIntHeight.requestFocus();
                return;
            }

            // for weight, kg unit
            if (TextUtils.isEmpty(editIntWeight.getText().toString()))
            {
                editIntWeight.setError("Please enter your weight");
                editIntWeight.requestFocus();
                return;
            }

            int weight = Integer.parseInt(editIntWeight.getText().toString());

            if (weight < 20 || weight > 300)
            {
                editIntWeight.setError("Invalid weight value kg");
                editIntWeight.requestFocus();
                return;
            }

            // set progress bar on
            progressBar.setVisibility(ProgressBar.VISIBLE);
            signUpBtn.setVisibility(Button.GONE);

            weight = Integer.parseInt(editIntWeight.getText().toString());
            height = Integer.parseInt(editIntHeight.getText().toString());

            // for now we define it as a string.
            userProfile.setWeight(String.valueOf(weight));
            userProfile.setHeight(String.valueOf(height));
            userProfile.setDietType(dietType);

            /*
            Log.d(TAG, "onSignUpListener: " + userProfile);
            Log.d(TAG, "onSignUpListener: " + firebaseUser.getUid()); // FIXME: for debug purpose
            Log.d(TAG, "onSignUpListener: " + firebaseUser.getDisplayName()); // FIXME: for debug purpose
            Log.d(TAG, "onSignUpListener: " + firebaseUser.getEmail()); // FIXME: for debug purpose
            Log.d(TAG, "onSignUpListener: " + firebaseUser.getPhotoUrl()); // FIXME: for debug purpose
            Log.d(TAG, "onSignUpListener: " + firebaseUser.getProviderId()); // FIXME: for debug purpose

             */

            // TODO: create user profile through this and add it inside of the firebase
            // TODO: do this tmr
            databaseReferenceUserProfile.setValue(userProfile)
                    .addOnCompleteListener(onSignUpCompleteListener());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onSignUpCompleteListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onSignUpCompleteListener: " + task); // FIXME: for debug purpose
                Toast.makeText(UserProfileCreateSecondActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                intentToLoginActivity = new Intent(UserProfileCreateSecondActivity.this,
                        LoginActivity.class);
                intentToLoginActivity.putExtra("userProfile", userProfile);
                startActivity(intentToLoginActivity);
                finish(); // to close this page.
            }
            else
            {
                progressBar.setVisibility(ProgressBar.GONE);
                signUpBtn.setVisibility(Button.VISIBLE); // to show the button again.
                Log.d(TAG, "onSignUpFailedListener: " +
                        Objects.requireNonNull(task.getException()).getMessage()); // FIXME: for debug purpose
                Toast.makeText(UserProfileCreateSecondActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        };
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

    private void initListDietType()
    {
        dietTypeArrayList.add(new UserProfile("Vegetarian", R.drawable.vege));
        dietTypeArrayList.add(new UserProfile("Non-Vegetarian", R.drawable.nonvege));
    }
}