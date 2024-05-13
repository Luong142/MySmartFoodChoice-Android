package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
    static final String PATH_RECIPE = "Android Dietitian Recipe";
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.
    static final String TAG = "DietitianCreateRecipeFragment";

    DatabaseReference databaseReferenceCreateRecipe, databaseReferenceCreateRecipeChild;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    Dish recipe;

    ArrayList<String> ingredientArrayList;

    UserProfile selectedUserProfile;

    Bundle bundleStore;

    int index;

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

        // todo: this will help not to push the content up
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
            ingredientArrayList = new ArrayList<>();
        }

        // todo: get the selected user profile
        bundleStore = getArguments();

        if (bundleStore != null)
        {
            selectedUserProfile = bundleStore.getParcelable("selectedUserProfile");
            index = bundleStore.getInt("index");

            // todo: done testing
            // todo: the purpose is to get the user key and it should show to the correct user page.
            // Log.d(TAG, "onViewCreated: " + selectedUserProfile);
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
                return;
            }

            if (recipeInstructionsText.getText().toString().isEmpty())
            {
                recipeInstructionsText.setError("Please enter recipe instructions");
                return;
            }

            if (ingredientArrayList.isEmpty())
            {
                ingredientText.setError("Please enter ingredient");
                return;
            }

            if (recipe == null)
            {
                recipe = new Dish();
            }

            recipe.setMeals(new ArrayList<>());

            Dish.Meals meal = new Dish.Meals();

            recipeName = recipeNameText.getText().toString().trim();
            recipeInstruction = recipeInstructionsText.getText().toString().trim();

            // set the user key
            meal.setUserKey(selectedUserProfile.getKey());
            meal.setStrArea(cuisineSpinner.getSelectedItem().toString().trim());
            meal.setStrCategory(categorySpinner.getSelectedItem().toString().trim());
            meal.setStrInstructions(recipeInstruction.trim());
            meal.setStrMeal(recipeName.trim());
            ingredientArrayList.trimToSize();
            meal.setIngredientsManual(ingredientArrayList);
            meal.setDietitianKey(dietitianID);

            recipe.getMeals().add(meal);

            // set value for database firebase.
            databaseReferenceCreateRecipeChild = databaseReferenceCreateRecipe.push();
            databaseReferenceCreateRecipeChild.setValue(recipe).addOnCompleteListener(onCompleteCreateRecipeListener());
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

                // clear the ingredientArrayList and notify the adapter
                if (!ingredientArrayList.isEmpty())
                {
                    ingredientArrayList.clear();
                    ingredientRecipeAdapter.notifyDataSetChanged();
                }

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
            if (recipeNameText.getText().toString().isEmpty() &&
                    recipeInstructionsText.getText().toString().isEmpty() &&
                    ingredientText.getText().toString().isEmpty()
                )
            {
                return;
            }

            recipeNameText.setText("");
            recipeInstructionsText.setText("");
            ingredientText.setText("");
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
        ingredientRecipeAdapter.notifyItemRemoved(position);
        ingredientRecipeAdapter.notifyItemRangeChanged(position, ingredientArrayList.size() - position);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_create_recipe, container, false);
    }
}