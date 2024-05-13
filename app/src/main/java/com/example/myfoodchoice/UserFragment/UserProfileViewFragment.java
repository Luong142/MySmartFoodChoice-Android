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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserProfileUpdateFirstActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private static final String PATH_USERPROFILE = "Android User Profile";
    // todo: we need to update more attributes (weight, diet type)
    // TODO: declare UI components
    Button updateProfileBtn, deleteAccountBtn;

    ProgressBar progressBarDeleteAccount;

    CardView cardViewDetailUserProfile, cardViewHealthContent;

    TextView userProfileText, healthProfileText;

    ImageView imageView;

    AlertDialog.Builder alertDialog;

    static final String TAG = "UserProfileViewFragment";

    DatabaseReference databaseReferenceUserProfile,
    databaseReferenceRegisteredAccounts,
    databaseReferenceMeals;

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
            databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_USERPROFILE).child(userID);
            databaseReferenceRegisteredAccounts = firebaseDatabase
                    .getReference("Registered Accounts").child(userID);

            databaseReferenceMeals = firebaseDatabase.getReference("Meals").child(userID);

            databaseReferenceUserProfile.addValueEventListener(valueUserProfileEventListener());
        }

        // TODO: init UI components
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);

        userProfileText = view.findViewById(R.id.userProfileText);

        imageView = view.findViewById(R.id.displayUserImage);
        deleteAccountBtn = view.findViewById(R.id.deleteAccountBtn);

        cardViewDetailUserProfile = view.findViewById(R.id.cardViewDetailUserProfile);
        cardViewHealthContent = view.findViewById(R.id.cardViewHealthContent);
        healthProfileText = view.findViewById(R.id.healthProfileText);

        // set on click for card view
        cardViewHealthContent.setVisibility(View.GONE);
        cardViewDetailUserProfile.setOnClickListener(onCardViewDetailUserHealthListener());

        // set progress bar
        progressBarDeleteAccount = view.findViewById(R.id.progressBarDeleteAccount);
        progressBarDeleteAccount.setVisibility(View.GONE);

        // add onClickListener
        updateProfileBtn.setOnClickListener(onUpdateUserProfileListener());
        deleteAccountBtn.setOnClickListener(onDeleteAccountListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCardViewDetailUserHealthListener()
    {
        return v ->
        {
            if (cardViewHealthContent.getVisibility() == View.GONE)
            {
                cardViewHealthContent.setVisibility(View.VISIBLE);
            }
            else
            {
                cardViewHealthContent.setVisibility(View.GONE);
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onDeleteAccountListener()
    {
        return v ->
        {
            // todo: delete both account and user profile and meals.

            alertDialog = new AlertDialog.Builder(requireContext());

            deleteAccountBtn.setVisibility(View.GONE);
            progressBarDeleteAccount.setVisibility(View.VISIBLE);

            alertDialog.setTitle("Delete Account");
            alertDialog.setMessage("Are you sure you want to delete your account?");
            alertDialog.setNegativeButton("No", (dialog, which) ->
            {
                deleteAccountBtn.setVisibility(View.VISIBLE);
                progressBarDeleteAccount.setVisibility(View.GONE);
            });

            alertDialog.setPositiveButton("Yes", (dialog, which) ->
            {
                // delete data from firebase
                databaseReferenceRegisteredAccounts.removeValue().addOnCompleteListener(onCompleteDeleteRegisteredAccountListener());
                databaseReferenceUserProfile.removeValue().addOnCompleteListener(onCompleteDeleteUserProfileListener());
                databaseReferenceMeals.removeValue().addOnCompleteListener(onCompleteDeleteMealsListener());

                // delete account
                if (firebaseUser != null)
                {
                    firebaseUser.delete().
                            addOnCompleteListener(onDeleteAccountFirebaseUserListener());
                }
            });

            // todo: important here
            alertDialog.show();
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onDeleteAccountFirebaseUserListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                // todo: test these functions first before moving on.
                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                // logout user
                firebaseAuth.signOut();

                // go back to login page
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
            else
            {
                Toast.makeText(getContext(), "Error deleting account " +
                        task.getException(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteDeleteMealsListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onCompleteDeleteMeals: Meals deleted successfully");
            }
            else
            {
                Log.d(TAG, "onCompleteDeleteMeals: Error deleting meals " +
                        task.getException());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteDeleteUserProfileListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onCompleteDeleteUserProfile: User Profile deleted successfully");
            }
            else
            {
                Log.d(TAG, "onCompleteDeleteUserProfile: Error deleting user profile " +
                        task.getException());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteDeleteRegisteredAccountListener()
    {
        return task -> {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onCompleteDeleteRegisteredAccount: Registered Account deleted successfully");
            }
            else
            {
                Log.d(TAG, "onCompleteDeleteRegisteredAccount: Error deleting registered account " +
                        task.getException());
            }
        };
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
                    userProfileText.setText(userProfile.getUserProfileDetail());
                    healthProfileText.setText(userProfile.getUserHealthDetail());

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
                // fixme: pls note that when we delete the account and any information about user profile
                // todo: it will delete
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

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