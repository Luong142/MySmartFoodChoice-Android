package com.example.myfoodchoice.UserFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfoodchoice.R;
import com.example.myfoodchoice.AuthenticationActivity.UserProfileCreateFirstActivity;
import com.example.myfoodchoice.UserActivity.UserProfileUpdateActivity;

public class UserProfileViewFragment extends Fragment
{
    // TODO: declare UI components
    Button updateProfileBtn;

    TextView displayUserProfile;

    static final String TAG = "UserProfileViewFragment";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);



        // TODO: init UI components
        //createProfileBtn  = view.findViewById(R.id.createProfileBtn);
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        displayUserProfile = view.findViewById(R.id.displayUserProfile);

        // add onClickListener
        //createProfileBtn.setOnClickListener(onCreateUserProfileListener());
        updateProfileBtn.setOnClickListener(onUpdateUserProfileListener());

    }

    private View.OnClickListener onUpdateUserProfileListener()
    {
        return v ->
        {
            Intent intent = new Intent(getActivity(), UserProfileUpdateActivity.class);
            startActivity(intent);
            // no finish()
        };
    }

    private View.OnClickListener onCreateUserProfileListener()
    {
        return v ->
        {
            Intent intent = new Intent(getActivity(), UserProfileCreateFirstActivity.class);
            startActivity(intent);
            // no finish()
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_view_profile, container, false);
    }
}