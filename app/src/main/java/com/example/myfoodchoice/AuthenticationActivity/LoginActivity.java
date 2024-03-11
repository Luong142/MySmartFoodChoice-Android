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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myfoodchoice.GuestActivity.GuestBMICalculatorActivity;
import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.Prevalent.Prevalent;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{
    // TODO: declare UI component

    // button
    Button loginBtn;

    // Edit text
    EditText loginEmailEditText, loginPasswordEditText;

    // check box
    CheckBox rememberMe;

    // clickable text
    TextView clickableForgotPassword, clickableSignUpNav, clickableLoginAsGuest;

    // for clickable text
    SpannableString spannableStringSignUpNav, spannableStringLoginAsGuestNav, spannableStringForgotPassword;

    // for progress bar
    ProgressBar progressBar;

    // firebase login
    FirebaseAuth mAuth;

    static final int INDEXSTART = 0;

    FirebaseDatabase firebaseDatabase;

    String email, password;

    UserProfile userProfile;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Smart Food Choice");

        // TODO: init paper
        Paper.init(LoginActivity.this);

        // TODO: init Firebase auth
        mAuth = FirebaseAuth.getInstance();

        // TODO: init Firebase database, paste the correct link as reference.
        firebaseDatabase = FirebaseDatabase.getInstance(
                "https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        // for testing
        // firebaseDatabase.getReference().child("Test").child("new child").setValue("new value");

        // TODO: init UI components
        loginEmailEditText = findViewById(R.id.login_email);
        loginPasswordEditText = findViewById(R.id.login_password);

        // init user profile
        intent = getIntent();
        userProfile = intent.getParcelableExtra("userProfile");
        // TODO: do we need userProfile here to pass to main-menu?

        // check box
        rememberMe = findViewById(R.id.rememberMe_checkBox);

        // clickable text
        clickableForgotPassword = findViewById(R.id.clickableForgotPassword);
        clickableSignUpNav = findViewById(R.id.clickableSignUpNavText);
        clickableLoginAsGuest = findViewById(R.id.clickableLoginGuestNavText);

        // button
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setVisibility(View.VISIBLE);
        loginBtn.setOnClickListener(onLoginListener());

        // nav to sign up page based on text click
        spannableStringSignUpNav = new SpannableString(clickableSignUpNav.getText());
        spannableStringSignUpNav.setSpan(clickableSignUpNavSpan(), 17, clickableSignUpNav.length(), 0);
        clickableSignUpNav.setText(spannableStringSignUpNav);
        clickableSignUpNav.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to guest main menu page based on text click
        spannableStringLoginAsGuestNav = new SpannableString(clickableLoginAsGuest.getText());
        spannableStringLoginAsGuestNav.setSpan(clickableLoginAsGuestNavSpan(), INDEXSTART, clickableLoginAsGuest.length(), 0);
        clickableLoginAsGuest.setText(spannableStringLoginAsGuestNav);
        clickableLoginAsGuest.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to forgot password page based on text click
        spannableStringForgotPassword = new SpannableString(clickableForgotPassword.getText());
        spannableStringForgotPassword.setSpan(clickableForgotPasswordNavSpan(), INDEXSTART, clickableForgotPassword.length(), 0);
        clickableForgotPassword.setText(spannableStringForgotPassword);
        clickableForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // check box listener
        rememberMe.setOnCheckedChangeListener(onCheckedListener());

    }

    // TODO: the purpose is to save data via Paper.
    public CompoundButton.OnCheckedChangeListener onCheckedListener()
    {
        return (buttonView, isChecked) ->
        {
            email = loginEmailEditText.getText().toString().trim();
            password = loginPasswordEditText.getText().toString().trim();
            if (isChecked)
            {
                Log.d("LoginActivity", "remember me checked! ");
                Paper.book().write(Prevalent.UserEmailKey, email);
                Paper.book().write(Prevalent.UserPasswordKey, password);
            }
            
        };
    }
    // TODO: to implement the login functionalities for this activity.

     View.OnClickListener onLoginListener()
    {
        return v ->
        {
            Log.d("LoginActivity", "login button activated! ");
            email = loginEmailEditText.getText().toString().trim();
            password = loginPasswordEditText.getText().toString().trim();
            allowLogin(email, password);
        };
    }

    public void allowLogin(String email, String password)
    {
        // TODO: login function

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
        loginBtn.setVisibility(View.GONE);

        // authentication login
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(LoginActivity.this, "Welcome to Smart Food Choice!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, UserMainMenuActivity.class);
                intent.putExtra("userProfile", userProfile);
                Log.d("userProfile", "userProfile: " + userProfile);
                startActivity(intent);
                finish();
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Email or Password incorrect, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ClickableSpan clickableForgotPasswordNavSpan()
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

    public ClickableSpan clickableSignUpNavSpan()
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

    public ClickableSpan clickableLoginAsGuestNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to guest main menu page! ");
                    Intent intent = new Intent(LoginActivity.this, GuestBMICalculatorActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }
}