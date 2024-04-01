package com.example.myfoodchoice.BusinessTrainerFragment;

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

import com.example.myfoodchoice.BusinessTrainerActivity.TrainerUpdateProfileActivity;
import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;


public class TrainerProfileViewFragment extends Fragment
{
    // TODO: declare UI components
    Button updateProfileBtn;

    TextView displayTrainerProfile;

    ImageView imageView;

    static final String TAG = "TrainerProfileView";

    DatabaseReference databaseReferenceBusinessProfile;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID;

    BusinessProfile businessProfile;

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
            databaseReferenceBusinessProfile =
                    firebaseDatabase.getReference("Business Profile").child(userID);

            databaseReferenceBusinessProfile.addValueEventListener(valueTrainerProfileEventListener());
            //databaseReferenceBusinessProfile.
            // addListenerForSingleValueEvent(valueTrainerProfileSingleEventListener());
        }

        // TODO: init UI components
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        displayTrainerProfile = view.findViewById(R.id.displayUserProfile);
        imageView = view.findViewById(R.id.displayUserImage);

        // add onClickListener
        updateProfileBtn.setOnClickListener(onUpdateTrainerProfileListener());
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueTrainerProfileEventListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // get the data from database.
                businessProfile = snapshot.getValue(BusinessProfile.class);
                Log.d(TAG, "onDataChange: " + businessProfile);

                if (businessProfile != null)
                {
                    if (businessProfile.getRole().equals("Trainer"))
                    {
                        // form a display here
                        StringBuilder stringBuilder = new StringBuilder();
                        String fullName = businessProfile.getFirstName() + " " + businessProfile.getLastName();
                        int contactNumber = businessProfile.getContactNumber();
                        stringBuilder.append("Full Name: ").append(fullName).append("\n");
                        stringBuilder.append("Contact Number: ").append(contactNumber);

                        // display user profile info
                        displayTrainerProfile.setText(stringBuilder);

                        // set profile picture here
                        String profileImageUrl = businessProfile.getProfileImageUrl();
                        Uri profileImageUri = Uri.parse(profileImageUrl);
                        // FIXME: the image doesn't show because the image source is from Gallery within android device.
                        // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                        Picasso.get()
                                .load(profileImageUri)
                                .resize(150, 150)
                                .error(R.drawable.error)
                                .into(imageView);
                    }
                    else
                    {
                        Toast.makeText(getContext(),
                                "Error there is no business vendor as trainer role in the database",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    /*
    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueTrainerProfileSingleEventListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // get the data from database.
                businessProfile = snapshot.getValue(BusinessProfile.class);
                Log.d(TAG, "onDataChange: " + businessProfile);

                if (businessProfile != null)
                {
                    if (businessProfile.getRole().equals("Trainer"))
                    {
                        // display user profile info
                        displayTrainerProfile.setText(businessProfile.toString());

                        // set profile picture here
                        String profileImageUrl = businessProfile.getProfileImageUrl();
                        Uri profileImageUri = Uri.parse(profileImageUrl);
                        // FIXME: the image doesn't show because the image source is from Gallery within android device.
                        // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                        Picasso.get()
                                .load(profileImageUri)
                                .resize(150, 150)
                                .error(R.drawable.error)
                                .into(imageView);
                    }
                    else
                    {
                        Toast.makeText(getContext(),
                                "Error there is no business vendor as trainer role in the database",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

     */

    @NonNull
    @Contract(" -> new")
    private View.OnClickListener onUpdateTrainerProfileListener()
    {
        return v ->
        {
            Intent intent = new Intent(getContext(), TrainerUpdateProfileActivity.class);
            startActivity(intent);
            // no finish()
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trainer_profile_view, container, false);
    }
}