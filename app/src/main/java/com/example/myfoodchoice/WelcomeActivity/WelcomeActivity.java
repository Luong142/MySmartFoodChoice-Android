package com.example.myfoodchoice.WelcomeActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterActivity;
import com.example.myfoodchoice.Prevalent.Prevalent;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity
{
    // declare buttons
    private Button signingBtn, signupBtn;

    private BottomNavigationView bottomNavigationView;

    private String emailRememberMe, passwordRememberMe;

    private FirebaseAuth firebaseAuth;

    private AlertDialog.Builder builder;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // init paper
        Paper.init(WelcomeActivity.this);

        // init firebase auth;
        firebaseAuth = FirebaseAuth.getInstance();

        // init buttons
        signingBtn = findViewById(R.id.signInBtn);
        signupBtn = findViewById(R.id.signUpBtn);

        signingBtn.setOnClickListener(onSignInListener);
        signupBtn.setOnClickListener(onSignUpListener);

        // init bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_welcome);
        bottomNavigationView.setOnItemSelectedListener(onItemClickedListener());

        // assign to email and password
        emailRememberMe = Paper.book().read(Prevalent.UserEmailKey);
        passwordRememberMe = Paper.book().read(Prevalent.UserPasswordKey);

        if (emailRememberMe != null && passwordRememberMe != null)
        {
            if (!TextUtils.isEmpty(emailRememberMe) && !TextUtils.isEmpty(passwordRememberMe))
            {
                allowLogin(emailRememberMe, passwordRememberMe);

                builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setTitle("Already Logged in");
                builder.setMessage("Please wait...");
                builder.show();
            }
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

    private NavigationBarView.OnItemSelectedListener onItemClickedListener()
    {
        return item ->
        {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_welcome)
            {
                // Handle navigation to Home
                return true;
            }
            else if (itemId == R.id.nav_about_us)
            {
                Intent intent = new Intent(WelcomeActivity.this, AboutUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.nav_partnership)
            {
                Intent intent2 = new Intent(WelcomeActivity.this, PartnershipActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.nav_review)
            {
                Intent intent3 = new Intent(WelcomeActivity.this, ReviewActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.nav_contact_us)
            {
                Intent intent5 = new Intent(WelcomeActivity.this, ContactUsActivity.class);
                startActivity(intent5);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        };
    }

    private final View.OnClickListener onSignInListener = v ->
    {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    };

    private final View.OnClickListener onSignUpListener = v ->
    {
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(intent);
    };
}