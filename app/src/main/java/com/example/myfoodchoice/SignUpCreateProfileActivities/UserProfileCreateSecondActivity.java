package com.example.myfoodchoice.SignUpCreateProfileActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.AdapterSpinner.DietTypeAdapter;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class UserProfileCreateSecondActivity extends AppCompatActivity
{
    // TODO: declare database
    // todo: health risks consider, diet type only has vegetarian, non-vegetarian.
    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile;

    final static String PATH_USERPROFILE = "User Profile";

    ArrayList<UserProfile> dietTypeArrayList;

    String dietType;

    EditText editIntHeight, editIntWeight;

    // TODO: declare UI component
    Spinner spinnerDietType;

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
        dietTypeArrayList = new ArrayList<>();
         // allergiesArrayList = new ArrayList<>();

        // TODO: init UI component
        initListDietType();
        spinnerDietType = findViewById(R.id.dietTypeSpinner);
        DietTypeAdapter dietTypeAdapter = new DietTypeAdapter(this, dietTypeArrayList);
        spinnerDietType.setAdapter(dietTypeAdapter);
        spinnerDietType.setOnItemSelectedListener(onItemSelectedDietTypeListener);

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

        // for height and weight
        editIntHeight = findViewById(R.id.heightProfile);
        editIntWeight = findViewById(R.id.weightProfile);

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
            nextBtn.setVisibility(Button.GONE);

            weight = Integer.parseInt(editIntWeight.getText().toString());
            height = Integer.parseInt(editIntHeight.getText().toString());

            // for now we define it as a string.
            userProfile.setWeight(String.valueOf(weight));
            userProfile.setHeight(String.valueOf(height));
            userProfile.setDietType(dietType);

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

    private final AdapterView.OnItemSelectedListener onItemSelectedDietTypeListener =
            new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id)
                {
                    // the purpose is for spinner to select and apply the string dietType to define the user profile
                    UserProfile userProfile1 = (UserProfile) parent.getItemAtPosition(position);
                    dietType = userProfile1.getDietType();
                    nextBtn.setEnabled(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                    // show message
                    Toast.makeText(getApplicationContext(), "Please select a diet type.", Toast.LENGTH_SHORT).show();
                    nextBtn.setEnabled(false);
                }
            };

    private void initListDietType()
    {
        // FIXME: edamam can't be used.
        dietTypeArrayList.add(new UserProfile("Vegetarian", R.drawable.vege));
        dietTypeArrayList.add(new UserProfile("Non-Vegetarian", R.drawable.non_vege));
    }
}