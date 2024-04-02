package com.example.myfoodchoice.SignUpCreateProfileActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.Adapter.AllergiesAdapter;
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

public class UserProfileHealthDeclarationActivity extends AppCompatActivity
{
    // todo: declare database

    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile;

    Intent intent, intentNavToLogin;

    ArrayList<Allergies> allergiesArrayList;

    // todo: declare UI

    Button signUpBtn;

    ProgressBar progressBar;

    Spinner spinnerAllergies;

    CheckBox diabetesCheck, highCholesterolCheck, highBloodPressureCheck;

    Boolean diabetesCheckValue, highCholesterolCheckValue, highBloodPressureCheckValue;

    final static String TAG = "UserProfileHealthDeclaration";

    final static String PATH_USERPROFILE = "User Profile";
    private String allergies;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_health_declaration);

        // TODO: initialize database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_USERPROFILE).child(firebaseUser.getUid());
        userProfile = new UserProfile();
        allergiesArrayList = new ArrayList<>();

        // TODO: init UI component
        signUpBtn = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progressBar);
        diabetesCheck = findViewById(R.id.diabetesCheckbox);
        highCholesterolCheck = findViewById(R.id.highCholesterolCheckbox);
        highBloodPressureCheck = findViewById(R.id.highBloodPressureCheckbox);

        // set progress bar
        progressBar.setVisibility(ProgressBar.GONE);
        signUpBtn.setVisibility(Button.VISIBLE);

        // TODO: init UI component
        initListAllergies();
        spinnerAllergies = findViewById(R.id.allergiesSpinner);
        AllergiesAdapter allergiesAdapter = new AllergiesAdapter(this, allergiesArrayList);
        spinnerAllergies.setAdapter(allergiesAdapter);
        spinnerAllergies.setOnItemSelectedListener(onItemSelectedAllergiesListener());

        // for user profile that has been brought over from the first user profile activity.
        intent = getIntent();
        userProfile = intent.getParcelableExtra("userProfile");
        // Log.d(TAG, "Checking user profile pls: " + userProfile);

        // for sign up button
        signUpBtn.setOnClickListener(onSignUpListener());

        diabetesCheck.setOnCheckedChangeListener(onCheckedDiabetesListener());
        highCholesterolCheck.setOnCheckedChangeListener(onCheckedCholesterolListener());
        highBloodPressureCheck.setOnCheckedChangeListener(onCheckedBloodPressureListener());


    }

    @NonNull
    @Contract(" -> new")
    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            // set progress bar on
            progressBar.setVisibility(ProgressBar.VISIBLE);
            signUpBtn.setVisibility(Button.GONE);

            userProfile.setDiabetes(diabetesCheckValue);
            userProfile.setHighCholesterol(highCholesterolCheckValue);
            userProfile.setHighBloodPressure(highBloodPressureCheckValue);

            Log.d(TAG, "onSignUpListener: " + userProfile);

            // todo: set user profile here
            databaseReferenceUserProfile.setValue(userProfile).addOnCompleteListener(onSignUpCompletedListener());

        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onSignUpCompletedListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(UserProfileHealthDeclarationActivity.
                        this, "Sign up successful", Toast.LENGTH_SHORT).show();
                intentNavToLogin = new Intent(UserProfileHealthDeclarationActivity.this, LoginActivity.class);
                startActivity(intentNavToLogin);
                finish();
            }
            else
            {
                progressBar.setVisibility(ProgressBar.GONE);
                signUpBtn.setVisibility(Button.VISIBLE); // to show the button again.
                Log.d(TAG, "onSignUpFailedListener: " +
                        Objects.requireNonNull(task.getException()).getMessage()); // FIXME: for debug purpose
                Toast.makeText(UserProfileHealthDeclarationActivity.this,
                        "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private CompoundButton.OnCheckedChangeListener onCheckedBloodPressureListener()
    {
        return (buttonView, isChecked) ->
        {
            highBloodPressureCheckValue = isChecked;
        };
    }

    @NonNull
    @Contract(pure = true)
    private CompoundButton.OnCheckedChangeListener onCheckedCholesterolListener()
    {
        return (buttonView, isChecked) ->
        {
            highCholesterolCheckValue = isChecked;
        };
    }

    @NonNull
    @Contract(" -> new")
    private CompoundButton.OnCheckedChangeListener onCheckedDiabetesListener()
    {
        return (buttonView, isChecked) ->
        {
            diabetesCheckValue = isChecked;
        };
    }

    @NonNull
    @Contract(" -> new")
    private AdapterView.OnItemSelectedListener onItemSelectedAllergiesListener()
    {
        return new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
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
    }

    private void initListAllergies()
    {
        allergiesArrayList.add(new Allergies("Gluten", R.drawable.gluten_free));
        allergiesArrayList.add(new Allergies("Dairy", R.drawable.dairy));
        allergiesArrayList.add(new Allergies("Egg", R.drawable.egg));
        allergiesArrayList.add(new Allergies("Shellfish", R.drawable.shell)); // Replaced "Tree Nuts" with "Shellfish"
        allergiesArrayList.add(new Allergies("Peanut", R.drawable.peanut));
    }
}