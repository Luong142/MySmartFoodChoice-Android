package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnRecommendRecipeDietitianListener;
import com.example.myfoodchoice.AdapterRecyclerView.ViewUserProfileAdapter;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;


public class DietitianViewUserProfileFragment extends Fragment implements OnRecommendRecipeDietitianListener
{
    // todo: the purpose of this fragment is to let the dietitian to choose which user profile
    // they should create recipe for them
    // todo: also we need to specify the user profile and
    //  let the dietitian to see and decide which recipe is suitable for them

    // todo: init firebase
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReferenceUserProfile;

    final static String PATH_USER_PROFILE = "User Profile";

    final static String TAG = "DietitianViewUserProfileFragment";

    FirebaseUser firebaseUser;

    UserProfile userProfile, selectedUserProfile;

    ArrayList<UserProfile> userProfileArrayList;

    String userID;

    Bundle bundleStore, bundlerFromMain;

    DietitianCreateRecipeFragment dietitianCreateRecipeFragment;

    DietitianSearchRecipeFragment dietitianSearchRecipeFragment;

    DietitianHealthTipsFragment dietitianHealthTipsFragment;

    // todo: declare UI components

    RecyclerView userProfileRecyclerView;

    ViewUserProfileAdapter viewUserProfileAdapter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile =
                    firebaseDatabase.getReference(PATH_USER_PROFILE);
            // we need to read every single user profile in the database
            // note that there is no user id here

            databaseReferenceUserProfile.addValueEventListener(onAllUserProfileViewListener());

            userProfileArrayList = new ArrayList<>();
        }

        // init utilities
        bundleStore = new Bundle();
        bundlerFromMain = getArguments();
        dietitianCreateRecipeFragment = new DietitianCreateRecipeFragment();
        dietitianSearchRecipeFragment = new DietitianSearchRecipeFragment();
        dietitianHealthTipsFragment = new DietitianHealthTipsFragment();

        // todo: init UI components
        userProfileRecyclerView = view.findViewById(R.id.userProfileRecyclerView);

        // set adapter here
        viewUserProfileAdapter = new ViewUserProfileAdapter(userProfileArrayList,
                this);
        setAdapter();
    }

    @Override
    public void recommendRecipeDietitian(int position)
    {
        // todo: this should transfer dietitian to next part.
        selectedUserProfile = userProfileArrayList.get(position);
        // Log.d(TAG, "recommendRecipeDietitian: " + selectedUserProfile);

        // todo: careful with the name
        bundleStore.putParcelable("selectedUserProfile", selectedUserProfile);

        // fixme: remember to set arguments for both.
        dietitianCreateRecipeFragment.setArguments(bundleStore);
        dietitianSearchRecipeFragment.setArguments(bundleStore);
        dietitianHealthTipsFragment.setArguments(bundleStore);

        try
        {
            String action = bundlerFromMain.getString("action");

            // Log.d(TAG, "recommendRecipeDietitian: " + action);
            if (Objects.equals(action, "createRecipe"))
            {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, dietitianCreateRecipeFragment)
                        .commit();
            }

            if (Objects.equals(action, "searchRecipe"))
            {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, dietitianSearchRecipeFragment)
                        .commit();
            }

            if (Objects.equals(action, "createHealthTips"))
            {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, dietitianHealthTipsFragment)
                        .commit();
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "recommendRecipeDietitian: " + e.getMessage());
        }
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener onAllUserProfileViewListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot childSnapshot: snapshot.getChildren())
                {
                    userProfile = childSnapshot.getValue(UserProfile.class);

                    if (userProfile != null)
                    {
                        userProfileArrayList.add(userProfile);
                        // Log.d(TAG, "onDataChange: " + userProfile.getKey());
                    }
                }
                // we notify the adapter
                viewUserProfileAdapter.notifyItemChanged(userProfileArrayList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        // Log.d(TAG, "Before setting layout manager: " + (recipeRecyclerView == null));
        userProfileRecyclerView.setLayoutManager(layoutManager);
        // Log.d(TAG, "After setting layout manager: " + (recipeRecyclerView == null));
        userProfileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userProfileRecyclerView.setAdapter(viewUserProfileAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_view_user_profile, container, false);
    }
}