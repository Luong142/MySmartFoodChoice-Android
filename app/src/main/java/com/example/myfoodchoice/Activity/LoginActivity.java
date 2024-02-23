package com.example.myfoodchoice.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myfoodchoice.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity
{
    // TODO: declare UI component

    // button
    private Button loginBtn;

    // Edit text
    private EditText loginUsernameEditText, loginPasswordEditText;

    // check box
    private CheckBox rememberMe;

    private FirebaseAnalytics firebaseAnalytics;

    // clickable text
    private TextView clickableForgotPassword, clickableSignUpNav, clickableLoginAsGuest;

    private SpannableString spannableStringSignUpNav, spannableStringLoginAsGuestNav, spannableStringForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // TODO: init UI components
        loginUsernameEditText = findViewById(R.id.editText_username);
        loginPasswordEditText = findViewById(R.id.editText_password);

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


    }

    // TODO: to implement the login functionalities for this activity.
    private View.OnClickListener onLoginListener()
    {
        return v ->
        {
            Log.d("LoginActivity", "login button activated! ");

            // TODO: implement login logic here? override the function from the database.



            finish();
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