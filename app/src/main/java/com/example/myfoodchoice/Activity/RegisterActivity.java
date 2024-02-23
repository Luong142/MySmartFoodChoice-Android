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
    private EditText signupEmailEditText, signupFirstNameEditText, signupLastNameEditText, signupPasswordEditText
            ,signupUsernameEditText;

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
        signupUsernameEditText  = findViewById(R.id.sign_up_username);
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

        // if the user already have their account (alternative)
        if (mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
                Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6)
            {
                Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            // make the progressBar appear.
            progressBar.setVisibility(View.VISIBLE);

            // register user to the firebase.
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(this, "User created successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Error" +
                            Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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