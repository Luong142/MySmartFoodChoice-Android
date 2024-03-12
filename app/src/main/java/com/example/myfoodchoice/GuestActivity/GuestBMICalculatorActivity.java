package com.example.myfoodchoice.GuestActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodchoice.AuthenticationActivity.RegisterUserActivity;
import com.example.myfoodchoice.R;

public class GuestBMICalculatorActivity extends AppCompatActivity
{
    // TODO: declare UI components

    private Button calculateBMIBtn;

    private TextView mCurrentHeight, mCurrentWeight, mCurrentAge, signUpClickableText;

    private SpannableString spannableString;

    private ImageView mIncrementAge, mIncrementWeight, mDecrementWeight, mDecrementAge;

    private SeekBar mSeekbarHeight;

    private LinearLayout male, female;

    private int numWeight = 55;
    private int numAge = 22;

    private int currentProgress;

    private String mIntHeightProgress = "170";

    String gender = "null";

    String weight = "55";

    String age = "22";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_bmi_calculator);
        // Objects.requireNonNull(getSupportActionBar()).hide();

        // TODO: init UI components

        calculateBMIBtn = findViewById(R.id.calculateBMIBtn); // button

        mCurrentHeight = findViewById(R.id.currentHeight); // textview
        mCurrentWeight = findViewById(R.id.currentWeight); // textview
        mCurrentAge = findViewById(R.id.currentAge); // textview
        signUpClickableText = findViewById(R.id.signUpClickableText); // textview

        mIncrementAge = findViewById(R.id.incrementAge); // imageview
        mIncrementWeight = findViewById(R.id.incrementWeight); // imageview
        mDecrementWeight = findViewById(R.id.decrementWeight); // imageview
        mDecrementAge = findViewById(R.id.decrementAge); // imageview

        mSeekbarHeight = findViewById(R.id.seekbarForHeight); // seekbar
        mSeekbarHeight.setMax(300);
        mSeekbarHeight.setProgress(170);
        mSeekbarHeight.setOnSeekBarChangeListener(onSeekBarChangeListener());

        male = findViewById(R.id.male); // linear layout
        female = findViewById(R.id.female); // linear layout

        male.setOnClickListener(onMaleClickListener());
        female.setOnClickListener(onFemaleClickListener());

        // age and weight image click for increment or decrement
        mIncrementAge.setOnClickListener(v ->
        {
            numAge++;
            age = String.valueOf(numAge);
            mCurrentAge.setText(age); // set textview
        });
        mDecrementAge.setOnClickListener(v ->
        {
           numAge--;
           age = String.valueOf(numAge);
           mCurrentAge.setText(age); // set textview
        });

        mIncrementWeight.setOnClickListener(v ->
        {
           numWeight++;
           weight = String.valueOf(numWeight);
           mCurrentWeight.setText(weight);
        });

        mDecrementWeight.setOnClickListener(v ->
        {
           numWeight--;
           weight = String.valueOf(numWeight);
           mCurrentWeight.setText(weight);
        });

        // for sign up text
        spannableString = new SpannableString(signUpClickableText.getText());
        spannableString.setSpan(clickableLoginAsGuestNavSpan(), 0, spannableString.length(), 0);
        signUpClickableText.setText(spannableString);
        signUpClickableText.setMovementMethod(LinkMovementMethod.getInstance());

        // for bmi calculation button
        calculateBMIBtn.setOnClickListener(onCalculateBMIListener());
    }

    private View.OnClickListener onCalculateBMIListener()
    {
        {
            return v ->
            {
                if (gender.equals("null"))
                {
                    Toast.makeText(GuestBMICalculatorActivity.this, "Please select your gender.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mIntHeightProgress.equals("0"))
                {
                    Toast.makeText(GuestBMICalculatorActivity.this, "Please select your height.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (numAge == 0 || numAge < 0)
                {
                    Toast.makeText(GuestBMICalculatorActivity.this, "Age is incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (numWeight == 0 || numWeight < 0)
                {
                    Toast.makeText(GuestBMICalculatorActivity.this, "Weight is incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(GuestBMICalculatorActivity.this, GuestBMIResultActivity.class);
                Log.d("GuestMainMenuActivity", "Navigating to bmi page!");
                intent.putExtra("gender", gender);
                intent.putExtra("height", mIntHeightProgress);
                intent.putExtra("weight", weight);
                intent.putExtra("age", age);
                startActivity(intent);
                finish(); // close this activity and go back to previous activity
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
                    Log.d("GuestMainMenuActivity", "Navigating to register page!");
                    Intent intent = new Intent(GuestBMICalculatorActivity.this, RegisterUserActivity.class);
                    startActivity(intent);
                    finish(); // close this activity and go back to previous activity (LoginActivity)
                }
            };
        }
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener()
    {
        return new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                currentProgress = progress;
                mIntHeightProgress = String.valueOf(currentProgress);
                mCurrentHeight.setText(mIntHeightProgress); // set textview to current progress (170 cm)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        };
    }

    private View.OnClickListener onMaleClickListener()
    {
        return v ->
        {
            male.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_focused));
            female.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_notfocused));
            gender = "Male";
        };
    }

    private View.OnClickListener onFemaleClickListener()
    {
        return v ->
        {
            male.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_notfocused));
            female.setBackground(ContextCompat.getDrawable(this, R.drawable.malefemale_focused));
            gender = "Female";
        };
    }
}