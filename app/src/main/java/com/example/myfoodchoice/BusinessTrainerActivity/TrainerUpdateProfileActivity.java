package com.example.myfoodchoice.BusinessTrainerActivity;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TrainerUpdateProfileActivity extends AppCompatActivity
{
    // TODO: declare UI components
    ImageView imageProfilePictureUpdate;

    EditText firstNameUpdate, lastNameUpdate, contactNumUpdate;

    ProgressBar progressBar;

    Button updateBtn;
    static final String TAG = "UserProfileUpdateFirstActivity";

    DatabaseReference databaseReferenceBusinessProfile;

    StorageReference storageReferenceProfilePics;

    StorageTask<UploadTask.TaskSnapshot> storageTask;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID, firstName, lastName, profileImageURL, myUri, role;

    int contactNumInt;

    BusinessProfile businessProfile;

    Uri selectedImageUri;

    Intent intent;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_update_profile);

        // TODO: init firebase components
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        firebaseAuth = FirebaseAuth.getInstance();

        storageReferenceProfilePics =
                FirebaseStorage.getInstance().getReference().child("ProfilePics");

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceBusinessProfile = firebaseDatabase.getReference("Business Profile").child(userID);
        }

        // define role
        role = "Trainer";

        // TODO: init UI components
        imageProfilePictureUpdate = findViewById(R.id.profilePictureUpdate);
        firstNameUpdate = findViewById(R.id.firstNameUpdate);
        lastNameUpdate = findViewById(R.id.lastNameUpdate);
        contactNumUpdate = findViewById(R.id.contactNumberUpdate);
        updateBtn = findViewById(R.id.updateBtn);
        progressBar = findViewById(R.id.progressBar);

        // set both to gone and visible
        progressBar.setVisibility(ProgressBar.GONE);
        updateBtn.setVisibility(Button.VISIBLE);

        imageProfilePictureUpdate.setOnClickListener(onImageClickListener());
        updateBtn.setOnClickListener(onUpdateListener());

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
                            imageProfilePictureUpdate.setImageURI(selectedImageUri);
                        }
                    }
                }
        );
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpdateListener()
    {
        return v ->
        {
            // to set progress bar on
            progressBar.setVisibility(ProgressBar.VISIBLE);
            updateBtn.setVisibility(Button.GONE);

            // validation here
            if (selectedImageUri == null)
            {
                Toast.makeText(TrainerUpdateProfileActivity.this,
                        "Please select a profile picture.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(ProgressBar.GONE);
                updateBtn.setVisibility(Button.VISIBLE);
                return; // exit the method if selectedImageUri is null
            }

            if (firstNameUpdate.getText().toString().isEmpty())
            {
                firstNameUpdate.setError("Please enter your first name.");
                progressBar.setVisibility(ProgressBar.GONE);
                updateBtn.setVisibility(Button.VISIBLE);
                return; // exit the method if first name is empty
            }

            if (lastNameUpdate.getText().toString().isEmpty())
            {
                lastNameUpdate.setError("Please enter your last name.");
                progressBar.setVisibility(ProgressBar.GONE);
                updateBtn.setVisibility(Button.VISIBLE);
                return; // exit the method if last name is empty
            }

            if (contactNumUpdate.getText().toString().isEmpty())
            {
                contactNumUpdate.setError("Please enter your contact number.");
                progressBar.setVisibility(ProgressBar.GONE);
                updateBtn.setVisibility(Button.VISIBLE);
                return; // exit the method if contact number is empty
            }

            // init
            firstName = firstNameUpdate.getText().toString().trim();
            lastName = lastNameUpdate.getText().toString().trim();
            contactNumInt = Integer.parseInt(contactNumUpdate.getText().toString().trim());
            profileImageURL = selectedImageUri.toString();

            // TODO: a part of user profile, update firebase user
            firebaseUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName + lastName)
                    .setPhotoUri(selectedImageUri)
                    .build()).addOnFailureListener(onFailureListener());

            // checking if it is updated.
            // Log.d(TAG, "onNextBtnListener: " + firebaseUser.getPhotoUrl());
            // Log.d(TAG, "onNextBtnListener: " + firebaseUser.getDisplayName());

            // upload the image to Firebase Storage
            final StorageReference storageReference = storageReferenceProfilePics.child
                    (firebaseUser.getUid() + ".jpg");

            // FIXME: the selected image Uri haven't converted to Uri path.
            storageTask = storageReference.putFile(selectedImageUri).addOnFailureListener(onFailurePart());
            // Log.d(TAG,"onCompeteUploadListener: " + firebaseUser.getDisplayName());

            // to init and set value
            businessProfile = new BusinessProfile();
            businessProfile.setFirstName(firstName);
            businessProfile.setLastName(lastName);
            businessProfile.setContactNumber(contactNumInt);
            businessProfile.setRole(role);

            // set image here
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

    @NonNull
    @Contract(pure = true)
    private OnFailureListener onFailurePart()
    {
        return v ->
        {
            Log.d(TAG, "onFailurePart: " + v);
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Uri> onCompleteUploadListener()
    {
        return task ->
        {
            // FIXME: profile picture task is unsuccessful.
            if (task.isSuccessful())
            {
                Uri downloadUri = task.getResult();
                myUri = downloadUri.toString();

                // TODO: set the value based on UserProfile class.
                businessProfile.setProfileImageUrl(myUri);
                // Log.d(TAG, "onCreateProfileListener: " + selectedImageUri);
                // Log.d(TAG, "onNextListener: " + userProfile);
                // databaseReferenceUserProfile.setValue(userProfile).addOnCompleteListener(onCompleteListener());
                Map<String, Object> updates = new HashMap<>();
                updates.put("profileImageUrl", myUri);
                updates.put("contactNumber", contactNumInt);
                updates.put("firstName", firstName);
                updates.put("lastName", lastName);

                // update the children
                databaseReferenceBusinessProfile.setValue(updates)
                        .addOnCompleteListener(onCompleteListener())
                        .addOnFailureListener(onFailureListener());

                Log.d(TAG,"onCompeteUploadListener: " + firebaseUser.getPhotoUrl());
            }
            else
            {
                Log.d(TAG, "onCompleteUploadListener: " + myUri);
                progressBar.setVisibility(ProgressBar.GONE);
                //Log.e(TAG, "onCompleteUploadListener: " + task.getException());
                Toast.makeText(TrainerUpdateProfileActivity.this,
                        "Image not selected.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteListener()
    {
        return task ->
        {
            firebaseUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(myUri)).build())
                    .addOnCompleteListener(onCompleteUpdateProfileListener())
                    .addOnFailureListener(onFailureListener());

            // to carry the userProfile for the next update in second one.
            intent = new Intent(TrainerUpdateProfileActivity.this,
                    TrainerMainMenuActivity.class);
            startActivity(intent);
            finish();
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteUpdateProfileListener()
    {
        return task ->
        {
            Log.d(TAG, "onCompleteUpdateProfileListener: " + task.isSuccessful());
            Toast.makeText(TrainerUpdateProfileActivity.this,
                    "Update profile successfully.", Toast.LENGTH_SHORT).show();
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnFailureListener onFailureListener()
    {
        return e ->
        {
            Log.e(TAG, "onFailure: ", e);
            Toast.makeText(TrainerUpdateProfileActivity.this,
                    "Failed to update profile.", Toast.LENGTH_SHORT).show();
        };
    }

    @NonNull
    @Contract(pure = true)
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
}