package com.example.myfoodchoice.UserFragment;

import static com.example.myfoodchoice.ModelMeal.Meal.formatTime;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.Adapter.DishGuestUserAdapter;
import com.example.myfoodchoice.AdapterInterfaceListener.OnDishClickListener;
import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.ModelMeal.Meal;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.CaloriesNinjaAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitClient;
import com.example.myfoodchoice.ml.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogMealNutritionAnalysisFragment extends Fragment implements OnDishClickListener
{
    final static String TAG = "UserLogMealNutritionFragment";
    int imageSize;

    // TODO: declare UI components
    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceDailyFoodIntake,
            databaseReferenceDailyFoodIntakeChild;
    ImageView foodImage;

    TextView cholesterolTextView, sugarTextView, saltTextView, caloriesTextView;

    TextView checkInTextView, foodNameTextView;

    // TODO: add in one more button for taking photo I think.
    FloatingActionButton takePhotoBtn, uploadPhotoBtn;

    CardView logMealBtn;

    LinearLayout addDishBtn;

    ActivityResultLauncher<Intent> uploadPhotoactivityResultLauncher, takePhotoActivityResultLauncher;

    ActivityResultLauncher<String[]> requestPermissionLauncher;

    // for image file

    Bitmap image;

    int dimension;

    // calling calories ninja API
    private CaloriesNinjaAPI caloriesNinjaAPI;

    private FoodItem foodItem;
    FoodItem.Item itemDisplay;

    List<FoodItem.Item> foodItemsDisplay;
    // firebase

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID, gender, foodName;

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meals"; // fixme:  the path need to access daily meal.

    Meal meal;
    double totalCalories, totalCholesterol, totalSalt, totalSugar;

    RecyclerView dishRecyclerView;

    DishGuestUserAdapter dishGuestUserAdapter;
    private Uri selectedImageUri;

    Bundle bundle;

    StringBuilder caloriesMessage, cholesterolMessage, saltMessage, sugarMessage;
    double displayCalories, displaySalt, displaySugar, displayCholesterol;

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

            databaseReferenceDailyFoodIntake =
                    firebaseDatabase.getReference(PATH_DAILY_FOOD_INTAKE).child(userID);

        }
        // set the food item in the Meal object
        // todo: test the meal object
        bundle = getArguments();
        if (bundle != null)
        {
            meal = bundle.getParcelable("meal");
            /*
            if (meal != null)
            {
                Log.d(TAG, "onViewCreated: " + meal.getKey());
                Log.d(TAG, "onViewCreated: " + meal.isAfternoon());
                Log.d(TAG, "onViewCreated: " + meal.isMorning());
                Log.d(TAG, "onViewCreated: " + meal.isNight());
            }
            todo: test is done
             */
        }

        foodItem = new FoodItem();
        if (meal != null)
        {
            meal.setDishes(foodItem);
        }

        itemDisplay = new FoodItem.Item();

        // init nutrition value to 0
        totalCalories = 0;
        totalCholesterol = 0;
        totalSalt = 0;
        totalSugar = 0;

        // init message
        caloriesMessage = new StringBuilder();
        cholesterolMessage = new StringBuilder();
        saltMessage = new StringBuilder();
        sugarMessage = new StringBuilder();

        // TODO: init UI components
        checkInTextView = view.findViewById(R.id.checkInTextView);
        foodNameTextView = view.findViewById(R.id.foodName);

        // fixme: should be matched with the ID.


        // todo: init text view for nutrition value
        caloriesTextView = view.findViewById(R.id.caloriesTextView);
        cholesterolTextView = view.findViewById(R.id.cholesterolTextView);
        saltTextView = view.findViewById(R.id.sodiumTextView);
        sugarTextView = view.findViewById(R.id.sugarTextView);

        takePhotoBtn = view.findViewById(R.id.takePhotoBtn);
        uploadPhotoBtn = view.findViewById(R.id.uploadPhotoBtn);

        // todo: this is actually a card view.
        logMealBtn = view.findViewById(R.id.logMealBtn);
        logMealBtn.setOnClickListener(onNavToLogMealListener());

        // init add dish btn card view
        addDishBtn = view.findViewById(R.id.addDishBtn);
        addDishBtn.setOnClickListener(onAddDishListener());

        // init and set recycler view
        foodItemsDisplay = new ArrayList<>();
        dishRecyclerView = view.findViewById(R.id.dishRecyclerView);
        dishGuestUserAdapter = new DishGuestUserAdapter(foodItemsDisplay, this);
        setAdapter();
        dishRecyclerView.setVerticalScrollBarEnabled(true);

        foodImage = view.findViewById(R.id.foodPhoto);

        // todo: set onclick here
        uploadPhotoBtn.setOnClickListener(onUploadPhotoListener());
        takePhotoBtn.setOnClickListener(onTakePhotoListener());

        // todo: init API
        caloriesNinjaAPI = RetrofitClient.getRetrofitInstance().create(CaloriesNinjaAPI.class);

        imageSize = 224; // important?

        // for camera activity
        takePhotoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), onTakePhotoActivityLauncher());

        // for upload photo
        uploadPhotoactivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), onUploadPhotoActivityLauncher());

        // for permission
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), onPermissionLauncher());

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
            // Log.d(TAG, "The dish name is classified as: " + foodName);
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
            // Log.d(TAG, "The dish info is: \n" + s);

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
    @Contract(pure = true)
    private View.OnClickListener onAddDishListener()
    {
        return v ->
        {
            if (itemDisplay.getName() == null || itemDisplay.getFoodImage() == null)
            {
                Toast.makeText(requireContext(), "Please select a dish", Toast.LENGTH_SHORT).show();
                return;
            }

            // fixme: there might be a problem with the dish
            Toast.makeText(requireContext(), "Dish is added.", Toast.LENGTH_SHORT).show();

            // ensure the list is initialized before adding an item
            if (meal.getDishes().getItems() == null)
            {
                meal.getDishes().setItems(new ArrayList<>());
            }

            // this object for the next activity to record.
            meal.getDishes().getItems().add(itemDisplay);

            // this one is for adapter which means for UI to show.
            foodItemsDisplay.add(itemDisplay);

            // fixme: there is another problem that the food item can be duplicated.
            // Log.d(TAG, "onAddDishListener: " + foodItems);
            dishGuestUserAdapter.notifyItemInserted(foodItemsDisplay.size() - 1);
        };
    }

    @Override
    public void onRemoveDish(int position) // todo: remove the element
    {
        // todo: to remove the dish from the list.
        if (meal.getDishes().getItems() == null)
        {
            meal.getDishes().setItems(new ArrayList<>());
        }

        // this object for the next activity to record.
        meal.getDishes().getItems().remove(position);

        // this one is for adapter which means for UI to show.
        foodItemsDisplay.remove(position);

        dishGuestUserAdapter.notifyItemRemoved(position);
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
                        // assign the variable to the is foodItems array list.

                        // Log.d(TAG, "onResponse: " + foodItem);
                        // todo: set progress bar here

                        // get all total calculations
                        for (FoodItem.Item itemLoop : foodItem.getItems())
                        {
                            totalCalories += itemLoop.getCalories();
                            totalCholesterol += itemLoop.getCholesterol_mg();
                            totalSalt += itemLoop.getSodium_mg();
                            totalSugar += itemLoop.getSugar_g();

                            // todo: set the item.
                            itemDisplay = itemLoop;
                            itemDisplay.setFoodImage(selectedImageUri.toString());

                        }

                        // todo: set the total calories first.
                        meal.setTotalCalories(totalCalories);
                        meal.setTotalCholesterol(totalCholesterol);
                        meal.setTotalSodium(totalSalt);
                        meal.setTotalSugar(totalSugar);

                        // update the individual nutrition value
                        updateDishNutritionUI();

                        // reset the value
                        /*
                        totalCalories = 0;
                        totalCholesterol = 0;
                        totalSalt = 0;
                        totalSugar = 0;
                         */
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

    private void updateDishNutritionUI()
    {
        // to display individually
        displayCalories = totalCalories;
        displayCholesterol = totalCholesterol;
        displaySalt = totalSalt;
        displaySugar = totalSugar;
        // foodItems.add(itemLoop);

        // todo: set text
        caloriesMessage
                .append(displayCalories)
                .append(" kcal");
        caloriesTextView.setText(caloriesMessage.toString());

        cholesterolMessage
                .append(displayCholesterol)
                .append(" mg");
        cholesterolTextView.setText(cholesterolMessage.toString());

        saltMessage
                .append(displaySalt)
                .append(" mg");
        saltTextView.setText(saltMessage.toString());

        sugarMessage
                .append(displaySugar)
                .append(" g");
        sugarTextView.setText(sugarMessage.toString());

        // reset string builder
        caloriesMessage.setLength(0);
        cholesterolMessage.setLength(0);
        saltMessage.setLength(0);
        sugarMessage.setLength(0);

        // reset the value for one dish only
        displayCalories = 0;
        displaySalt = 0;
        displaySugar = 0;
        displayCholesterol = 0;
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
            StringBuilder message = new StringBuilder();

            if (meal.getDishes() == null || meal.getDishes().getItems() == null ||
                    meal.getDishes().getItems().isEmpty())
            {
                message.append("Dish is required to be added before logging your meal.");
                Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (meal.getDishes().getItems().size() > 5)
            {
                message.append("You can only add up to 5 dishes.");
                Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (foodItemsDisplay.isEmpty())
            {
                message.append("Dish is required to be added before logging your meal.");
                Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            // todo: set the time stamp for meal
            meal.startDate();

            // todo: set total nutrition value

            // todo: push the data in firebase
            databaseReferenceDailyFoodIntakeChild = databaseReferenceDailyFoodIntake.push();

            meal.setKey(databaseReferenceDailyFoodIntakeChild.getKey());

            // fixme: testing
            //Log.d(TAG, "onNavToLogMealListener: " + meal);
            //Log.d(TAG, "onNavToLogMealListener: " + formatTime(meal.getDate()));

            databaseReferenceDailyFoodIntakeChild.setValue(meal).addOnCompleteListener(onCompleteLogMealListener());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteLogMealListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(requireContext(), "Logged your meal.", Toast.LENGTH_LONG).show();
                bundle.putParcelable("meal", meal);

                // todo: reverse this page change to
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UserLogMealFragment())
                        .commit();
            }
            else
            {
                Toast.makeText(requireContext(), "Error logging your meal.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCompleteLogMealListener: " + task.getException());
            }
        };
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        dishRecyclerView.setLayoutManager(layoutManager);
        dishRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dishRecyclerView.setAdapter(dishGuestUserAdapter);
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultCallback<Map<String, Boolean>> onPermissionLauncher()
    {
        return permissions ->
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
        };
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultCallback<ActivityResult> onTakePhotoActivityLauncher()
    {
        return result ->
        {
            if (result.getResultCode() == Activity.RESULT_OK)
            {
                Intent data = result.getData();
                if (data != null && data.getData() != null)
                {
                    selectedImageUri = data.getData();
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
        };
    }

    @NonNull
    @Contract(pure = true)
    private ActivityResultCallback<ActivityResult> onUploadPhotoActivityLauncher()
    {
        return result ->
        {
            if (result.getResultCode() == Activity.RESULT_OK)
            {
                Intent data = result.getData();
                if (data != null && data.getData() != null)
                {
                    selectedImageUri = data.getData();
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
