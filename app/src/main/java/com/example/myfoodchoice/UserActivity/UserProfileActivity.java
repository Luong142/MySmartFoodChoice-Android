package com.example.myfoodchoice.UserActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity
{
    // TODO: declare UI components
    EditText firstName, lastName, age;

    ImageView profilePicture;
    ProgressBar progressBar;

    Button nextBtn;

    String firstNameString, lastNameString, currentPhotoPath, imageFileName;

    int ageInt;

    int profilePictureID;

    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    final static String TAG = "UserProfileActivity";

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init UI components
        firstName = findViewById(R.id.firstNameProfile);
        lastName = findViewById(R.id.lastNameProfile);
        age = findViewById(R.id.ageProfile);
        profilePicture = findViewById(R.id.profilePicture);
        progressBar = findViewById(R.id.progressBar);
        nextBtn = findViewById(R.id.nextBtn);

        // set progress bar to gone
        progressBar.setVisibility(ProgressBar.GONE);

        // set onClickListener button
        nextBtn.setOnClickListener(onNextListener());

        // init userProfile
        userProfile = getIntent().getParcelableExtra("userProfile");
        // log debug
        Log.d(TAG, "onCreate: " + Objects.requireNonNull(userProfile));

    }

    private View.OnClickListener onNextListener()
    {
        return v ->
        {
            firebaseUser = firebaseAuth.getCurrentUser();

            profilePictureID = profilePicture.getId();
            firstNameString = firstName.getText().toString().trim();
            lastNameString = lastName.getText().toString().trim();
            ageInt = Integer.parseInt(age.getText().toString().trim());

            // TODO: the user can click on image view and select their profile picture or take new picture to upload
            // TODO: to Firebase database.

            userProfile.setFirstName(firstNameString);
            userProfile.setLastName(lastNameString);
            userProfile.setAge(ageInt);

        };
    }
}

