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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnClickExpandRecipeDetailListener;
import com.example.myfoodchoice.AdapterRecyclerView.MealMainHistoryAdapter;
import com.example.myfoodchoice.AdapterRecyclerView.RecipeSearchMainAdapter;
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

public class DietitianSearchRecipeFragment extends Fragment implements OnClickExpandRecipeDetailListener {
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

    String userID, selectedSpinner, searchQuery;

    ArrayList<RecipeCategories.RecipeCategory> recipeCategoryArrayList;

    ArrayList<RecipeCuisines.RecipeCuisine> recipeCuisineArrayList;

    RecipeSearchMainAdapter recipeSearchMainAdapter;

    // todo: call API

    RecipeCategories recipeCategories;

    RecipeCuisines recipeCuisines;

    FreeFoodRecipeCuisineAPI freeFoodRecipeCuisineAPI;

    FreeFoodRecipeCategoryAPI freeFoodRecipeCategoryAPI;

    Call<RecipeCategories> categoriesCall;

    Call<RecipeCuisines> cuisinesCall;

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

            // set database here
        }

        // init UI here
        searchRecipeView = view.findViewById(R.id.searchRecipeView);
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        spinnerCategoryCuisine = view.findViewById(R.id.spinnerCategoryCuisine);

        // set spinner
        spinnerCategoryCuisine.setOnItemSelectedListener(onItemSelectedListener());

        // set adapter to recycler view.
        recipeCategoryArrayList = new ArrayList<>(); // todo: based on the spinner we can switch to search.
        recipeCuisineArrayList = new ArrayList<>();

        // call API here
        freeFoodRecipeCuisineAPI = RetrofitFreeFoodClient.
        getRetrofitFreeInstance().create(FreeFoodRecipeCuisineAPI.class);

        freeFoodRecipeCategoryAPI = RetrofitFreeFoodClient.
        getRetrofitFreeInstance().create(FreeFoodRecipeCategoryAPI.class);

        // set search
        searchRecipeView.setOnQueryTextListener(onSearchRecipeTextListener());
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

                switch (selectedSpinner)
                {
                    case "Category":
                        recipeSearchMainAdapter = new RecipeSearchMainAdapter(recipeCuisineArrayList);
                        setAdapter();
                        freeFoodRecipeCategoryAPI.searchRecipeCategory(searchQuery).
                                enqueue(callBackCategoryResponseFromAPI());
                        return true;
                    case "Cuisine":
                        recipeSearchMainAdapter = new RecipeSearchMainAdapter(recipeCategoryArrayList);
                        setAdapter();
                        freeFoodRecipeCuisineAPI.searchRecipeCuisine(searchQuery).
                                enqueue(callBackCuisineResponseFromAPI());
                        return true;
                    default:
                        Log.d(TAG, "default");
                        break;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                /*
                searchQuery = newText;
                Log.d(TAG, searchQuery);

                switch (selectedSpinner)
                {
                    case "Category":
                        //freeFoodRecipeCategoryAPI.searchRecipeCategory(searchQuery).
                                //enqueue(callBackCategoryResponseFromAPI());
                        return true;
                    case "Cuisine":
                        //freeFoodRecipeCuisineAPI.searchRecipeCuisine(searchQuery).
                                //enqueue(callBackCuisineResponseFromAPI());
                        return true;
                    default:
                        Log.d(TAG, "default");
                        break;
                }
                return false;
                 */
                return false;
            }
        };
    }

    @Override
    public void onExpandRecipeDetail(int position)
    {
        // do nothing? when on click?
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
                // Log.d(TAG, selectedSpinner);
                // String searchRecipe = searchRecipeView.getQuery().toString().trim();

                // the purpose is to load the correct arraylist type
                // and to notify the adapter as well.
                switch (selectedSpinner)
                {
                    case "Category":
                        /*
                        freeFoodRecipeCategoryAPI.searchRecipeCategory(searchRecipe).
                                enqueue(callBackCategoryResponseFromAPI());
                         */
                        break;
                    case "Cuisine":
                        //freeFoodRecipeCuisineAPI.searchRecipeCuisine(searchRecipe).
                            //enqueue(callBackCuisineResponseFromAPI());
                        break;
                    default:
                        Log.d(TAG, "default");
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
                recipeCategories = response.body();

                if (recipeCategories != null)
                {
                    // this one working
                    // Log.d(TAG, "Response: " + recipeCategories);
                    recipeCategoryArrayList = recipeCategories.getRecipeCategory();

                    // add all
                    recipeCategoryArrayList.addAll(recipeCategories.getRecipeCategory());

                    Log.d(TAG, "Response: " + recipeCategoryArrayList);
                    // debug here

                    recipeSearchMainAdapter.notifyDataSetChanged();

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

    @NonNull
    @Contract(" -> new")
    private Callback<RecipeCuisines> callBackCuisineResponseFromAPI()
    {
        return new Callback<RecipeCuisines>()
        {

            @Override
            public void onResponse(@NonNull Call<RecipeCuisines> call, @NonNull Response<RecipeCuisines> response)
            {
                recipeCuisines = response.body();

                if (recipeCuisines != null)
                {
                    recipeCuisineArrayList = recipeCuisines.getRecipeCuisine();

                    // add all items
                    recipeCuisineArrayList.addAll(recipeCuisines.getRecipeCuisine());
                    Log.d(TAG, "Response: " + recipeCuisineArrayList);

                    if (recipeCuisineArrayList != null)
                    {
                        recipeSearchMainAdapter.notifyItemChanged(recipeCuisineArrayList.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeCuisines> call, @NonNull Throwable t)
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
        recipeRecyclerView.setAdapter(recipeSearchMainAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_search_recipe, container, false);
    }
}