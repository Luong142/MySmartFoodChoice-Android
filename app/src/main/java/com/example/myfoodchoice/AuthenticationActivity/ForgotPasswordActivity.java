package com.example.myfoodchoice.AuthenticationActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{
    // TODO: declare UI components
    private Button resetPasswordBtn, backBtn;

    private EditText emailEditText;

    private ProgressBar progressBar;

    private String email;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // TODO: initialize UI components
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        backBtn = findViewById(R.id.backBtn);
        emailEditText = findViewById(R.id.emailEditText);
        progressBar = findViewById(R.id.progressBar);

        // set the visibility of progress bar
        progressBar.setVisibility(ProgressBar.GONE);
        resetPasswordBtn.setVisibility(Button.VISIBLE);

        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // set on click listener for reset password button
        resetPasswordBtn.setOnClickListener(onResetListener());
        backBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private View.OnClickListener onResetListener()
    {
        return v ->
        {
            email = emailEditText.getText().toString();
            if (email.isEmpty())
            {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }
            resetPassword();
        };
    }

    private void resetPassword()
    {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        resetPasswordBtn.setVisibility(Button.GONE);

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task ->
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email for password reset instructions", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                {
                    Toast.makeText
                            (ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.GONE);
                    resetPasswordBtn.setVisibility(Button.VISIBLE);
                });
    }
}