package com.example.myfoodchoice.BusinessDietitianFragment;

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
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCuisines;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCategoryAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCuisineAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitFreeFoodClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

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

    String userID, searchQuery;

    ArrayList<RecipeCategories.RecipeCategory> recipeCategoryArrayList;

    RecipeSearchCategoryMainAdapter recipeSearchCategoryMainAdapter;

    // todo: call API

    RecipeCategories recipeCategories;

    RecipeCuisines recipeCuisines;

    FreeFoodRecipeCuisineAPI freeFoodRecipeCuisineAPI;

    FreeFoodRecipeCategoryAPI freeFoodRecipeCategoryAPI;

    // todo: declare the ui here
    SearchView searchRecipeView;

    RecyclerView recipeRecyclerView;

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

            // set database here
        }

        // init UI here
        searchRecipeView = view.findViewById(R.id.searchRecipeView);
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);

        // set adapter to recycler view.
        recipeCategoryArrayList = new ArrayList<>(); // todo: based on the spinner we can switch to search.

        // call API here
        freeFoodRecipeCuisineAPI = RetrofitFreeFoodClient.
        getRetrofitFreeInstance().create(FreeFoodRecipeCuisineAPI.class);

        freeFoodRecipeCategoryAPI = RetrofitFreeFoodClient.
        getRetrofitFreeInstance().create(FreeFoodRecipeCategoryAPI.class);

        // set search
        searchRecipeView.setOnQueryTextListener(onSearchRecipeTextListener());
    }

    @Override
    public void onCreateRecipeFromSearch(int position)
    {
        // todo: our plan is to let the dietitian to check user profile and
        //  then when they click on that user profile, it should open up this fragment and
        //  let the dietitian to search for that suitable recipe.




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
                recipeSearchCategoryMainAdapter = new
                        RecipeSearchCategoryMainAdapter(recipeCategoryArrayList);
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