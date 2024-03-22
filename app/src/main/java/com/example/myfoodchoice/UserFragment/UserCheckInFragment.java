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

import com.example.myfoodchoice.Adapter.CheckInDayAdapter;
import com.example.myfoodchoice.Adapter.RecipeItemAdapter;
import com.example.myfoodchoice.AdapterInterfaceListener.OnDailyCheckInListener;
import com.example.myfoodchoice.Model.Recipe;
import com.example.myfoodchoice.ModelUtilities.CheckInDay;
import com.example.myfoodchoice.R;

import java.util.ArrayList;


public class UserCheckInFragment extends Fragment implements OnDailyCheckInListener
{
    // TODO: declare components

    RecyclerView dayRecyclerView;

    CheckInDayAdapter dayAdapter;

    private ArrayList<CheckInDay> dayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components.



        // TODO: init UI components

        // Initialize the recipeList
        dayList = new ArrayList<>();
        populateDayList();

        // for recycle view
        dayRecyclerView = view.findViewById(R.id.checkInDaysRecyclerView);
        dayAdapter = new CheckInDayAdapter(dayList, this);
        setAdapter();
        dayRecyclerView.setVerticalScrollBarEnabled(true);

        // Set the adapter to the RecyclerView
        // dayRecyclerView.setAdapter(adapter);

        // TODO: Populate the recipeList with your Recipe data


        // Notify the adapter that the data has changed
        // recipeItemAdapter.notifyDataSetChanged();
        

    }

    @Override
    public void onCheckInClick(int position)
    {
        // TODO: implement onClick
        Toast.makeText(getContext(), "pls update this next: day click is: " + (position + 1),
                Toast.LENGTH_SHORT).show();
    }

    private void populateDayList()
    {
        CheckInDay checkInDay = new CheckInDay(R.drawable.check_in, "Day 1");
        CheckInDay checkInDay1 = new CheckInDay(R.drawable.check_in, "Day 2");
        CheckInDay checkInDay2 = new CheckInDay(R.drawable.check_in, "Day 3");
        CheckInDay checkInDay3 = new CheckInDay(R.drawable.check_in, "Day 4");
        CheckInDay checkInDay4 = new CheckInDay(R.drawable.check_in, "Day 5");
        CheckInDay checkInDay5 = new CheckInDay(R.drawable.check_in, "Day 6");
        CheckInDay checkInDay6 = new CheckInDay(R.drawable.check_in, "Day 7");

        dayList.add(checkInDay);
        dayList.add(checkInDay1);
        dayList.add(checkInDay2);
        dayList.add(checkInDay3);
        dayList.add(checkInDay4);
        dayList.add(checkInDay5);
        dayList.add(checkInDay6);
    }

    private void setAdapter() 
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        dayRecyclerView.setLayoutManager(layoutManager);
        dayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dayRecyclerView.setAdapter(dayAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_check_in, container, false);
    }
    
}