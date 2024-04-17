package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DietitianCreateRecipeFragment extends Fragment
{
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.



    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_create_recipe, container, false);
    }
}