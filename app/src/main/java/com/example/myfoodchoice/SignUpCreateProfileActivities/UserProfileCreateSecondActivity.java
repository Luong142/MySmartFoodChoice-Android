package com.example.myfoodchoice.SignUpCreateProfileActivities;

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

import com.example.myfoodchoice.Adapter.AllergiesAdapter;
import com.example.myfoodchoice.Adapter.DietTypeAdapter;
import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.ModelAdapter.Allergies;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
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
    // todo: health risks consider, diet type only has vegetarian, non-vegetarian.
    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile;

    final static String TAG = "UserProfileCreateSecondActivity";

    final static String LABEL = "User Profile";

    ArrayList<UserProfile> dietTypeArrayList;

    ArrayList<Allergies> allergiesArrayList;

    String dietType, allergies;

    EditText editIntHeight, editIntWeight;

    // TODO: declare UI component
    Spinner spinnerDietType, spinnerAllergies;

    Intent intent, intentToLoginActivity;

    Button signUpBtn;

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
        databaseReferenceUserProfile = firebaseDatabase.getReference(LABEL).child(firebaseUser.getUid());
        userProfile = new UserProfile();
        dietTypeArrayList = new ArrayList<>();
        allergiesArrayList = new ArrayList<>();

        // TODO: init UI component
        initListDietType();
        spinnerDietType = findViewById(R.id.dietTypeSpinner);
        DietTypeAdapter dietTypeAdapter = new DietTypeAdapter(this, dietTypeArrayList);
        spinnerDietType.setAdapter(dietTypeAdapter);
        spinnerDietType.setOnItemSelectedListener(onItemSelectedDietTypeListener);

        // TODO: init UI component
        initListAllergies();
        spinnerAllergies = findViewById(R.id.allergiesSpinner);
        AllergiesAdapter allergiesAdapter = new AllergiesAdapter(this, allergiesArrayList);
        spinnerAllergies.setAdapter(allergiesAdapter);
        spinnerAllergies.setOnItemSelectedListener(onItemSelectedAllergiesListener);

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
            userProfile.setAllergies(allergies);

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

    private final AdapterView.OnItemSelectedListener onItemSelectedAllergiesListener
            = new AdapterView.OnItemSelectedListener ()
    {
        @Override
        public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id)
        {
            Allergies allergies1 = (Allergies) parent.getItemAtPosition(position);
            allergies = allergies1.getName();
            signUpBtn.setEnabled(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
            // show message
            Toast.makeText(getApplicationContext(), "Please select allergy.", Toast.LENGTH_SHORT).show();
            signUpBtn.setEnabled(false);
        }
    };

    private final AdapterView.OnItemSelectedListener onItemSelectedDietTypeListener =
            new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id)
        {
            // the purpose is for spinner to select and apply the string dietType to define the user profile
            UserProfile userProfile1 = (UserProfile) parent.getItemAtPosition(position);
            dietType = userProfile1.getDietType();
            signUpBtn.setEnabled(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
            // show message
            Toast.makeText(getApplicationContext(), "Please select a diet type.", Toast.LENGTH_SHORT).show();
            signUpBtn.setEnabled(false);
        }
    };

    private void initListAllergies()
    {
        allergiesArrayList.add(new Allergies("Gluten", R.drawable.gluten_free));
        allergiesArrayList.add(new Allergies("Dairy", R.drawable.dairy));
        allergiesArrayList.add(new Allergies("Egg", R.drawable.egg));
        allergiesArrayList.add(new Allergies("Shellfish", R.drawable.shell)); // Replaced "Tree Nuts" with "Shellfish"
        allergiesArrayList.add(new Allergies("Peanut", R.drawable.peanut));
    }

    private void initListDietType()
    {
        // FIXME: the key in Edamam API are in lower case not upper case
        dietTypeArrayList.add(new UserProfile("Vegetarian", R.drawable.vegetarian));
        dietTypeArrayList.add(new UserProfile("High-Protein", R.drawable.protein));
        dietTypeArrayList.add(new UserProfile("Low-Carb", R.drawable.carb));
        dietTypeArrayList.add(new UserProfile("Low-Fat", R.drawable.fat));
        dietTypeArrayList.add(new UserProfile("Sugar-Conscious", R.drawable.sugar));
    }
}