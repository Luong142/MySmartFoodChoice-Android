package com.example.myfoodchoice.BusinessDietitianFragment;

import static com.example.myfoodchoice.BusinessDietitianActivity.DietitianMainMenuActivity.PATH_BUSINESS_PROFILE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnCreateRecipeFromSearchListener;
import com.example.myfoodchoice.AdapterRecyclerView.RecipeSearchCategoryMainAdapter;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;
import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodDetailAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCategoryAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitFreeFoodClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietitianSearchRecipeFragment extends Fragment implements OnCreateRecipeFromSearchListener {
    static final String TAG = "DietitianSearchRecipeFragment";


    // todo: here is our plan, we use
    //  https://www.themealdb.com/api/json/v1/1/filter.php?c=Vegetarian, (category)
    //  https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian (cuisine),
    //  to view more details in another list of that dish
    //  www.themealdb.com/api/json/v1/1/search.php?s=BeaverTails
    // todo: we need to use only category part to search

    // todo: init firebase
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    static final String PATH_RECIPE = "Android Dietitian Recipe";

    DatabaseReference databaseReferenceCreateRecipe, databaseReferenceCreateRecipeChild,
            databaseReferenceDietitianProfile;

    BusinessProfile businessProfile;

    String dietitianID, searchQuery, dietitianProfileImage, dietitianProfileInfo;

    ArrayList<RecipeCategories.RecipeCategory> recipeCategoryArrayList;

    RecipeSearchCategoryMainAdapter recipeSearchCategoryMainAdapter;

    // todo: call API

    RecipeCategories recipeCategories;

    UserProfile selectedUserProfile;

    Dish recipes;

    Bundle bundleFromView;

    FreeFoodDetailAPI freeFoodDetailAPI;

    FreeFoodRecipeCategoryAPI freeFoodRecipeCategoryAPI;

    // todo: declare the ui here
    SearchView searchRecipeView;

    RecyclerView recipeRecyclerView;
    int index;

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
            dietitianID = firebaseUser.getUid();

            // set database here
            // TODO: init database reference for user profile
            databaseReferenceCreateRecipe = firebaseDatabase.getReference(PATH_RECIPE).child(dietitianID);

            databaseReferenceDietitianProfile = firebaseDatabase.getReference
                    (PATH_BUSINESS_PROFILE).child(dietitianID);

            databaseReferenceDietitianProfile.addValueEventListener(onDietitianProfileListener());
        }

        bundleFromView = getArguments();
        if (bundleFromView != null)
        {
            selectedUserProfile = bundleFromView.getParcelable("selectedUserProfile");
            index = bundleFromView.getInt("index");

            // Log.d(TAG, "onViewCreated: " + selectedUserProfile);
        }

        // init UI here
        searchRecipeView = view.findViewById(R.id.searchRecipeView);
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);

        // set adapter to recycler view.
        recipeCategoryArrayList = new ArrayList<>(); // todo: based on the spinner we can switch to search.
        recipeSearchCategoryMainAdapter = new
                RecipeSearchCategoryMainAdapter(recipeCategoryArrayList, this);

        // call API here
        freeFoodDetailAPI = RetrofitFreeFoodClient.getRetrofitFreeInstance().create(FreeFoodDetailAPI.class);

        freeFoodRecipeCategoryAPI = RetrofitFreeFoodClient.
        getRetrofitFreeInstance().create(FreeFoodRecipeCategoryAPI.class);

        // set search
        searchRecipeView.setOnQueryTextListener(onSearchRecipeTextListener());
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener onDietitianProfileListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    businessProfile = snapshot.getValue(BusinessProfile.class);
                    if (businessProfile != null)
                    {
                        dietitianProfileImage = businessProfile.getProfileImageUrl();
                        dietitianProfileInfo = businessProfile.getDietitianInfo();
                    }
                }
                else
                {
                    Log.d(TAG, "onDataChange: No data found.");
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error)
            {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        };
    }

    @Override
    public void onCreateRecipeFromSearch(int position)
    {
        // todo: our plan is to let the dietitian to check user profile and
        //  then when they click on that user profile, it should open up this fragment and
        //  let the dietitian to search for that suitable recipe.
        // get the string name of food whenever it is clicked.

        // remove the recipe from search
        recipeCategoryArrayList.remove(position);
        recipeSearchCategoryMainAdapter.notifyItemRemoved(position);

        // todo: we can improve this by warning the dietitian based on user profile
        //  (allergic, diet type, and health conditions)
        freeFoodDetailAPI.searchMealByName(recipeCategoryArrayList.get(position).getStrMeal())
                .enqueue(new Callback<Dish>()
                {
                    @Override
                    public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response)
                    {
                        if (response.isSuccessful())
                        {
                            recipes = response.body();

                            if (recipes != null)
                            {
                                // Log.d(TAG, "onResponse: " + recipes.getMeals().get(0).getUserKey());

                                Dish filteredRecipes = filterEmptyStrings(recipes);
                                // Log.d(TAG, "onResponse: " + filteredRecipes);
                                filteredRecipes.getMeals().get(0).setUserKey(selectedUserProfile.getKey());
                                filteredRecipes.getMeals().get(0).setDietitianKey(dietitianID);
                                filteredRecipes.getMeals().get(0).setDietitianProfileImage(dietitianProfileImage);
                                filteredRecipes.getMeals().get(0).setDietitianInfo(dietitianProfileInfo);

                                // Log.d(TAG, "onResponse: " + filteredRecipes.getMeals().get(0).getUserKey());

                                // set database here
                                databaseReferenceCreateRecipeChild = databaseReferenceCreateRecipe.push();
                                databaseReferenceCreateRecipeChild.setValue(filteredRecipes)
                                        .addOnCompleteListener(onCompletedSearchRecipeListener());
                            }
                        }
                        else
                        {
                            Log.d(TAG, "onResponse: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t)
                    {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    @Contract("_ -> param1")
    public static <T> T filterEmptyStrings(@NonNull T object)
    {
        try
        {
            for (Field field : object.getClass().getDeclaredFields())
            {
                field.setAccessible(true); // Make private fields accessible
                Object value = field.get(object);
                if (value instanceof String && ((String) value).isEmpty())
                {
                    field.set(object, null); // Set empty strings to null or any default value
                }
            }
        }
        catch (IllegalAccessException e)
        {
            Log.d(TAG, "IllegalAccessException: " + e);
        }
        return object;
    }


    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompletedSearchRecipeListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                // fixme: there might be a potential error here.
                Toast.makeText(requireContext(), "Recipe created successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireContext(), "Error" +
                        Objects.requireNonNull(task.getException()), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private SearchView.OnQueryTextListener onSearchRecipeTextListener()
    {
        return new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchQuery = query;
                // Log.d(TAG, searchQuery);
                // we need to add one listener for creating a new recipe based on one user profile.
                setAdapter();
                freeFoodRecipeCategoryAPI.searchRecipeCategory(searchQuery).
                        enqueue(callBackCategoryResponseFromAPI());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private Callback<RecipeCategories> callBackCategoryResponseFromAPI()
    {
        return new Callback<RecipeCategories>()
        {
            @Override
            public void onResponse(@NonNull Call<RecipeCategories> call,
                                   @NonNull Response<RecipeCategories> response)
            {
                if (response.isSuccessful())
                {
                    recipeCategories = response.body();
                    if (recipeCategories != null && recipeCategories.getRecipeCategory()
                            != null &&
                            !recipeCategories.getRecipeCategory().isEmpty())
                    {
                        // Assuming recipeCategoryArrayList is already initialized
                        recipeCategoryArrayList.addAll(recipeCategories.getRecipeCategory());
                        recipeSearchCategoryMainAdapter.notifyItemChanged(recipeCategoryArrayList.size() - 1);
                    }
                    else
                    {
                        Toast.makeText(requireContext(),
                                "No category found.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(requireContext(),
                            "Error:  " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeCategories> call,
                                  @NonNull Throwable t)
            {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        };
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        // Log.d(TAG, "Before setting layout manager: " + (recipeRecyclerView == null));
        recipeRecyclerView.setLayoutManager(layoutManager);
        // Log.d(TAG, "After setting layout manager: " + (recipeRecyclerView == null));
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recipeSearchCategoryMainAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_search_recipe, container, false);
    }
}