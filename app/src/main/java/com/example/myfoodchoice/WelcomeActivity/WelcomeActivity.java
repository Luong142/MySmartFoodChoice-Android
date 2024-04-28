package com.example.myfoodchoice.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterBusinessActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterUserActivity;

import com.example.myfoodchoice.ModelChatGPT.ChatGPT;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCuisines;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.CaloriesNinjaAPI;
import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodDetailAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCategoryAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCuisineAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitChatGPTAPI;

import org.jetbrains.annotations.Contract;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity
{
    // declare buttons
    Button signingBtn, signUpAsGuestBtn, signUpAsBusinessBtn;

    // TextView signUpAsGuest, signUpAsBusiness;

    // SpannableString spannableSignUpAsGuestNav, spannableSignUpAsBusiness;

    static final int INDEXSTART = 0;

    static final String TAG = "WelcomeActivity";

    private static final String BASE_URL = "https://api.anthropic.com/";

    private CaloriesNinjaAPI caloriesNinjaAPI;

    private FreeFoodDetailAPI freeFoodDetailAPI;

    private FreeFoodRecipeCuisineAPI freeFoodRecipeCuisineAPI;

    private FreeFoodRecipeCategoryAPI freeFoodRecipeCategoryAPI;

    // private Call<FoodItem> call;

    private Call<Dish> callDish;

    private Call<RecipeCategories> categoriesCall;

    private Call<RecipeCuisines> cuisinesCall;

    private ChatGPTAPI chatGPTAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // LangChain4JAPI langChain4JAPI = new LangChain4JAPI();

        // Log.d(TAG, "onCreate: " + langChain4JAPI.getMessage("Hello friend"));

        chatGPTAPI = RetrofitChatGPTAPI.getRetrofitChatGPTInstance().create(ChatGPTAPI.class);

        ChatGPT chatGPTRequest = new ChatGPT();
        chatGPTRequest.setId("chatcmpl-abc123");
        chatGPTRequest.setCreated(1677858242);
        chatGPTRequest.setModel("gpt-3.5-turbo");
        chatGPTRequest.setObject("chat.completion");

        // todo: continue this, https://platform.openai.com/docs/api-reference/making-requests
        ChatGPT.Usage usage = new ChatGPT.Usage();


        // chatGPTRequest.setUsage()


        // chatGPTAPI.getChatCompletion("Hello friend").enqueue(callBackResponseFromAPI());

        // TODO: init ninja api
        // caloriesNinjaAPI = RetrofitClient.getRetrofitNinjaInstance().create(CaloriesNinjaAPI.class);

        // freeFoodAPI = RetrofitClient.getRetrofitFreeInstance().create(FreeFoodAPI.class);

        //FreeFoodRecipeCuisineAPI freeFoodRecipeCuisineAPI = RetrofitFreeFoodClient.
                //getRetrofitFreeInstance().create(FreeFoodRecipeCuisineAPI.class);

        //FreeFoodRecipeCategoryAPI freeFoodRecipeCategoryAPI = RetrofitFreeFoodClient.
                //getRetrofitFreeInstance().create(FreeFoodRecipeCategoryAPI.class);

        //freeFoodRecipeCuisineAPI.searchRecipeCuisine("Canadian").
                //enqueue(callBackResponseFromAPI());

        //freeFoodRecipeCategoryAPI.searchRecipeCategory("Seafood")
        // .enqueue(callBackCategoryResponseFromAPI());

        // fixme: to test the input string name of food.
        // String query = "Fish Soup";

        // call = caloriesNinjaAPI.getFoodItem(query);
        //callDish = freeFoodAPI.searchMealByName(query);
        // todo: in this part we can use Ninja API calls.
        //callDish.enqueue(callBackResponseFromAPI());

        // init paper
        Paper.init(WelcomeActivity.this);

        // init buttons
        signingBtn = findViewById(R.id.signInBtn);
        signUpAsGuestBtn = findViewById(R.id.signUpAsUserBtn);
        signUpAsBusinessBtn = findViewById(R.id.signUpAsBusinessBtn);

        // nav to sign up page based on text click
        // FIXME: the index is out of bound.
        signUpAsGuestBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(WelcomeActivity.this, RegisterUserActivity.class);
            startActivity(intent);
        });
        signUpAsBusinessBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(WelcomeActivity.this, RegisterBusinessActivity.class);
            startActivity(intent);
        });

        signingBtn.setOnClickListener(onSignInListener);

        // TODO: we have normal user, guest, business vendor with two types
        //  (Dietitian and Trainer)
        // TODO: use the attribute to identify which user type.
        //  (Dietitian = 1, Trainer = 2, Normal User = 3)
        //  (Dietitian = 1, Trainer = 2, Normal User = 3)
    }

    @NonNull
    @Contract(" -> new")
    private Callback<RecipeCategories> callBackCategoryResponseFromAPI()
    {
        return new Callback<RecipeCategories>()
        {
            @Override
            public void onResponse(Call<RecipeCategories> call, Response<RecipeCategories> response)
            {
                RecipeCategories recipeCategories = response.body();

                if (recipeCategories != null)
                {
                    Log.d(TAG, "onResponse: " + recipeCategories);
                }
            }

            @Override
            public void onFailure(Call<RecipeCategories> call, Throwable t) {

            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private Callback<RecipeCuisines> callBackResponseFromAPI()
    {
        return new Callback<RecipeCuisines>()
        {
            @Override
            public void onResponse(@NonNull Call<RecipeCuisines> call, @NonNull Response<RecipeCuisines> response)
            {
                RecipeCuisines recipeCuisines = response.body();

                if (recipeCuisines != null)
                {
                    Log.d(TAG, "onResponse: " + recipeCuisines);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeCuisines> call, @NonNull Throwable t)
            {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        };
    }


    private final View.OnClickListener onSignInListener = v ->
    {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    };
}
