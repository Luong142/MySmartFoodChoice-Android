package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

public class DietitianSearchRecipeFragment extends Fragment
{
    static final String TAG = "DietitianSearchRecipeFragment";
    // todo: here is our plan, we use
    //  https://www.themealdb.com/api/json/v1/1/filter.php?c=Vegetarian, (category)
    //  https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian (cuisine),
    //  to view more details in another list of that dish
    //  www.themealdb.com/api/json/v1/1/search.php?s=BeaverTails
    // https://www.youtube.com/watch?v=tQ7V7iBg5zE&ab_channel=CodingSTUFF

    // todo: declare firebase here
    // todo: init firebase
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID, selectedSpinner;


    // todo: declare the ui here
    SearchView searchRecipeView;

    RecyclerView recipeRecyclerView;

    Spinner spinnerCategoryCuisine;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // todo: init firebase here.
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


        }

        // init UI here
        searchRecipeView = view.findViewById(R.id.searchRecipeView);
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        spinnerCategoryCuisine = view.findViewById(R.id.spinnerCategoryCuisine);

        // set spinner
        spinnerCategoryCuisine.setOnItemSelectedListener(onItemSelectedListener());

        // set adapter to recycler view.

    }

    @NonNull
    @Contract(pure = true)
    private AdapterView.OnItemSelectedListener onItemSelectedListener()
    {
        return new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedSpinner = parent.getItemAtPosition(position).toString();
                Log.d(TAG, selectedSpinner);

                // the purpose is to load the correct arraylist type
                // and to notify the adapter as well.
                switch (selectedSpinner)
                {
                    case "Category":

                        break;
                    case "Cuisine":

                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // nothing happen
                selectedSpinner = "Category";
            }
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_search_recipe, container, false);
    }
}