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

import com.example.myfoodchoice.AdapterRecyclerView.RecipeViewHistoryAdapter;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;


public class UserViewRecipeFragment extends Fragment
{
    static final String TAG = "UserViewRecipeFragment";
    // todo: init firebase
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    static final String PATH_RECIPE = "Android Dietitian Recipe";

    DatabaseReference databaseReferenceCreateRecipe;

    Dish recipes;

    String userID;

    // TODO: declare components, search recipes, but also need to exclude the allergies.
    RecyclerView recipeRecyclerView;

    RecipeViewHistoryAdapter recipeViewHistoryAdapter;

    private ArrayList<Dish> recipeList;

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

            // set database here
            // TODO: init database reference for user profile
            databaseReferenceCreateRecipe = firebaseDatabase.getReference(PATH_RECIPE);

            // Initialize the recipeList
            recipeList = new ArrayList<>();

            databaseReferenceCreateRecipe.addChildEventListener(onChildViewRecipeListener());
        }

        // TODO: init UI components
        // for recycle view
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeViewHistoryAdapter = new RecipeViewHistoryAdapter(recipeList);
        setAdapter();
        recipeRecyclerView.setVerticalScrollBarEnabled(true);
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener onChildViewRecipeListener()
    {
        return new ChildEventListener()
        {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if (dataSnapshot.exists())
                    {
                        recipes = dataSnapshot.getValue(Dish.class);
                        if (recipes != null)
                        {
                            for (Dish.Meals meals : recipes.getMeals())
                            {
                                if (meals.getUserKey().equals(userID))
                                {
                                    recipeList.add(recipes);
                                    recipeViewHistoryAdapter.notifyItemInserted(recipeList.size() - 1);
                                }
                                else
                                {
                                    Log.d(TAG, "onChildAdded: " + meals);
                                }
                            }
                        }
                    }
                    else
                    {
                        Log.d(TAG, "onChildAdded: " + dataSnapshot);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    recipes = dataSnapshot.getValue(Dish.class);
                    if (recipes != null)
                    {
                        for (Dish.Meals meals : recipes.getMeals())
                        {
                            if (meals.getUserKey().equals(userID))
                            {
                                recipeList.add(recipes);
                                recipeViewHistoryAdapter.notifyItemChanged(recipeList.size() - 1);
                            }
                            else
                            {
                                Log.d(TAG, "onChildAdded: " + meals);
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    recipes = dataSnapshot.getValue(Dish.class);
                    if (recipes != null)
                    {
                        for (Dish.Meals meals : recipes.getMeals())
                        {
                            if (meals.getUserKey().equals(userID))
                            {
                                recipeList.add(recipes);
                                recipeViewHistoryAdapter.notifyItemRemoved(recipeList.size() - 1);
                            }
                            else
                            {
                                Log.d(TAG, "onChildAdded: " + meals);
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    recipes = dataSnapshot.getValue(Dish.class);
                    if (recipes != null)
                    {
                        for (Dish.Meals meals : recipes.getMeals())
                        {
                            if (meals.getUserKey().equals(userID))
                            {
                                recipeList.add(recipes);
                                recipeViewHistoryAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Log.d(TAG, "onChildAdded: " + meals);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        };
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recipeViewHistoryAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_view_recipe, container, false);
    }
}