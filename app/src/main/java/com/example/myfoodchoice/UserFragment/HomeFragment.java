package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.myfoodchoice.R;

public class HomeFragment extends Fragment
{
    // TODO: declare UI components
    ProgressBar progressBar;

    TextView caloriesTextView, kcalModelStringTextView, progressTextView, checkInTextView;

    // TODO: add in one more button for taking photo I think.
    Button uploadPhoto, takePhoto;

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
        uploadPhoto = view.findViewById(R.id.uploadPhoto);

        // click button upload photo
        uploadPhoto.setOnClickListener(onUploadListener());
    }

    private View.OnClickListener onUploadListener()
    {
        return v ->
        {

        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}