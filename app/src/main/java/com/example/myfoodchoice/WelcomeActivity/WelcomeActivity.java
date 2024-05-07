package com.example.myfoodchoice.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterBusinessActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterUserActivity;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;
import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCuisines;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.CaloriesNinjaAPI;
import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodDetailAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCategoryAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodRecipeCuisineAPI;

import io.paperdb.Paper;
import retrofit2.Call;

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

        /*
        CallChatAPI.makeChatGPTRequest(
                "I am happy to see you pls explain what is FLutter", "you are my friend");
         */

        // todo: continue this, https://platform.openai.com/docs/api-reference/making-requests

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

    private final View.OnClickListener onSignInListener = v ->
    {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    };
}
