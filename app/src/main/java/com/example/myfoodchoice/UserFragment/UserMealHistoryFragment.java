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
import com.example.myfoodchoice.AdapterRecyclerView.MealMainHistoryAdapter;
import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
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
import java.util.List;
import java.util.Objects;

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

    final static String PATH_DAILY_FOOD_INTAKE = "Meals"; // fixme:  the path need to access daily globalMeal.

    String userID;

    // TODO: declare UI components
    RecyclerView mealHistoryRecyclerView, mealDetailRecyclerView;

    MealMainHistoryAdapter mealMainHistoryAdapter;

    TabLayout timeTabLayout;

    ArrayList<Meal> mealArrayList;

    boolean showMorningMeal, showAfternoonMeal, showNightMeal, showAllMeal;
    
    Meal globalMeal;

    FoodItem foodItem;

    List<FoodItem.Item> items;

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
                    firebaseDatabase.getReference(PATH_DAILY_FOOD_INTAKE).child(userID);
            databaseReferenceMeals.addChildEventListener(valueChildMealEventListener());
        }

        // todo: init UI components
        timeTabLayout = view.findViewById(R.id.timeTabLayout);
        timeTabLayout.addOnTabSelectedListener(onTabSelectedListener());

        // need to set this recycler view.
        mealArrayList = new ArrayList<>();
        mealHistoryRecyclerView = view.findViewById(R.id.mealRecyclerView);
        mealMainHistoryAdapter = new MealMainHistoryAdapter(mealArrayList, this);
        setAdapter();
    }

    // fixme: there is a bug that the meal can be duplicated when it read.
    // fixme: the problem is that array list has been replaced not correctly.

    @NonNull
    @Contract(pure = true)
    private TabLayout.OnTabSelectedListener onTabSelectedListener()
    {
        return new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                // todo: to view item based on the time.
                showMorningMeal = Objects.equals(tab.getText(), "Morning");
                showAfternoonMeal = Objects.equals(tab.getText(), "Afternoon");
                showNightMeal = Objects.equals(tab.getText(), "Night");
                showAllMeal = Objects.equals(tab.getText(), "All");

                // todo: to show the data?
                filterAndUpdateMeals();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
            }
        };
    }

    private void filterAndUpdateMeals()
    {
        ArrayList<Meal> filteredMeals = new ArrayList<>();

        for (Meal meal : mealArrayList)
        {
            if (showMorningMeal && meal.isMorning())
            {
                filteredMeals.add(meal);
            }

            if (showAfternoonMeal && meal.isAfternoon())
            {
                filteredMeals.add(meal);
            }

            if (showNightMeal && meal.isNight())
            {
                filteredMeals.add(meal);
            }

            if (showAllMeal)
            {
                filteredMeals.add(meal);
            }
        }
        // Update the adapter with the filtered list
        mealMainHistoryAdapter.updateMeals(filteredMeals);
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
                globalMeal = snapshot.getValue(Meal.class);

                if (globalMeal == null)
                {
                    Log.d(TAG, "Meal is null error here! ");
                    return;
                }

                foodItem = globalMeal.getDishes();
                items = foodItem.getItems();

                // todo: to show the data?
                mealArrayList.add(globalMeal);
                mealMainHistoryAdapter.notifyItemInserted(mealArrayList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                globalMeal = snapshot.getValue(Meal.class);

                if (globalMeal == null)
                {
                    Log.d(TAG, "Meal is null error here! ");
                    return;
                }

                foodItem = globalMeal.getDishes();
                items = foodItem.getItems();

                // Find the index of the changed meal in the list
                int index = -1;
                for (int i = 0; i < mealArrayList.size(); i++)
                {
                    if (mealArrayList.get(i).getKey().equals(globalMeal.getKey()))
                    { // Assuming Meal has a getId() method
                        index = i;
                        break;
                    }
                }

                // If the meal is found in the list, update it
                if (index != -1)
                {
                    mealArrayList.set(index, globalMeal); // Update the meal at the found index
                    mealMainHistoryAdapter.notifyItemChanged(index); // Notify the adapter of the change
                }
                else
                {
                    // If the meal is not found in the list, you might want to add it or handle this case differently
                    Log.d(TAG, "Meal not found in the list. Consider adding it.");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                globalMeal = snapshot.getValue(Meal.class);

                if (globalMeal == null)
                {
                    Log.d(TAG, "Meal is null error here! ");
                    return;
                }

                foodItem = globalMeal.getDishes();
                items = foodItem.getItems();

                // todo: to show the data?
                mealArrayList.remove(globalMeal);
                mealMainHistoryAdapter.notifyItemRemoved(mealArrayList.size() - 1);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                Meal globalMeal = snapshot.getValue(Meal.class);
                if (globalMeal != null)
                {
                    Log.d(TAG, "onChildAdded: " + globalMeal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, error.getMessage());
            }
        };
    }

    @Override
    public void onClickMeal(int position)
    {
        // todo: if click the entire item, what we want to do with it?
        // todo: our plan is to make an expandable recycler view to open.
        // do nothing here.
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        mealHistoryRecyclerView.setLayoutManager(layoutManager);
        mealHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mealHistoryRecyclerView.setAdapter(mealMainHistoryAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_history, container, false);
    }
}