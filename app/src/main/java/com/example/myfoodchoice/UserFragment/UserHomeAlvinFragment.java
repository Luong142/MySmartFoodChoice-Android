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

import org.jetbrains.annotations.Contract;


public class UserHomeAlvinFragment extends Fragment
{
    // todo: declare button UI component
    Button logMyMealBtn, mealHistoryBtn;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // todo: init button UI component
        logMyMealBtn = view.findViewById(R.id.logMealBtn);
        mealHistoryBtn = view.findViewById(R.id.mealHistoryBtn);

        logMyMealBtn.setOnClickListener(onLogMyMealListener());
        mealHistoryBtn.setOnClickListener(onMealHistoryListener());

    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onMealHistoryListener()
    {
        return v ->
        {
            UserMealHistoryFragment userMealHistoryFragment = new UserMealHistoryFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userMealHistoryFragment)
                    .commit();
        };
    }

    @Nullable
    @Contract(pure = true)
    private View.OnClickListener onLogMyMealListener()
    {
        return v ->
        {
            UserLogMealFragment userLogMealFragment = new UserLogMealFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userLogMealFragment)
                    .commit();
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home_alvin, container, false);
    }
}