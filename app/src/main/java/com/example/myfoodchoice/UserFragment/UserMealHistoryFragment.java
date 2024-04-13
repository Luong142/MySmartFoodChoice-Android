package com.example.myfoodchoice.UserFragment;

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

import com.example.myfoodchoice.AdapterInterfaceListener.OnActionMealListener;
import com.example.myfoodchoice.AdapterRecyclerView.MealHistoryAdapter;
import com.example.myfoodchoice.ModelMeal.Meal;
import com.example.myfoodchoice.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class UserMealHistoryFragment extends Fragment implements OnActionMealListener
{
    private static final String TAG = "UserMealHistoryFragment";
    // todo: init firebase
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceMeals;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meals"; // fixme:  the path need to access daily meal.

    String userID;

    // TODO: declare UI components
    RecyclerView mealHistoryRecyclerView;

    MealHistoryAdapter mealHistoryAdapter;

    TabLayout timeTabLayout;

    ArrayList<Meal> mealArrayList;

    boolean showMorningMeal, showAfternoonMeal, showNightMeal;

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
                    firebaseDatabase.getReference(PATH_USERPROFILE).child(userID);

            databaseReferenceMeals =
                    firebaseDatabase.getReference(PATH_DAILY_FOOD_INTAKE);
            databaseReferenceMeals.addChildEventListener(valueChildMealEventListener());
        }

        // todo: init UI components
        timeTabLayout = view.findViewById(R.id.timeTabLayout);

        // need to set this recycler view.
        mealHistoryRecyclerView = view.findViewById(R.id.mealRecyclerView);
        mealHistoryAdapter = new MealHistoryAdapter(mealArrayList, this);

    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener valueChildMealEventListener()
    {
        return new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, error.getMessage());
            }
        };
    }

    @Override
    public void onRemoveMeal(int position)
    {

    }

    @Override
    public void onViewMeal(int position)
    {

    }


    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        mealHistoryRecyclerView.setLayoutManager(layoutManager);
        mealHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mealHistoryRecyclerView.setAdapter(mealHistoryAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_history, container, false);
    }
}