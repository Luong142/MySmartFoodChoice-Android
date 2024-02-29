package com.example.myfoodchoice.AuthenticationActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity
{
    // TODO: declare UI components
    EditText firstName, lastName, age;

    ImageView profilePicture;
    ProgressBar progressBar;

    Button createProfileBtn;

    String firstNameString, lastNameString;

    int ageInt;

    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;

    final static String TAG = "UserProfileActivity";

    ActivityResultLauncher<Intent> activityResultLauncher;

    static final String LABEL = "Registered Users";

    Uri selectedImageUri;

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
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();


        // TODO: init UI components
        firstName = findViewById(R.id.firstNameProfile);
        lastName = findViewById(R.id.lastNameProfile);
        age = findViewById(R.id.ageProfile);
        profilePicture = findViewById(R.id.profilePicture);
        progressBar = findViewById(R.id.progressBar);
        createProfileBtn = findViewById(R.id.createProfileBtn);

        // set progress bar to gone
        progressBar.setVisibility(ProgressBar.GONE);

        // set onClickListener button
        createProfileBtn.setOnClickListener(onCreateProfileListener());

        // init userProfile
        userProfile = getIntent().getParcelableExtra("userProfile");
        // log debug
        Log.d(TAG, "onCreate: " + Objects.requireNonNull(userProfile));

        profilePicture.setOnClickListener(onImageClickListener());

        // init activity for gallery part.
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null)
                        {
                            // get the URI of the selected image
                            selectedImageUri = data.getData();
                            Log.d(TAG, "Selected image URI: " + selectedImageUri);
                            // display the selected image in an ImageView.
                            profilePicture.setImageURI(selectedImageUri);
                            getImageInImageView();
                        }
                    }
                }
        );
    }

    private void getImageInImageView()
    {

    }

    private View.OnClickListener onImageClickListener()
    {
        return v ->
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(Intent.createChooser(intent, "Select File"));
        };
    }

    private View.OnClickListener onCreateProfileListener()
    {
        return v ->
        {
            // make the progress bar appear
            progressBar.setVisibility(ProgressBar.VISIBLE);

            firstNameString = firstName.getText().toString().trim();
            lastNameString = lastName.getText().toString().trim();
            ageInt = Integer.parseInt(age.getText().toString().trim());

            // TODO: the user can click on image view and select their profile picture or take new picture to upload
            // TODO: to Firebase database.

            // Check if selectedImageUri is null
            if (selectedImageUri == null)
            {
                Toast.makeText(UserProfileActivity.this,
                        "Please select a profile picture.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(ProgressBar.GONE);
                return; // Exit the method if selectedImageUri is null
            }

            // FIXME: try to upload the image to storage and then retrieve it.
            // upload the image to Firebase Storage
            StorageReference storageRef = firebaseStorage.getReference().
                    child("profile_images/" + firebaseUser.getUid());
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        // Get the download URL
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Set the download URL to the user profile
                            userProfile.setProfileImageUrl(uri.toString());
                            userProfile.setFirstName(firstNameString);
                            userProfile.setLastName(lastNameString);
                            userProfile.setAge(ageInt);

                            Log.d(TAG, "onCreateProfileListener: " + selectedImageUri);
                            Log.d(TAG, "onNextListener: " + userProfile);
                            databaseReference = firebaseDatabase.getReference(LABEL).child(firebaseUser.getUid());
                            databaseReference.setValue(userProfile).addOnCompleteListener(onCompleteListener());
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(UserProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(ProgressBar.GONE);
                    });
        };
    }

    private OnCompleteListener<Void> onCompleteListener()
    {
        {
            return task ->
            {
                Toast.makeText(UserProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // to close this page.
            };
        }
    }
}

