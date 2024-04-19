package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnActionIngredientListener;
import com.example.myfoodchoice.AdapterRecyclerView.IngredientRecipeAdapter;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;


public class DietitianCreateRecipeFragment extends Fragment implements OnActionIngredientListener
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

    ArrayList<String> ingredientArrayList;

    // todo: declare UI components
    EditText recipeNameText, recipeInstructionsText, ingredientText;

    Spinner cuisineSpinner, categorySpinner;
    String dietitianID, recipeName, recipeInstruction;
    FloatingActionButton addIngredientBtn, clearRecipeBtn;

    Button createRecipeBtn;

    RecyclerView ingredientRecyclerView;

    IngredientRecipeAdapter ingredientRecipeAdapter;

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
            ingredientArrayList = new ArrayList<>();
        }

        // todo: init UI components
        recipeNameText = view.findViewById(R.id.recipeNameEditText);
        recipeInstructionsText = view.findViewById(R.id.instructionsEditText);
        ingredientText = view.findViewById(R.id.ingredientsEditText);
        cuisineSpinner = view.findViewById(R.id.cuisineSpinner);
        categorySpinner = view.findViewById(R.id.categorySpinner);


        // set button
        addIngredientBtn = view.findViewById(R.id.addIngredientBtn);
        clearRecipeBtn = view.findViewById(R.id.clearTextRecipeBtn);
        createRecipeBtn = view.findViewById(R.id.createRecipeBtn);

        addIngredientBtn.setOnClickListener(onAddIngredientListener());
        clearRecipeBtn.setOnClickListener(onClearRecipeListener());
        createRecipeBtn.setOnClickListener(onCreateRecipeListener());

        // set recycler view
        ingredientRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        ingredientRecipeAdapter = new IngredientRecipeAdapter(ingredientArrayList, this);
        ingredientRecyclerView.setAdapter(ingredientRecipeAdapter);
        ingredientRecyclerView.setVerticalScrollBarEnabled(true);
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateRecipeListener()
    {
        return v ->
        {
            if (recipeNameText.getText().toString().isEmpty())
            {
                recipeNameText.setError("Please enter recipe name");
            }

            if (recipeInstructionsText.getText().toString().isEmpty())
            {
                recipeInstructionsText.setError("Please enter recipe instructions");
            }

            recipeName = recipeNameText.getText().toString().trim();
            recipeInstruction = recipeInstructionsText.getText().toString().trim();

            recipe.setStrArea(cuisineSpinner.getSelectedItem().toString().trim());
            recipe.setStrCategory(categorySpinner.getSelectedItem().toString().trim());
            recipe.setStrInstructions(recipeInstruction);
            recipe.setStrMeal(recipeName);
            recipe.setIngredients(ingredientArrayList);

            // set value for database firebase.
            databaseReferenceCreateRecipe.setValue(recipe).addOnCompleteListener(onCompleteCreateRecipeListener());

        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteCreateRecipeListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                recipeNameText.setText("");
                recipeInstructionsText.setText("");
                ingredientText.setText("");
                Toast.makeText(getContext(), "Recipe created successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Error" +
                        Objects.requireNonNull(task.getException()), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onAddIngredientListener()
    {
        return v ->
        {
            if (ingredientText.getText().toString().isEmpty())
            {
                ingredientText.setError("Please enter ingredient.");
                return;
            }

            ingredientArrayList.add(ingredientText.getText().toString().trim());
            ingredientRecipeAdapter.notifyItemChanged(ingredientArrayList.size() - 1);
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onClearRecipeListener()
    {
        return v ->
        {
            // clear everything
            recipeNameText.setText("");
            recipeInstructionsText.setText("");
            recipeName = "";
            recipeInstruction = "";
            recipe = new Dish.Meals();
            ingredientArrayList.clear();
            ingredientRecipeAdapter.notifyItemChanged(ingredientArrayList.size() - 1);
        };
    }

    @Override
    public void onDeleteIngredient(int position)
    {
        if (ingredientArrayList.isEmpty())
        {
            return;
        }
        ingredientArrayList.remove(position);
        ingredientRecipeAdapter.notifyItemChanged(position);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_create_recipe, container, false);
    }
}