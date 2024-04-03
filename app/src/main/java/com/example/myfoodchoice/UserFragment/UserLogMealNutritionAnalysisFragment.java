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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.CaloriesNinjaAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitClient;
import com.example.myfoodchoice.UserActivity.UserLogMealActivity;
import com.example.myfoodchoice.ml.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class UserLogMealNutritionAnalysisFragment extends Fragment
{
    final static String TAG = "UserLogMealNutritionFragment";
    int imageSize;

    // TODO: declare UI components
    ProgressBar progressBarCalories, progressBarCholesterol, progressBarSugar, progressBarSalt;

    ImageView foodImage;

    TextView progressCholesterolTextView,
            progressSugarTextView,
            progressSaltTextView,
            progressCaloriesTextView,
            checkInTextView
            , foodNameTextView;

    // TODO: add in one more button for taking photo I think.
    FloatingActionButton takePhotoBtn, uploadPhotoBtn;

    CardView logMealBtn;

    ActivityResultLauncher<Intent> uploadPhotoactivityResultLauncher;

    ActivityResultLauncher<Intent> takePhotoActivityResultLauncher;

    ActivityResultLauncher<String[]> requestPermissionLauncher;

    // for image file

    Bitmap image;

    int dimension;

    // calling calories ninja API
    private CaloriesNinjaAPI caloriesNinjaAPI;

    private FoodItem foodItem;

    String foodName;

    // firebase
    DatabaseReference databaseReferenceUserProfile;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID, gender;

    double maxCalories, maxCholesterol, maxSugar, maxSalt;

    boolean isDiabetes, isHighBloodPressure, isHighCholesterol;

    Intent intentNavToLogMeal;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            gender = "Male"; // fixme: by default value
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile =
                    firebaseDatabase.getReference(PATH_USERPROFILE).child(userID);

            databaseReferenceUserProfile.addValueEventListener(onGenderHealthValueListener());
        }

        // TODO: init UI components
        checkInTextView = view.findViewById(R.id.checkInTextView);
        foodNameTextView = view.findViewById(R.id.foodName);

        // fixme: should be matched with the ID.
        progressBarCalories = view.findViewById(R.id.progressBarCalories);
        progressCaloriesTextView = view.findViewById(R.id.progressCaloriesTextView); // id name incorrect

        progressBarCholesterol = view.findViewById(R.id.progressBarCholesterol);
        progressCholesterolTextView = view.findViewById(R.id.progressCholesterolTextView);

        progressBarSalt = view.findViewById(R.id.progressBarSodium);
        progressSaltTextView = view.findViewById(R.id.progressSodiumTextView);

        progressBarSugar = view.findViewById(R.id.progressBarSugar);
        progressSugarTextView = view.findViewById(R.id.progressSugarTextView);

        takePhotoBtn = view.findViewById(R.id.takePhotoBtn);
        uploadPhotoBtn = view.findViewById(R.id.uploadPhotoBtn);
        // todo: this is actually a card view.
        logMealBtn = view.findViewById(R.id.logMealBtn);
        logMealBtn.setOnClickListener(onNavToLogMealListener());

        foodImage = view.findViewById(R.id.foodPhoto);

        // todo: set onclick here
        // logMealBtn.setOnClickListener(onNavToLogMealListener());
        // historyMealBtn.setOnClickListener(onNavToHistoryMealListener());
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


    @NonNull
    @Contract(" -> new")
    private ValueEventListener onGenderHealthValueListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userProfile = snapshot.getValue(UserProfile.class);

                if (userProfile != null)
                {
                    gender = userProfile.getGender();
                    switch(gender)
                    {
                        case "Male":
                            maxCalories = 3000; // per calories
                            maxCholesterol = 300; // per mg
                            maxSugar = 36; // per grams
                            maxSalt = 2300; // per mg
                            break;
                        case "Female":
                            maxCalories = 2000;
                            maxCholesterol = 240;
                            maxSugar = 24;
                            maxSalt = 2300; // per mg, should be sodium
                            break;
                        default:
                            // wrong gender no default value.
                            Log.d(TAG, "Unknown gender: " + gender);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error);
            }
        };
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

            String[] classes = {"Nasi Lemak", "Kaya Toast", "Curry Puff", "Sliced Fish Soup"};
            // fixme: eggs need to remove, we can add Laksa

            // result.setText(classes[maxPos]);
            // todo: need to test image recognition algo.
            foodName = classes[maxPos];
            Log.d(TAG, "The dish name is classified as: " + foodName);
            foodNameTextView.setText(foodName);

            // call API, and get result with that model class.
            Call<FoodItem> call = caloriesNinjaAPI.getFoodItem(foodName);
            // todo: uncomment this part below to do get calories info and more from this API.
            call.enqueue(callBackResponseFromAPI());

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
                        // todo: set progress bar here
                        double totalCalories = 0;
                        double totalCholesterol = 0;
                        double totalSalt = 0;
                        double totalSugar = 0;
                        
                        // get all total calculations
                        for (FoodItem.Item item : foodItem.getItems())
                        {
                            totalCalories += item.getCalories();
                            totalCholesterol += item.getCholesterol_mg();
                            totalSalt += item.getSodium_mg();
                            totalSugar += item.getSugar_g();
                        }

                        // calculate percentage
                        double percentageCalories = (totalCalories / maxCalories) * 100;
                        double percentageCholesterol = (totalCholesterol / maxCholesterol) * 100;
                        double percentageSalt = (totalSalt / maxSalt) * 100;
                        double percentageSugar = (totalSugar / maxSugar) * 100;

                        // Log.d(TAG, "onResponse: " + percentageCalories);
                        // Log.d(TAG, "onResponse: " + percentageCholesterol);
                        // Log.d(TAG, "onResponse: " + percentageSalt);
                        // Log.d(TAG, "onResponse: " + percentageSugar);

                        // fixme: null pointer exception
                        progressBarCalories.setProgress((int) percentageCalories);
                        progressCaloriesTextView.setText(String.format(Locale.ROOT, "%.1f%%",
                                percentageCalories));

                        progressBarCholesterol.setProgress((int) percentageCholesterol);
                        progressCholesterolTextView.setText(String.format(Locale.ROOT, "%.1f%%",
                                percentageCholesterol));

                        // fixme: recalculate sodium percentage
                        progressBarSalt.setProgress((int) percentageSalt);
                        progressSaltTextView.setText(String.format(Locale.ROOT, "%.1f%%",
                                percentageSalt));

                        progressBarSugar.setProgress((int) percentageSugar);
                        progressSugarTextView.setText(String.format(Locale.ROOT, "%.1f%%",
                                percentageSugar));
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
            intentNavToLogMeal = new Intent(requireContext(), UserLogMealActivity.class);
            intentNavToLogMeal.putExtra("gender", gender);
            startActivity(intentNavToLogMeal);
            requireActivity().finish();
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
        return inflater.inflate(R.layout.fragment_user_log_meal_nutrition_analysis, container, false);
    }
}