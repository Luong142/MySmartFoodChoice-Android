package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnHealthTipsClickListener;
import com.example.myfoodchoice.AdapterRecyclerView.HealthTipsUserAdapter;
import com.example.myfoodchoice.ModelDietitian.HealthTips;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class DietitianViewHealthTipsFragment extends Fragment implements OnHealthTipsClickListener
{
    // todo: declare firebase components
    // private FirebaseAuth mAuth;
    // private FirebaseUser mUser;
    // private DatabaseReference mDatabaseReference;

    // TODO: declare components
    RecyclerView healthTipsRecyclerView;

    HealthTipsUserAdapter healthTipsUserAdapter;

    private ArrayList<HealthTips> healthTips;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components.



        // TODO: init UI components
        // Initialize the recipeList
        healthTips = new ArrayList<>();
        populateHealthTipsList();

        // for init recycle view component
        healthTipsRecyclerView = view.findViewById(R.id.healthTipsRecyclerView);
        healthTipsUserAdapter = new HealthTipsUserAdapter(healthTips, this);
        setAdapter();
        healthTipsRecyclerView.setVerticalScrollBarEnabled(true);

        // Set the adapter to the RecyclerView
        // recipeRecyclerView.setAdapter(adapter);

        // TODO: Populate the recipeList with your Recipe data


        // Notify the adapter that the data has changed
        // recipeItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHealthTipsClick(int position)
    {
        // TODO: implement onClick
        Toast.makeText(getContext(), "pls update this next", Toast.LENGTH_SHORT).show();
    }

    private void populateHealthTipsList()
    {
        HealthTips healthTips1 = new HealthTips("Healthy Eating",
                "Eat a balanced diet with a variety of foods to help you maintain a healthy weight.");
        HealthTips healthTips2 = new HealthTips("Healthy Sleep",
                "Get enough sleep to help you stay healthy and prevent obesity.");

        healthTips.add(healthTips1);
        healthTips.add(healthTips2);

    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        healthTipsRecyclerView.setLayoutManager(layoutManager);
        healthTipsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        healthTipsRecyclerView.setAdapter(healthTipsUserAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_view_health_tips, container, false);
    }
}