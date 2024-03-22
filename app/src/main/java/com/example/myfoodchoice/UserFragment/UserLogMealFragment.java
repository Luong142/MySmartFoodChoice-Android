package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;

import org.jetbrains.annotations.Contract;


public class UserLogMealFragment extends Fragment
{
    // TODO: declare UI component
    Button morningBtn, afternoonBtn, nightBtn;

    String time;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init UI components
        morningBtn = view.findViewById(R.id.morningButton);
        afternoonBtn = view.findViewById(R.id.afternoonButton);
        nightBtn = view.findViewById(R.id.nightButton);

        morningBtn.setOnClickListener(onNavToUserMealRecordMorningListener());
        afternoonBtn.setOnClickListener(onNavToUserMealRecordAfternoonListener());
        nightBtn.setOnClickListener(onNavToUserMealRecordNightListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordNightListener()
    {
        return v ->
        {
            time = "Morning";
            UserMainMenuActivity activity = (UserMainMenuActivity) requireActivity();
            activity.navigateToFragment(new UserMealRecordFragment());
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordAfternoonListener()
    {
        return v ->
        {
            time = "Afternoon";
            UserMainMenuActivity activity = (UserMainMenuActivity) requireActivity();
            activity.navigateToFragment(new UserMealRecordFragment());
        };
    }

    // TODO: for now it should go to meal record?
    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToUserMealRecordMorningListener()
    {
        return v ->
        {
            time = "Morning";
            UserMainMenuActivity activity = (UserMainMenuActivity) requireActivity();
            activity.navigateToFragment(new UserMealRecordFragment());
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_log_meal, container, false);
    }
}