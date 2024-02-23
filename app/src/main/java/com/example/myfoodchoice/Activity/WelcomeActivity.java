package com.example.myfoodchoice.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfoodchoice.R;

public class WelcomeActivity extends AppCompatActivity
{
    // declare buttons
    private Button signingBtn, signupBtn;

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