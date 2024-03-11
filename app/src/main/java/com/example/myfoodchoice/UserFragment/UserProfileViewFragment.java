package com.example.myfoodchoice.UserFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.AuthenticationActivity.UserProfileCreateFirstActivity;
import com.example.myfoodchoice.UserActivity.UserProfileUpdateActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
            databaseReferenceUserProfile.addListenerForSingleValueEvent(valueUserProfileEventListener());
        }

        // TODO: init UI components
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        displayUserProfile = view.findViewById(R.id.displayUserProfile);
        imageView = view.findViewById(R.id.displayUserImage);

        // add onClickListener
        updateProfileBtn.setOnClickListener(onUpdateUserProfileListener());

    }

    private ValueEventListener valueUserProfileEventListener() // TODO: the purpose is to retrieve the data and display it.
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
                    displayUserProfile.setText(userProfile.toString());

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

            }
        };
    }

    private View.OnClickListener onUpdateUserProfileListener()
    {
        return v ->
        {
            Intent intent = new Intent(getActivity(), UserProfileUpdateActivity.class);
            startActivity(intent);
            // no finish()
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_view_profile, container, false);
    }
}