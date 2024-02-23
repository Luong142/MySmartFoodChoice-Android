package com.example.myfoodchoice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfoodchoice.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity
{
    // TODO: init UI component
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // TODO: research about FireBase on this link:
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(onLoginListener());
    }

    // TODO: to implement the login functionalities for this activity.
    private View.OnClickListener onLoginListener()
    {
        return v ->
        {
            FirebaseAnalytics.getInstance(this).logEvent("Testing", null);
        };
    }
}