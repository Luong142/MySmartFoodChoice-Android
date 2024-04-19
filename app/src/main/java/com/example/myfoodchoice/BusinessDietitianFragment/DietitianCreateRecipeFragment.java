package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;


public class DietitianCreateRecipeFragment extends Fragment
{
    static final String PATH_RECIPE = "Dietitian Recipe";
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.

    DatabaseReference databaseReferenceCreateRecipe;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    Dish.Meals recipe;

    // todo: declare UI components
    EditText recipeName, recipeInstructions;

    Spinner cuisineSpinner, categorySpinner;
    String dietitianID;
    FloatingActionButton addIngredientBtn, clearRecipeBtn;

    Button createRecipeBtn;

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

            recipe = new Dish.Meals();
        }

        // todo: init UI components
        recipeName = view.findViewById(R.id.recipeNameEditText);
        recipeInstructions = view.findViewById(R.id.instructionsEditText);
        cuisineSpinner = view.findViewById(R.id.cuisineSpinner);
        categorySpinner = view.findViewById(R.id.categorySpinner);


        // set button
        addIngredientBtn = view.findViewById(R.id.addIngredientBtn);
        clearRecipeBtn = view.findViewById(R.id.clearTextRecipeBtn);
        createRecipeBtn = view.findViewById(R.id.createRecipeBtn);

        addIngredientBtn.setOnClickListener(onAddIngredientListener());
        clearRecipeBtn.setOnClickListener(onClearRecipeListener());
        createRecipeBtn.setOnClickListener(onCreateRecipeListener());

    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateRecipeListener()
    {
        return v ->
        {

        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onAddIngredientListener()
    {
        return v ->
        {

        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onClearRecipeListener()
    {
        return v ->
        {
            recipeName.setText("");
            recipeInstructions.setText("");
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_create_recipe, container, false);
    }
}