package com.example.myfoodchoice.UserFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.R;

public class UserMealRecordFragment extends Fragment
{
    // TODO: declare
    ImageView cameraImageClick;

    private ActivityResultLauncher<Intent> cameraResultLauncher;

    private static final int CAMERA_REQUEST_CODE = 100;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init UI components
        cameraImageClick = view.findViewById(R.id.cameraImageClick);

        // Initialize the ActivityResultLauncher
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null)
                        {
                            Bundle extras = data.getExtras();
                            if (extras != null)
                            {
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                // Use the imageBitmap as needed, e.g., set it to an ImageView
                                cameraImageClick.setImageBitmap(imageBitmap);
                            }
                        }
                    }
                });

        // Set up the camera click listener
        cameraImageClick.setOnClickListener(v -> openCamera());

    }

    private void openCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Activity activity = getActivity();
        if (activity != null && takePictureIntent.resolveActivity(activity.getPackageManager()) != null)
        {
            cameraResultLauncher.launch(takePictureIntent);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_meal_record, container, false);
    }
}