package com.example.myfoodchoice.SignUpCreateProfileActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.AdapterSpinner.BusinessRoleAdapter;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.WelcomeActivity.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;

public class BusinessProfileCreateActivity extends AppCompatActivity
{
    // TODO: declare UI components
    ImageView profileImage;

    EditText firstName, lastName, contactNumber;

    Spinner spinnerRole;

    String firstNameString, lastNameString;

    int contactNumberInt;

    Button createProfileBtn;

    ProgressBar progressBar;

    FirebaseDatabase firebaseDatabase;

    BusinessProfile businessProfile;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceTrainerProfile;

    StorageReference storageReferenceProfilePics;

    StorageTask<UploadTask.TaskSnapshot> storageTask;

    final static String TAG = "BusinessProfileCreateActivity";

    ActivityResultLauncher<Intent> activityResultLauncher;

    static final String LABEL_USER = "Registered Accounts";

    static final String LABEL = "Business Profile";

    Uri selectedImageUri;
    String myUri, role;
    Intent intentRetrieveUserAccount;
    Account account;
    ArrayList<BusinessProfile> roleArrayList;
    DatabaseReference databaseReferenceRegisteredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile_create);

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferenceTrainerProfile = firebaseDatabase.getReference(LABEL).child(firebaseUser.getUid());
        storageReferenceProfilePics =
                FirebaseStorage.getInstance().getReference().child("ProfilePics");
        databaseReferenceRegisteredUser = firebaseDatabase.getReference(LABEL_USER).child(firebaseUser.getUid());

        // init array list
        roleArrayList = new ArrayList<>();

        // set model class
        businessProfile = new BusinessProfile();
        intentRetrieveUserAccount = getIntent();
        account = intentRetrieveUserAccount.getParcelableExtra("userAccount");

        // TODO: init UI components
        profileImage = findViewById(R.id.profileImage);
        firstName = findViewById(R.id.firstNameProfile);
        lastName = findViewById(R.id.lastNameProfile);
        contactNumber = findViewById(R.id.contactNumberProfile);
        progressBar = findViewById(R.id.progressBar);

        // set progress bar to gone
        progressBar.setVisibility(ProgressBar.GONE);

        // TODO: init spinner part
        initBusinessRole();
        spinnerRole = findViewById(R.id.roleSpinner);
        BusinessRoleAdapter businessRoleAdapter = new BusinessRoleAdapter(this, roleArrayList);
        spinnerRole.setAdapter(businessRoleAdapter);
        spinnerRole.setOnItemSelectedListener(onItemSelectedRoleListener);

        // button
        createProfileBtn = findViewById(R.id.createProfileBtn);
        createProfileBtn.setOnClickListener(onCreateProfileListener());

        profileImage.setOnClickListener(onImageClickListener());

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
                            // Log.d(TAG, "Selected image URI: " + selectedImageUri);
                            // display the selected image in an ImageView.
                            profileImage.setImageURI(selectedImageUri);
                        }
                    }
                }
        );

        getUserInfo();
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateProfileListener()
    {
        return v ->
        {

            // TODO: the user can click on image view and select their profile picture or take new picture to upload
            // TODO: to Firebase database.

            // check if selectedImageUri is null
            if (selectedImageUri == null)
            {
                Toast.makeText(BusinessProfileCreateActivity.this,
                        "Please select a profile picture.", Toast.LENGTH_SHORT).show();
                // profileImage.requestFocus();
                return; // exit the method if selectedImageUri is null
            }

            if (TextUtils.isEmpty(firstName.getText().toString().trim()))
            {
                firstName.setError("Please enter your first name.");
                firstName.requestFocus();
                return; // exit the method if age is not entered
            }

            if (TextUtils.isEmpty(lastName.getText().toString().trim()))
            {
                lastName.setError("Please enter your last name.");
                lastName.requestFocus();
                return; // exit the method if age is not entered
            }

            if (TextUtils.isEmpty(contactNumber.getText().toString().trim()))
            {
                contactNumber.setError("Please enter your contact number.");
                contactNumber.requestFocus();
                return; // exit the method if age is not entered
            }

            // make the progress bar appear
            progressBar.setVisibility(ProgressBar.VISIBLE);
            createProfileBtn.setVisibility(Button.GONE);

            // upload the image to Firebase Storage
            final StorageReference storageReference = storageReferenceProfilePics.child
                    (firebaseUser.getUid() + ".jpg");

            // FIXME: the selected image Uri haven't converted to Uri path.
            storageTask = storageReference.putFile(selectedImageUri).addOnFailureListener(onFailurePart());
            // Log.d(TAG,"onCompeteUploadListener: " + firebaseUser.getDisplayName());

            firstNameString = firstName.getText().toString().trim();
            lastNameString = lastName.getText().toString().trim();
            contactNumberInt = Integer.parseInt(contactNumber.getText().toString().trim());

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
    private OnCompleteListener<Uri> onCompleteUploadListener()
    {
        return task ->
        {
            // FIXME: profile picture task is unsuccessful.
            if (task.isSuccessful())
            {
                Uri downloadUri = task.getResult();
                myUri = downloadUri.toString();

                // set data here
                businessProfile.setFirstName(firstNameString);
                businessProfile.setLastName(lastNameString);
                businessProfile.setContactNumber(contactNumberInt);
                businessProfile.setRole(role);

                // set business account here
                if (account == null)
                {
                    // Handle the case where the account object is null
                    Toast.makeText(this, "Account object is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                account.setAccountType(role);

                // FIXME: set image here based on the model
                businessProfile.setProfileImageUrl(myUri);

                Log.d(TAG, "onCompleteUploadListener: " + businessProfile);
                // Log.d(TAG, "onCompleteUploadListener: " + userAccount);
                // Log.d(TAG, "onCreateProfileListener: " + selectedImageUri);

                // TODO: set the value based on TrainerProfile class.
                // databaseReferenceUserProfile.setValue(userProfile).addOnCompleteListener(onCompleteListener());
                databaseReferenceRegisteredUser.setValue(account)
                        .addOnCompleteListener(onCompleteUserAccountListener())
                        .addOnFailureListener(onFailurePart());
            }
            else
            {
                // Log.d(TAG, "onCompleteUploadListener: " + myUri);
                progressBar.setVisibility(ProgressBar.GONE);
                //Log.e(TAG, "onCompleteUploadListener: " + task.getException());
                Toast.makeText(BusinessProfileCreateActivity.this,
                        "Image not selected.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteUserAccountListener()
    {
        return task ->
        {
            databaseReferenceTrainerProfile.setValue(businessProfile)
                    .addOnCompleteListener(onCompleteListener())
                    .addOnFailureListener(onFailurePart());

            firebaseUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(firstNameString + " " + lastNameString).build());

            firebaseUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(myUri)).build()).addOnCompleteListener(onCompleteCreateProfileListener());
            // Log.d(TAG,"onCompeteUploadListener: " + firebaseUser.getPhotoUrl());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                // Profile successfully written!
                // Log.d(TAG, "Profile successfully written!");
                Toast.makeText(BusinessProfileCreateActivity.this,
                        "Profile successfully created!", Toast.LENGTH_SHORT).show();

            }
            else
            {
                // If the write failed, display an error message
                // Log.w(TAG, "Error writing profile to database", task.getException());
                Toast.makeText(BusinessProfileCreateActivity.this,
                        "Failed to create profile.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteCreateProfileListener()
    {
        return task ->
        {
            Intent intent = new Intent(BusinessProfileCreateActivity.this,
                    WelcomeActivity.class);
            startActivity(intent);
            finish(); // to close this page.
        };
    }

    private final AdapterView.OnItemSelectedListener onItemSelectedRoleListener =
            new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id)
                {
                    // the purpose is for spinner to select and apply the string dietType to define the user profile
                    BusinessProfile businessProfile1 = (BusinessProfile) parent.getItemAtPosition(position);
                    role = businessProfile1.getRole();
                    // Log.d(TAG, "onItemSelected: " + role);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            };

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

    private void getUserInfo()
    {
        databaseReferenceTrainerProfile.child(firebaseUser.getUid()).addValueEventListener(valueEventListener());
    }

    @NonNull
    @Contract(" -> new")
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
                        Picasso.get().load(image).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };
    }

    private void initBusinessRole()
    {
        roleArrayList.add(new BusinessProfile("Trainer", R.drawable.trainer_icon_pink));
        roleArrayList.add(new BusinessProfile("Dietitian", R.drawable.dietitian_icon));
    }
}