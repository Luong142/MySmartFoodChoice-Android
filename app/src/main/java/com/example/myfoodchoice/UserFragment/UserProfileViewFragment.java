package com.example.myfoodchoice.UserFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserProfileUpdateFirstActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

public class UserProfileViewFragment extends Fragment
{
    // TODO: declare UI components
    Button updateProfileBtn;

    TextView displayUserProfile;

    ImageView imageView;

    static final String TAG = "UserProfileViewFragment";

    DatabaseReference databaseReferenceUserProfile;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID;

    UserProfile userProfile;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile = firebaseDatabase.getReference("User Profile").child(userID);
            // databaseReferenceUserProfile.addListenerForSingleValueEvent(valueUserProfileSingleEventListener());
            databaseReferenceUserProfile.addValueEventListener(valueUserProfileEventListener());
        }

        // TODO: init UI components
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        displayUserProfile = view.findViewById(R.id.displayUserProfile);
        imageView = view.findViewById(R.id.displayUserImage);

        // add onClickListener
        updateProfileBtn.setOnClickListener(onUpdateUserProfileListener());

    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueUserProfileEventListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // get the data from database.
                userProfile = snapshot.getValue(UserProfile.class);
                // Log.d(TAG, "onDataChange: " + userProfile);

                if (userProfile != null)
                {
                    // display user profile info
                    StringBuilder stringBuilder = new StringBuilder();
                    String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                    int age = userProfile.getAge();
                    stringBuilder.append("Full Name: ").append(fullName).append("\n");
                    stringBuilder.append("Age: ").append(age);

                    displayUserProfile.setText(stringBuilder);

                    // set profile picture here
                    String profileImageUrl = userProfile.getProfileImageUrl();
                    Uri profileImageUri = Uri.parse(profileImageUrl);
                    // FIXME: the image doesn't show because the image source is from Gallery within android device.
                    // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                    Picasso.get()
                            .load(profileImageUri)
                            .resize(150, 150)
                            .error(R.drawable.error)
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), "Error retrieving data from database " +
                        error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    /*
    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueUserProfileSingleEventListener() // TODO: the purpose is to retrieve the data and display it.
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // get the data from database.
                userProfile = snapshot.getValue(UserProfile.class);
                Log.d(TAG, "onDataChange: " + userProfile);

                if (userProfile != null)
                {
                    // display user profile info
                    StringBuilder stringBuilder = new StringBuilder();
                    String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                    int age = userProfile.getAge();
                    stringBuilder.append("Full Name: ").append(fullName).append("\n");
                    stringBuilder.append("Age: ").append(age).append("\n");

                    displayUserProfile.setText(stringBuilder);

                    // set profile picture here
                    String profileImageUrl = userProfile.getProfileImageUrl();
                    Uri profileImageUri = Uri.parse(profileImageUrl);
                    // FIXME: the image doesn't show because the image source is from Gallery within android device.
                    // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                    Picasso.get()
                            .load(profileImageUri)
                            .resize(150, 150)
                            .error(R.drawable.error)
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), "Error retrieving data from database " +
                        error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }



     */
    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpdateUserProfileListener()
    {
        return v ->
        {
            Intent intent = new Intent(getActivity(), UserProfileUpdateFirstActivity.class);
            startActivity(intent);
            // no finish()
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_view_profile, container, false);
    }
}