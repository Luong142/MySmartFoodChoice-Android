package com.example.myfoodchoice.GuestActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfoodchoice.R;

import java.util.Objects;

public class bmiActivity extends AppCompatActivity
{
    // TODO: declare UI components
    private Button recalculateBMI;

    private TextView mBMIdisplay, mBMIcategory, mGender;
    
    private Intent intent;

    private ImageView imageView;

    private String bmi, height, weight;

    private float numBMI, numHeight, numWeight;

    LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        //Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">Result</font>"));
        //ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E1D1D"));
        // getSupportActionBar().setBackgroundDrawable(colorDrawable);

        // TODO: init UI components
        intent = getIntent();

        // button
        recalculateBMI = findViewById(R.id.recalculateBMIBtn);

        mBMIdisplay = findViewById(R.id.bmiDisplay);
        mBMIcategory = findViewById(R.id.bmiCategory);
        mGender = findViewById(R.id.genderDisplay);
        background = findViewById(R.id.contentLayout);

        imageView = findViewById(R.id.imageView);

        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");

        numHeight = Float.parseFloat(height);
        numWeight = Float.parseFloat(weight);

        numHeight = numHeight / 100;

        numBMI = numWeight  / (numHeight * numHeight);

        bmi = Float.toString(numBMI);

        if (numBMI < 16)
        {
            mBMIcategory.setText("Underweight");
            background.setBackgroundColor(R.drawable.red_warning);
            imageView.setImageResource(R.drawable.crosss);
        }
        else if (numBMI >= 16.9 && numBMI > 16)
        {
            mBMIcategory.setText("Moderately Underweight");
            background.setBackgroundColor(R.drawable.red_warning);
            imageView.setImageResource(R.drawable.warning);
        }
        else if (numBMI < 18.4 && numBMI > 17)
        {
            mBMIcategory.setText("Mild Underweight");
            background.setBackgroundColor(R.drawable.orange_mild);
            imageView.setImageResource(R.drawable.warning);

        }
        else if (numBMI < 24.9 && numBMI > 18.5)
        {
            mBMIcategory.setText("Normal");
            background.setBackgroundColor(R.drawable.green_ok);
            imageView.setImageResource(R.drawable.ok);
        }
        else if (numBMI < 29.9 && numBMI > 25)
        {
            mBMIcategory.setText("Overweight");
            background.setBackgroundColor(R.drawable.orange_mild);
            imageView.setImageResource(R.drawable.warning);
        }
        else
        {
            mBMIcategory.setText("Obese Class I");
            background.setBackgroundColor(R.drawable.red_warning);
            imageView.setImageResource(R.drawable.warning);
        }

        mGender.setText(intent.getStringExtra("gender"));
        mBMIdisplay.setText(bmi);

        recalculateBMI.setOnClickListener(v ->
        {
            Intent intent = new Intent(bmiActivity.this, GuestMainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }
}