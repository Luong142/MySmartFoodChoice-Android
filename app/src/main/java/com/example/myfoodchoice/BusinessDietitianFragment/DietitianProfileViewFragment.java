package com.example.myfoodchoice.BusinessDietitianFragment;

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
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.BusinessDietitianActivity.DietitianUpdateProfileActivity;
import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.R;
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


public class DietitianProfileViewFragment extends Fragment
{
    // TODO: declare UI components
    Button updateProfileBtn, deleteAccountBtn;

    TextView displayTrainerProfile;

    ImageView imageView;

    ProgressBar progressBarDeleteAccount;

    static final String TAG = "DietitianProfileView";

    DatabaseReference databaseReferenceBusinessProfile,
            databaseReferenceRegisteredAccount,
            DatabaseReferenceRecipes,
            DatabaseReferenceHealthTips;

    AlertDialog.Builder alertDialog;

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
            databaseReferenceBusinessProfile.addValueEventListener(valueDietitianProfileEventListener());

            databaseReferenceRegisteredAccount =
                    firebaseDatabase.getReference("Registered Accounts").child(userID);

            DatabaseReferenceRecipes = firebaseDatabase.getReference("Dietitian Recipe").child(userID);

            // databaseReferenceBusinessProfile.addListenerForSingleValueEvent(
            // valueDietitianProfileSingleEventListener());
        }

        // TODO: init UI components
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        displayTrainerProfile = view.findViewById(R.id.displayUserProfile);
        imageView = view.findViewById(R.id.displayUserImage);
        deleteAccountBtn = view.findViewById(R.id.deleteAccountBtn);

        // for progress bar
        progressBarDeleteAccount = view.findViewById(R.id.progressBarDeleteAccount);
        progressBarDeleteAccount.setVisibility(View.GONE);

        // add onClickListener
        updateProfileBtn.setOnClickListener(onUpdateDietitianProfileListener());
        deleteAccountBtn.setOnClickListener(onDeleteDietitianAccountListener());
    }

    @NonNull
    @Contract(" -> new")
    private View.OnClickListener onDeleteDietitianAccountListener()
    {
        return v ->
        {
            // todo: delete both account and business profile, not sure if recipe or health tips should be deleted
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
                databaseReferenceRegisteredAccount.removeValue().addOnCompleteListener(onCompleteDeleteRegisteredAccountListener());
                databaseReferenceBusinessProfile.removeValue().addOnCompleteListener(onCompleteDeleteBusinessProfileListener());
                DatabaseReferenceRecipes.removeValue().addOnCompleteListener(onCompleteDeleteRecipesListener());

                // delete account
                if (firebaseUser != null)
                {
                    firebaseUser.delete().
                            addOnCompleteListener(onDeleteAccountFirebaseUserListener());
                }
            });

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
    private OnCompleteListener<Void> onCompleteDeleteRecipesListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onCompleteDeleteRecipesListener: Recipes deleted successfully");
            }
            else
            {
                Log.d(TAG, "onCompleteDeleteRecipesListener: Error deleting recipes " +
                        task.getException());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteDeleteBusinessProfileListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onCompleteDeleteBusinessProfile: Business Profile deleted successfully");
            }
            else
            {
                Log.d(TAG, "onCompleteDeleteBusinessProfile: Error deleting business profile " +
                        task.getException());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteDeleteRegisteredAccountListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "nCompleteDeleteRegisteredAccount: Registered Account deleted successfully");
            }
            else
            {
                Log.d(TAG, "nCompleteDeleteRegisteredAccount: Error deleting registered account " +
                        task.getException());
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueDietitianProfileEventListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // get the data from database.
                businessProfile = snapshot.getValue(BusinessProfile.class);
                // Log.d(TAG, "onDataChange: " + businessProfile);

                if (businessProfile != null)
                {
                    if (businessProfile.getRole().equals("Dietitian"))
                    {
                        // form a display here
                        StringBuilder stringBuilder = new StringBuilder();
                        String fullName = businessProfile.getFirstName() + " " + businessProfile.getLastName();
                        int contactNumber = businessProfile.getContactNumber();
                        stringBuilder.append("Full Name\n").append(fullName).append("\n").append("\n");
                        stringBuilder.append("Contact Number\n").append(contactNumber);

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
    @NonNull
    @Contract(" -> new")
    private View.OnClickListener onUpdateDietitianProfileListener()
    {
        return v ->
        {
            Intent intent = new Intent(getContext(), DietitianUpdateProfileActivity.class);
            startActivity(intent);
            // no finish()
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_profile_view, container, false);
    }
}