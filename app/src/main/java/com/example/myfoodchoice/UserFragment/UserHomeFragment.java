package com.example.myfoodchoice.UserFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.R;

import org.jetbrains.annotations.Contract;

public class UserHomeFragment extends Fragment
{
    // TODO: declare UI components
    ProgressBar progressBar;

    TextView caloriesTextView, kcalModelStringTextView, progressTextView, checkInTextView;

    // TODO: add in one more button for taking photo I think.
    Button uploadPhoto, takePhoto;
    ActivityResultLauncher<Intent> activityResultLauncher;

    Uri selectedImageUri;

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

        // init activity for gallery part.
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null)
                        {
                            // get the URI of the selected image
                            selectedImageUri = data.getData();

                        }
                    }
                }
        );

        /* TODO: here is the plan
        * we use two APIs food analysis API and OpenCV API
        *
        *
        *
        *
        *
        * */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }
}