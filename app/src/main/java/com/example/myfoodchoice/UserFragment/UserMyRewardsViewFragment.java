package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfoodchoice.R;


public class UserMyRewardsViewFragment extends Fragment
{
    /* fixme:Is it possible for you to add a ‘My Rewards’ page after you redeem the rewards.
    Because right now user are unable to see the rewards they have claimed.
     */
    private static final String TAG = "UserMyRewardsFragment";




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_my_rewards_view, container, false);
    }
}