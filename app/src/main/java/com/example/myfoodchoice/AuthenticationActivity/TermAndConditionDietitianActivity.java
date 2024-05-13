package com.example.myfoodchoice.AuthenticationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

public class TermAndConditionDietitianActivity extends AppCompatActivity
{
    FloatingActionButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition_dietitian);

        // back btn
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(onBackListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onBackListener()
    {
        return v ->
        {
            Intent intent = new Intent(TermAndConditionDietitianActivity.this,
                    RegisterBusinessActivity.class);
            startActivity(intent);
            finish();
        };
    }
}