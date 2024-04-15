package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnRecipeItemClickListener;
import com.example.myfoodchoice.AdapterRecyclerView.RecipeItemAdapter;
import com.example.myfoodchoice.ModelBusiness.Recipe;
import com.example.myfoodchoice.R;

import java.util.ArrayList;


public class UserRecipeFragment extends Fragment implements OnRecipeItemClickListener
{
    // TODO: declare components, search recipes, but also need to exclude the allergies.
    EditText recipeSearch;

    RecyclerView recipeRecyclerView;

    RecipeItemAdapter recipeItemAdapter;

    private ArrayList<Recipe> recipeList;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components.



        // TODO: init UI components
        recipeSearch = view.findViewById(R.id.searchRecipeEditText);

        // Initialize the recipeList
        recipeList = new ArrayList<>();
        populateRecipeList();

        // for recycle view
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        recipeItemAdapter = new RecipeItemAdapter(recipeList, this);
        setAdapter();
        recipeRecyclerView.setVerticalScrollBarEnabled(true);

        // Set the adapter to the RecyclerView
        // recipeRecyclerView.setAdapter(adapter);

        // TODO: Populate the recipeList with your Recipe data

        // Notify the adapter that the data has changed
        // recipeItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecipeItemClick(int position)
    {
        // TODO: implement onClick
        Toast.makeText(getContext(), "pls update this next", Toast.LENGTH_SHORT).show();
    }

    private void populateRecipeList()
    {
        recipeList.add(new Recipe(R.drawable.tomato, "Tomato", "A fruit"));
        recipeList.add(new Recipe(R.drawable.tomato, "Tomato", "A fruit"));
        recipeList.add(new Recipe(R.drawable.tomato, "Tomato", "A fruit"));
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recipeItemAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_recipe, container, false);
    }
}