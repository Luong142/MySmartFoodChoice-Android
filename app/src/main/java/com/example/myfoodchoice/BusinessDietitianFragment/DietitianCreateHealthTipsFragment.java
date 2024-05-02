package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

public class DietitianCreateHealthTipsFragment extends Fragment
{
    // todo: declare firebase here
    static final String PATH_HEALTH_TIPS = "Dietitian Health Tips";
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.
    static final String TAG = "DietitianCreateHealthTipsFragment";

    DatabaseReference databaseReferenceHealthTips, databaseReferenceHealthTipsChild;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String dietitianId;

    // todo: declare UI components
    EditText titleHealthTips, contentHealthTips;

    Button generateAIContentBtn, createHealthTipsBtn;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);





        // todo: init UI components
        titleHealthTips = view.findViewById(R.id.titleHealthTips);
        contentHealthTips = view.findViewById(R.id.contentHealthTips);
        generateAIContentBtn = view.findViewById(R.id.generateAIContentBtn);
        createHealthTipsBtn = view.findViewById(R.id.createHealthTipsBtn);


        // todo: set on click for all buttons
        generateAIContentBtn.setOnClickListener(onGenerateAIContentListener());
        createHealthTipsBtn.setOnClickListener(onCreateHealthTipsListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateHealthTipsListener()
    {
        return v ->
        {

        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onGenerateAIContentListener()
    {
        return v ->
        {

        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_create_health_tips, container, false);
    }
}