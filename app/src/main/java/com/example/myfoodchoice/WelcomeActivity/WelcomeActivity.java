package com.example.myfoodchoice.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterActivity;
import com.example.myfoodchoice.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WelcomeActivity extends AppCompatActivity
{
    // declare buttons
    private Button signingBtn, signupBtn;

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // init buttons
        signingBtn = findViewById(R.id.signInBtn);
        signupBtn = findViewById(R.id.signUpBtn);

        signingBtn.setOnClickListener(onSignInListener);
        signupBtn.setOnClickListener(onSignUpListener);

        // init bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_welcome);

        bottomNavigationView.setOnItemSelectedListener(item ->
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
        });
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