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

import com.example.myfoodchoice.AdapterRecyclerView.RecipeViewHistoryAdapter;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class DietitianViewRecordRecipeFragment extends Fragment
{
    // todo: declare firebase
    static final String PATH_RECIPE = "Dietitian Recipe";
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.
    static final String TAG = "DietitianRecordRecipeManualType";

    DatabaseReference databaseReferenceCreateRecipe;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String dietitianID;

    // todo: declare UI
    RecyclerView recyclerViewRecordRecipe;

    RecipeViewHistoryAdapter recipeViewHistoryAdapter;

    ArrayList<Dish> recipeArrayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components.

        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            dietitianID = firebaseUser.getUid();
            // TODO: init database reference for user profile
            databaseReferenceCreateRecipe = firebaseDatabase.getReference(PATH_RECIPE).child(dietitianID);

            databaseReferenceCreateRecipe.addValueEventListener(valueViewRecordRecipeManualListener());
        }

        // set adapter here
        recyclerViewRecordRecipe = view.findViewById(R.id.recyclerViewRecordRecipe);
        recipeArrayList = new ArrayList<>();
        recipeViewHistoryAdapter = new RecipeViewHistoryAdapter(recipeArrayList);
        setAdapter();

    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueViewRecordRecipeManualListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshotChild : snapshot.getChildren())
                {
                    Dish meals = snapshotChild.getValue(Dish.class);
                    recipeArrayList.add(meals);
                }

                // notify change here.
                recipeViewHistoryAdapter.notifyItemChanged(recipeArrayList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error)
            {
                Log.d(TAG, "on error here: " + error.getMessage());
            }
        };
    }


    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        recyclerViewRecordRecipe.setLayoutManager(layoutManager);
        recyclerViewRecordRecipe.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecordRecipe.setAdapter(recipeViewHistoryAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_view_record_recipe, container, false);
    }
}