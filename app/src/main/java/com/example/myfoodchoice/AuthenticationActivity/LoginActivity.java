package com.example.myfoodchoice.AuthenticationActivity;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodchoice.Activity.GuestMainMenuActivity;
import com.example.myfoodchoice.Activity.MainMenuActivity;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    // TODO: declare UI component

    // button
    private Button loginBtn;

    // Edit text
    private EditText loginEmailEditText, loginPasswordEditText;

    // check box
    private CheckBox rememberMe;

    // clickable text
    private TextView clickableForgotPassword, clickableSignUpNav, clickableLoginAsGuest;

    // for clickable text
    private SpannableString spannableStringSignUpNav, spannableStringLoginAsGuestNav, spannableStringForgotPassword;

    // for progress bar
    private ProgressBar progressBar;

    // firebase login
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Smart Food Choice");

        // TODO: init Firebase auth
        mAuth = FirebaseAuth.getInstance();

        // TODO: init UI components
        loginEmailEditText = findViewById(R.id.login_email);
        loginPasswordEditText = findViewById(R.id.login_password);

        // check box
        rememberMe = findViewById(R.id.rememberMe_checkBox);

        // clickable text
        clickableForgotPassword = findViewById(R.id.clickableForgotPassword);
        clickableSignUpNav = findViewById(R.id.clickableSignUpNavText);
        clickableLoginAsGuest = findViewById(R.id.clickableLoginGuestNavText);

        // button
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(onLoginListener());

        // nav to sign up page based on text click
        spannableStringSignUpNav = new SpannableString(clickableSignUpNav.getText());
        spannableStringSignUpNav.setSpan(clickableSignUpNavSpan(), 17, clickableSignUpNav.length(), 0);
        clickableSignUpNav.setText(spannableStringSignUpNav);
        clickableSignUpNav.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to guest main menu page based on text click
        spannableStringLoginAsGuestNav = new SpannableString(clickableLoginAsGuest.getText());
        spannableStringLoginAsGuestNav.setSpan(clickableLoginAsGuestNavSpan(), 0, clickableLoginAsGuest.length(), 0);
        clickableLoginAsGuest.setText(spannableStringLoginAsGuestNav);
        clickableLoginAsGuest.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to forgot password page based on text click
        spannableStringForgotPassword = new SpannableString(clickableForgotPassword.getText());
        spannableStringForgotPassword.setSpan(clickableForgotPasswordNavSpan(), 0, clickableForgotPassword.length(), 0);
        clickableForgotPassword.setText(spannableStringForgotPassword);
        clickableForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    // TODO: to implement the login functionalities for this activity.
    private View.OnClickListener onLoginListener()
    {
        return v ->
        {
            Log.d("LoginActivity", "login button activated! ");
            // TODO: login function
            String email = loginEmailEditText.getText().toString().trim();
            String password = loginPasswordEditText.getText().toString().trim();

            // validation
            if (TextUtils.isEmpty(email))
            {
                loginEmailEditText.setError("Email is required.");
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                loginPasswordEditText.setError("Password is required.");
                return;
            }

            // loading
            progressBar.setVisibility(View.VISIBLE);

            // authentication login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            });

        };
    }

    private ClickableSpan clickableForgotPasswordNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to forgot password page! ");
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);

                }
            };
        }
    }

    private ClickableSpan clickableSignUpNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to sign up page! ");
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }

    private ClickableSpan clickableLoginAsGuestNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to guest main menu page! ");
                    Intent intent = new Intent(LoginActivity.this, GuestMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }
}