package com.example.myfoodchoice.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodchoice.R;
import com.google.firebase.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity
{
    // TODO: declare UI components

    // buttons
    private Button signupBtn;

    // log in clickable text
    private TextView navLoginClick;

    // Edit text
    private EditText signupEmailEditText, signupFirstNameEditText, signupLastNameEditText, signupPasswordEditText;

    // progress bar
    private ProgressBar progressBar;

    private SpannableString spannableStringLoginNav;

    // for authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // init firebase auth
        mAuth = FirebaseAuth.getInstance();

        // TODO: init UI components

        // edit text
        signupEmailEditText = findViewById(R.id.sign_up_email);
        signupFirstNameEditText = findViewById(R.id.sign_up_firstname);
        signupLastNameEditText = findViewById(R.id.sign_up_lastname);
        signupPasswordEditText = findViewById(R.id.sign_up_password);

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // button
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(onSignUpListener());

        // clickable text
        navLoginClick = findViewById(R.id.clickableLoginNavText);
        spannableStringLoginNav = new SpannableString(navLoginClick.getText());
        spannableStringLoginNav.setSpan(clickableLoginNavSpan(), 18, navLoginClick.length(), 0);
        navLoginClick.setText(spannableStringLoginNav);
        navLoginClick.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            Log.d("RegisterActivity", "signup button activated! ");
            String email = signupEmailEditText.getText().toString().trim();
            String password = signupPasswordEditText.getText().toString().trim();

            // validation
            if (TextUtils.isEmpty(email))
            {
                signupEmailEditText.setError("Email is required.");
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                signupPasswordEditText.setError("Password is required.");
                return;
            }

            if (password.length() < 4)
            {
                signupPasswordEditText.setError("Password must be at least 6 characters.");
                return;
            }

            // make the progressBar appear.
            progressBar.setVisibility(View.VISIBLE);

            // register user to the firebase.
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "User created successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,
                            "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        };
    }

    private ClickableSpan clickableLoginNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }
}