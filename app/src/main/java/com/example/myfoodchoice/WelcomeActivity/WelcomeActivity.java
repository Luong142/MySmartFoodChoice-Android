package com.example.myfoodchoice.WelcomeActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterBusinessTrainerActivity;
import com.example.myfoodchoice.AuthenticationActivity.RegisterUserActivity;
import com.example.myfoodchoice.Prevalent.Prevalent;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Contract;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity
{
    // declare buttons
     Button signingBtn, signupBtn;

     String emailRememberMe, passwordRememberMe;

     TextView signUpAsUser, signUpAsTrainer, signUpAsDietitian;

    SpannableString spannableSignUpAsUserNav, spannableSignUpAsTrainer, spannableSignUpAsDietitian;

    FirebaseAuth firebaseAuth;

    static final int INDEXSTART = 0;

    AlertDialog.Builder builder;

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

        // init text view.
        signUpAsUser = findViewById(R.id.signUpAsUser);
        signUpAsTrainer = findViewById(R.id.signUpAsTrainer);
        signUpAsDietitian = findViewById(R.id.signUpAsDietitian);

        // nav to sign up page based on text click
        spannableSignUpAsUserNav = new SpannableString(signUpAsUser.getText());
        spannableSignUpAsUserNav.setSpan(clickableSignUpAsUser(), 17, signUpAsUser.length(), 0);
        signUpAsUser.setText(spannableSignUpAsUserNav);
        signUpAsUser.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to sign up page based on text click
        spannableSignUpAsTrainer = new SpannableString(signUpAsTrainer.getText());
        spannableSignUpAsTrainer.setSpan(clickableSignUpAsTrainer(), INDEXSTART, signUpAsTrainer.length(), 0);
        signUpAsTrainer.setText(spannableSignUpAsTrainer);
        signUpAsTrainer.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to sign up page based on text click
        spannableSignUpAsDietitian = new SpannableString(signUpAsDietitian.getText());
        spannableSignUpAsDietitian.setSpan(clickableSignUpAsDietitian(), INDEXSTART, signUpAsDietitian.length(), 0);
        signUpAsDietitian.setText(spannableSignUpAsDietitian);
        signUpAsDietitian.setMovementMethod(LinkMovementMethod.getInstance());

        signingBtn.setOnClickListener(onSignInListener);

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

        // TODO: we have normal user, guest, business vendor with two types
        //  (Dietitian and Trainer)
    }

    @NonNull
    @Contract(" -> new")
    private ClickableSpan clickableSignUpAsDietitian()
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(View widget)
            {
                Intent intent = new Intent(WelcomeActivity.this,
                        RegisterBusinessTrainerActivity.class);
                startActivity(intent);
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ClickableSpan clickableSignUpAsTrainer()
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(View widget)
            {
                Intent intent = new Intent(WelcomeActivity.this,
                        RegisterBusinessTrainerActivity.class);
                startActivity(intent);
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ClickableSpan clickableSignUpAsUser()
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(View widget)
            {
                Intent intent = new Intent(WelcomeActivity.this,
                        RegisterBusinessTrainerActivity.class);
                startActivity(intent);
            }
        };
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
        finish();
    };

}