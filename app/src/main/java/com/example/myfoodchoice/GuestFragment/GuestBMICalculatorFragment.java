package com.example.myfoodchoice.GuestFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.AuthenticationActivity.RegisterGuestActivity;
import com.example.myfoodchoice.GuestActivity.GuestBMIResultActivity;
import com.example.myfoodchoice.R;

import org.jetbrains.annotations.Contract;


public class GuestBMICalculatorFragment extends Fragment
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init UI components
        calculateBMIBtn = view.findViewById(R.id.calculateBMIBtn); // button

        mCurrentHeight = view.findViewById(R.id.currentHeight); // textview
        mCurrentWeight = view.findViewById(R.id.currentWeight); // textview
        mCurrentAge = view.findViewById(R.id.currentAge); // textview
        signUpClickableText = view.findViewById(R.id.signUpClickableText); // textview

        mIncrementAge = view.findViewById(R.id.incrementAge); // imageview
        mIncrementWeight = view.findViewById(R.id.incrementWeight); // imageview
        mDecrementWeight = view.findViewById(R.id.decrementWeight); // imageview
        mDecrementAge = view.findViewById(R.id.decrementAge); // imageview

        mSeekbarHeight = view.findViewById(R.id.seekbarForHeight); // seekbar
        mSeekbarHeight.setMax(300);
        mSeekbarHeight.setProgress(170);
        mSeekbarHeight.setOnSeekBarChangeListener(onSeekBarChangeListener());

        male = view.findViewById(R.id.male); // linear layout
        female = view.findViewById(R.id.female); // linear layout

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

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCalculateBMIListener()
    {
        {
            return v ->
            {
                if (gender.equals("null"))
                {
                    Toast.makeText(getContext(), "Please select your gender.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mIntHeightProgress.equals("0"))
                {
                    Toast.makeText(getContext(), "Please select your height.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (numAge == 0 || numAge < 0)
                {
                    Toast.makeText(getContext(), "Age is incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (numWeight == 0 || numWeight < 0)
                {
                    Toast.makeText(getContext(), "Weight is incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getContext(), GuestBMIResultActivity.class);
                Log.d("GuestMainMenuActivity", "Navigating to bmi page!");
                intent.putExtra("gender", gender);
                intent.putExtra("height", mIntHeightProgress);
                intent.putExtra("weight", weight);
                intent.putExtra("age", age);
                startActivity(intent);
            };
        }
    }

    @NonNull
    @Contract(" -> new")
    private ClickableSpan clickableLoginAsGuestNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("GuestMainMenuActivity", "Navigating to register page!");
                    Intent intent = new Intent(getContext(), RegisterGuestActivity.class);
                    startActivity(intent);
                }
            };
        }
    }

    @NonNull
    @Contract(" -> new")
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

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onMaleClickListener()
    {
        return v ->
        {
            male.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemale_focused));
            female.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemale_notfocused));
            gender = "Male";
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onFemaleClickListener()
    {
        return v ->
        {
            male.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemale_notfocused));
            female.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.malefemale_focused));
            gender = "Female";
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_b_m_i_calculator, container, false);
    }
}