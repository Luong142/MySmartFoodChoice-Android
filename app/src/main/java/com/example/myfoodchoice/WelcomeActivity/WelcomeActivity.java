package com.example.myfoodchoice.WelcomeActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterBusinessActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterGuestActivity;
import com.example.myfoodchoice.CallAPI.ClaudeAPIService;
import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;
import com.example.myfoodchoice.Prevalent.Prevalent;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.CaloriesNinjaAPI;
import com.example.myfoodchoice.RetrofitProvider.FreeFoodAPI;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Contract;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity
{
    // declare buttons
    Button signingBtn, signUpAsGuestBtn, signUpAsBusinessBtn;
    String emailRememberMe, passwordRememberMe;

    // TextView signUpAsGuest, signUpAsBusiness;

    // SpannableString spannableSignUpAsGuestNav, spannableSignUpAsBusiness;

    FirebaseAuth firebaseAuth;

    static final int INDEXSTART = 0;

    static final String TAG = "WelcomeActivity";

    AlertDialog.Builder builder;

    private static final String BASE_URL = "https://api.anthropic.com/";

    private ClaudeAPIService claudeAPIService;

    private CaloriesNinjaAPI caloriesNinjaAPI;

    private FreeFoodAPI freeFoodAPI;

    // private Call<FoodItem> call;

    private Call<Dish> callDish;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // TODO: test ClaudeAPI part
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         */
        // claudeAPIService = retrofit.create(ClaudeAPIService.class);
        // testClaudeAPI(); FIXME: this one failed.

        // FIXME: this one is not free too.
        // ChatGPTAPI chatGPTAPI = new ChatGPTAPI();
        // chatGPTAPI.generateOutput("What is the meaning of life?", this);

        // TODO: init ninja api
        // caloriesNinjaAPI = RetrofitClient.getRetrofitNinjaInstance().create(CaloriesNinjaAPI.class);
        //freeFoodAPI = RetrofitClient.getRetrofitFreeInstance().create(FreeFoodAPI.class);

        // fixme: to test the input string name of food.
        String query = "Fish Soup";

        // call = caloriesNinjaAPI.getFoodItem(query);
        //callDish = freeFoodAPI.searchMealByName(query);
        // todo: in this part we can use Ninja API calls.
        //callDish.enqueue(callBackResponseFromAPI());

        // init paper
        Paper.init(WelcomeActivity.this);

        // init firebase auth;
        firebaseAuth = FirebaseAuth.getInstance();

        // init buttons
        signingBtn = findViewById(R.id.signInBtn);
        signUpAsGuestBtn = findViewById(R.id.signUpAsGuestBtn);
        signUpAsBusinessBtn = findViewById(R.id.signUpAsBusinessBtn);


        // nav to sign up page based on text click
        // FIXME: the index is out of bound.
        signUpAsGuestBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(WelcomeActivity.this, RegisterGuestActivity.class);
            startActivity(intent);
        });
        signUpAsBusinessBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(WelcomeActivity.this, RegisterBusinessActivity.class);
            startActivity(intent);
        });

        signingBtn.setOnClickListener(onSignInListener);

        // assign to email and password
        emailRememberMe = Paper.book().read(Prevalent.UserEmailKey);
        passwordRememberMe = Paper.book().read(Prevalent.UserPasswordKey);

        if (emailRememberMe != null && passwordRememberMe != null)
        {
            if (!TextUtils.isEmpty(emailRememberMe) && !TextUtils.isEmpty(passwordRememberMe))
            {
                allowLogin(emailRememberMe, passwordRememberMe);

                if (!isFinishing())
                {
                    builder = new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setTitle("Already Logged in");
                    builder.setMessage("Please wait...");
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }


        // TODO: we have normal user, guest, business vendor with two types
        //  (Dietitian and Trainer)
        // TODO: use the attribute to identify which user type.
        //  (Dietitian = 1, Trainer = 2, Normal User = 3)
        //  (Dietitian = 1, Trainer = 2, Normal User = 3)
    }

    @NonNull // todo: test me, done successfully.
    @Contract(" -> new")
    private Callback<Dish> callBackResponseFromAPI()
    {
        return new Callback<Dish>()
        {
            @Override
            public void onResponse(@NonNull Call<Dish> call, @NonNull Response<Dish> response)
            {
                if (response.isSuccessful())
                {
                    Dish dish = response.body();
                    Log.d(TAG, "onResponse: " + dish);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Dish> call, @NonNull Throwable t)
            {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        };
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing())
        {
            alertDialog.dismiss();
        }
    }

    private void allowLogin(String email, String password)
    {
        // TODO: login function

        // authentication login
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(WelcomeActivity.this, "Welcome to Smart Food Choice!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WelcomeActivity.this, UserMainMenuActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(WelcomeActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private final View.OnClickListener onSignInListener = v ->
    {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    };


}