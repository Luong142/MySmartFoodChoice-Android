package com.example.myfoodchoice.AuthenticationActivity;

import static com.example.myfoodchoice.AuthenticationActivity.UserProfileCreateFirstActivity.LABEL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myfoodchoice.Adapter.DietTypeAdapter;
import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserProfileCreateSecondActivity extends AppCompatActivity
{
    // TODO: declare database
    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile;

    StorageReference storageReferenceProfilePics;


    final static String TAG = "UserProfileCreateSecondActivity";

    ArrayList<UserProfile> dietTypeArrayList;

    String dietType;

    EditText editIntHeight, editIntWeight;

    // TODO: declare UI component
    Spinner spinnerDietType;

    Intent intent;

    Button signUpBtn;

    int weight, height;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_create_second);

        // TODO: initialize database
        firebaseDatabase = FirebaseDatabase.getInstance();
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

        // for height and weight
        editIntHeight = findViewById(R.id.heightProfile);
        editIntWeight = findViewById(R.id.weightProfile);

        // for user profile that has been brought over from the first user profile activity.
        intent = new Intent();
        userProfile = intent.getParcelableExtra("userProfile");
        Log.d(TAG, "Checking user profile pls: " + userProfile);

        // button
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(onSignUpListener());

    }

    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            weight = Integer.parseInt(editIntWeight.getText().toString());
            height = Integer.parseInt(editIntHeight.getText().toString());

            // for now we define it as a string.
            userProfile.setWeight(String.valueOf(weight));
            userProfile.setHeight(String.valueOf(height));
            userProfile.setDietType(dietType);

            // TODO: create user profile through this and add it inside of the firebase
            // TODO: do this tmr

        };
    }

    private final AdapterView.OnItemSelectedListener onItemSelectedDietTypeListener =
            new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
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
        dietTypeArrayList.add(new UserProfile("Non-Vegetarian", R.drawable.nonVege));
    }
}