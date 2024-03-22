package com.example.myfoodchoice.UserFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;

import org.jetbrains.annotations.Contract;

public class UserHomeFragment extends Fragment
{
    // TODO: declare UI components
    ProgressBar progressBar;

    TextView caloriesTextView, kcalModelStringTextView, progressTextView, checkInTextView;

    // TODO: add in one more button for taking photo I think.
    Button logMealBtn, historyMealBtn;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init UI components
        checkInTextView = view.findViewById(R.id.checkInTextView);
        progressBar = view.findViewById(R.id.progressBar);
        progressTextView = view.findViewById(R.id.progressTextView);
        caloriesTextView = view.findViewById(R.id.caloriesNumTextView);
        kcalModelStringTextView = view.findViewById(R.id.kcalModelStringTextView);
        logMealBtn = view.findViewById(R.id.logMealBtn);
        historyMealBtn = view.findViewById(R.id.historyMealBtn);

        logMealBtn.setOnClickListener(onNavToLogMealListener());
        historyMealBtn.setOnClickListener(onNavToHistoryMealListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToLogMealListener()
    {
        return v ->
        {
            ((UserMainMenuActivity) requireActivity()).navigateToFragment(new UserLogMealFragment());
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onNavToHistoryMealListener()
    {
        return v ->
        {
            // TODO: this one is fragment so we need to allocate to the part in UserMainMenuActivity.
            ((UserMainMenuActivity) requireActivity()).navigateToFragment(new UserMealRecordFragment());
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUploadListener()
    {
        return v ->
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(Intent.createChooser(intent, "Select File"));
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }
}