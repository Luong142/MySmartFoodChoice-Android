package com.example.myfoodchoice.GuestActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.R;

import java.util.Locale;

public class GuestBMIResultActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_guest_bmi_result);

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

        bmi = String.format(Locale.ROOT, "%.2f", numBMI);

        if (numBMI < 16)
        {
            mBMIcategory.setText("Underweight");
            background.setBackgroundColor(getColor(R.color.red));
            imageView.setImageResource(R.drawable.crosss);
        }
        else if (numBMI > 16 && numBMI < 16.9)
        {
            mBMIcategory.setText("Moderately Underweight");
            background.setBackgroundColor(getColor(R.color.red));
            imageView.setImageResource(R.drawable.warning);
        }
        else if (numBMI > 17 && numBMI < 18.4)
        {
            mBMIcategory.setText("Moderately Underweight");
            background.setBackgroundColor(getColor(R.color.orange));
            imageView.setImageResource(R.drawable.warning);
        }

        else if (numBMI < 25 && numBMI > 18.4)
        {
            mBMIcategory.setText("Normal");
            background.setBackgroundColor(getColor(R.color._light_green));
            imageView.setImageResource(R.drawable.ok);
        }
        else if (numBMI < 29.4 && numBMI > 25)
        {
            mBMIcategory.setText("Overweight");
            background.setBackgroundColor(getColor(R.color.orange));
            imageView.setImageResource(R.drawable.warning);
        }
        else
        {
            mBMIcategory.setText("Obese Class I");
            background.setBackgroundColor(getColor(R.color.red));
            imageView.setImageResource(R.drawable.warning);
        }

        mGender.setText(intent.getStringExtra("gender"));
        mBMIdisplay.setText(bmi);

        recalculateBMI.setOnClickListener(v ->
        {
            Intent intent = new Intent(GuestBMIResultActivity.this, GuestMainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }
}