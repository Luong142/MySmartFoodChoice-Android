package com.example.myfoodchoice.UserFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.CaloriesNinjaAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitClient;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.example.myfoodchoice.ml.Model;

import org.jetbrains.annotations.Contract;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends Fragment
{
    final static String TAG = "UserHomeFragment";
    int imageSize;

    // TODO: declare UI components
    ProgressBar progressBar;

    ImageView foodImage;

    TextView caloriesTextView, kcalModelStringTextView, progressTextView, checkInTextView;

    // TODO: add in one more button for taking photo I think.
    Button logMealBtn, historyMealBtn, takePhotoBtn, uploadPhotoBtn;
    ActivityResultLauncher<Intent> uploadPhotoactivityResultLauncher;

    ActivityResultLauncher<Intent> takePhotoActivityResultLauncher;

    ActivityResultLauncher<String[]> requestPermissionLauncher;

    // for image file

    Bitmap image;

    int dimension;

    // calling calories ninja API
    private CaloriesNinjaAPI caloriesNinjaAPI;

    private Call<FoodItem> call;

    private FoodItem foodItem;

    String foodName;

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
        takePhotoBtn = view.findViewById(R.id.takePhotoBtn);
        uploadPhotoBtn = view.findViewById(R.id.uploadPhotoBtn);
        foodImage = view.findViewById(R.id.foodPhoto);

        // todo: set onclick here
        logMealBtn.setOnClickListener(onNavToLogMealListener());
        historyMealBtn.setOnClickListener(onNavToHistoryMealListener());
        uploadPhotoBtn.setOnClickListener(onUploadPhotoListener());
        takePhotoBtn.setOnClickListener(onTakePhotoListener());

        // todo: init API
        caloriesNinjaAPI = RetrofitClient.getRetrofitInstance().create(CaloriesNinjaAPI.class);

        imageSize = 224; // important?

        // for camera activity
        takePhotoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null)
                        {
                            Bundle extras = data.getExtras();
                            if (extras != null)
                            {
                                image = (Bitmap) extras.get("data");
                                if (image != null)
                                {
                                    dimension = Math.min(image.getWidth(), image.getHeight());
                                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                                    // Set the Bitmap to the ImageView
                                    foodImage.setImageBitmap(image);

                                    // I don't know what is this?
                                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                                    classifyImage(image);
                                }
                            }
                        }
                    }
                });

        // for upload photo
        uploadPhotoactivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null)
                        {
                            Uri selectedImageUri = data.getData();
                            try {
                                // Decode the URI to a Bitmap
                                image = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),
                                        selectedImageUri);
                                if (image != null)
                                {
                                    // Set dimension
                                    dimension = Math.min(image.getWidth(), image.getHeight());
                                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                                    // Set the Bitmap to the ImageView
                                    foodImage.setImageBitmap(image);

                                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                                    classifyImage(image);
                                }
                            }
                            catch (IOException e)
                            {
                                Log.d(TAG, "error here: " + e);
                            }
                        }
                    }
                }
        );


        // for permission
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions ->
                {
                    if (Boolean.TRUE.equals(permissions.get(Manifest.permission.CAMERA)))
                    {
                        // Permission granted, can now start the camera intent
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                        {
                            takePhotoActivityResultLauncher.launch(cameraIntent);
                        }
                    }
                    else
                    {
                        // denied, show toast
                        Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void classifyImage(@NonNull Bitmap image) // todo: algo using tensorflow lite to label image.
    {
        try {
            Model model = Model.newInstance(requireActivity().getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            // todo: a bit hard to do later we will settle this.
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 * imageSize  * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // what is this code about, using double for loop to put the float value inside of that byteBuffer.
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++)
            {
                for (int j = 0; j < imageSize; j++)
                {
                    int val = intValues[pixel++];
                    // todo: predefined value to normalize the extraction of RGB.

                    // todo: RED
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));

                    // todo: GREEN
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));

                    // todo: BLUE
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++)
            {
                if(confidences[i] > maxConfidence)
                {
                    // todo: pls note that confidence is currently not being used, only maxPos.
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Nasi lemak", "Kaya Toast", "hainanese curry rice",
                    "Ice cream", "curry puff", "eggs"}; // fixme: eggs need to remove, we can add Laksa

            // result.setText(classes[maxPos]);
            // todo: need to test image recognition algo.
            foodName = classes[maxPos];
            Log.d(TAG, "The dish name is classified as: " + foodName);

            // call API, and get result with that model class.
            call = caloriesNinjaAPI.getFoodItem(foodName);
            // todo: uncomment this part below to do get calories info and more from this API.
            // call.enqueue(callBackResponseFromAPI());

            // todo: input from user when search for recipe,
            // todo: if the "ingredients" contains the "allergies", we can show warning contains "nuts" to user, best option.
            // todo: 3 options

            StringBuilder s = new StringBuilder();
            for(int i = 0; i < classes.length; i++)
            {
                s.append(String.format(Locale.ROOT, "%s: %.1f%%\n", classes[i], confidences[i] * 100));
            }
            // confidence.setText(s);
            Log.d(TAG, "The dish info is: \n" + s);

            // Releases model resources if no longer used.
            model.close();
        }
        catch (IOException e)
        {
            // TODO Handle the exception
            Log.d(TAG, "ClassifyImage: " + e.getMessage());
        }
    }

    @NonNull
    @Contract(" -> new")
    private Callback<FoodItem> callBackResponseFromAPI()
    {
        return new Callback<FoodItem>()
        {
            @Override
            public void onResponse(@NonNull Call<FoodItem> call, @NonNull Response<FoodItem> response)
            {
                if (response.isSuccessful())
                {
                    foodItem = response.body();
                    if (foodItem != null)
                    {
                        Log.d(TAG, "onResponse: " + foodItem);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodItem> call, @NonNull Throwable t)
            {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private View.OnClickListener onTakePhotoListener()
    {
        return v ->
        {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA});
            }
            else
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                PackageManager packageManager = requireContext().getPackageManager();

                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    takePhotoActivityResultLauncher.launch(cameraIntent);
                }
            }
        };
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
            ((UserMainMenuActivity) requireActivity()).navigateToFragment(new UserLogMealSearchFoodFragment());
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUploadPhotoListener()
    {
        return v ->
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            uploadPhotoactivityResultLauncher.launch(Intent.createChooser(intent, "Select File"));
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