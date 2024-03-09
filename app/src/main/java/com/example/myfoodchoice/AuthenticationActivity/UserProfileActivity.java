package com.example.myfoodchoice.AuthenticationActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity
{
    // TODO: declare UI components
    EditText firstName, lastName, age;

    ImageView profilePicture, maleImage, femaleImage;
    ProgressBar progressBar;

    Button createProfileBtn;

    String firstNameString, lastNameString, myUri, gender;

    int ageInt;

    FirebaseDatabase firebaseDatabase;

    UserProfile userProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceRegisteredUser;

    StorageReference storageReferenceProfilePics;

    StorageTask<UploadTask.TaskSnapshot> storageTask;

    final static String TAG = "UserProfileActivity";

    ActivityResultLauncher<Intent> activityResultLauncher;

    static final String LABEL = "Registered Users";

    Uri selectedImageUri, defaultImageUri; // TODO: fix me

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
        databaseReferenceRegisteredUser = firebaseDatabase.getReference(LABEL).child(firebaseUser.getUid());
        storageReferenceProfilePics =
                FirebaseStorage.getInstance().getReference().child("ProfilePics");

        // TODO: init UI components
        firstName = findViewById(R.id.firstNameProfile);
        lastName = findViewById(R.id.lastNameProfile);
        age = findViewById(R.id.ageProfile);
        profilePicture = findViewById(R.id.profilePicture);
        progressBar = findViewById(R.id.progressBar);
        createProfileBtn = findViewById(R.id.createProfileBtn);
        maleImage = findViewById(R.id.maleImage);
        femaleImage = findViewById(R.id.femaleImage);

        // set gender to male by default
        gender = "Default";
        maleImage.setOnClickListener(onMaleClickListener());
        femaleImage.setOnClickListener(onFemaleClickListener());

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
                        }
                    }
                }
        );

        // TODO: this is to display the profile picture in here
        // we can reference this function to UserMainMenuActivity and use it to display the profile picture.
        getUserInfo();
    }

    // TODO: update this new field for the user to select to get the gender
    private View.OnClickListener onFemaleClickListener()
    {
        return v ->
        {
            maleImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_notfocused));
            femaleImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_focused));
            gender = "Female";
        };
    }

    private View.OnClickListener onMaleClickListener()
    {
        return v ->
        {
            maleImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_focused));
            femaleImage.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_notfocused));
            gender = "Male";
        };
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
            Log.d(TAG, "onCreateProfileListener: " + gender);

            // TODO: the user can click on image view and select their profile picture or take new picture to upload
            // TODO: to Firebase database.

            // check if selectedImageUri is null
            if (selectedImageUri == null)
            {
                Toast.makeText(UserProfileActivity.this,
                        "Please select a profile picture.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(ProgressBar.GONE);
                return; // exit the method if selectedImageUri is null
            }

            if (Objects.equals(gender, "Default"))
            {
                Toast.makeText(UserProfileActivity.this, "Please select a gender.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(ProgressBar.GONE);
                return; // exit the method if gender is not selected
            }

            // upload the image to Firebase Storage
            final StorageReference storageReference = storageReferenceProfilePics.child
                    (firebaseUser.getUid() + ".jpg");

            storageTask = storageReference.putFile(selectedImageUri);

            // set the download URL to the user profile
            userProfile.setGender(gender); // FIXME: need to test this field.
            userProfile.setFirstName(firstNameString);
            userProfile.setLastName(lastNameString);
            userProfile.setAge(ageInt);

            storageTask.continueWithTask(task ->
            {
                if (!task.isSuccessful())
                {
                    throw Objects.requireNonNull(task.getException());
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(onCompleteUploadListener());
        };
    }

    private OnCompleteListener<Uri> onCompleteUploadListener()
    {
        return task ->
        {
            // FIXME: need to test this field, profile picture
            if (task.isSuccessful())
            {
                Uri downloadUri = task.getResult();
                myUri = downloadUri.toString();

                userProfile.setProfileImageUrl(myUri);
                //Log.d(TAG, "onCreateProfileListener: " + selectedImageUri);
                Log.d(TAG, "onNextListener: " + userProfile);
                databaseReferenceRegisteredUser.setValue(userProfile).addOnCompleteListener(onCompleteListener());
            }
            else
            {
                progressBar.setVisibility(ProgressBar.GONE);
                //Log.e(TAG, "onCompleteUploadListener: " + task.getException());
                Toast.makeText(UserProfileActivity.this, "Image not selected.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getUserInfo()
    {
        databaseReferenceRegisteredUser.child(firebaseUser.getUid()).addValueEventListener(valueEventListener());
    }

    private ValueEventListener valueEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0)
                {
                    if (snapshot.hasChild("image"))
                    {
                        String image = Objects.requireNonNull(snapshot.child("image").getValue()).toString();
                        Picasso.get().load(image).into(profilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
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

