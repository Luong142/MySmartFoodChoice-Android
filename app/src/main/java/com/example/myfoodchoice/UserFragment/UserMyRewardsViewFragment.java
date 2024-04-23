package com.example.myfoodchoice.UserFragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnAlrRedeemedRewardUserItemListener;
import com.example.myfoodchoice.AdapterRecyclerView.AlrRedeemedRewardUserAdapter;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.ModelUtilities.Reward;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;


public class UserMyRewardsViewFragment extends Fragment implements OnAlrRedeemedRewardUserItemListener {
    /* fixme:Is it possible for you to add a ‘My Rewards’ page after you redeem the rewards.
    Because right now user are unable to see the rewards they have claimed.
     */
    // TODO: declare firebase components here
    private static final String TAG = "UserMyRewardsFragment";

    DatabaseReference databaseReferenceUserProfile;
    
    final static String PATH_UserProfile = "User Profile";

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    ArrayList<Reward> rewardArrayList;

    String userID;

    // todo: init UI here
    ImageView userProfileImage;

    TextView fullNameTextView, userPointsTextView;

    RecyclerView alrRedeemedRewardRecyclerView;

    AlrRedeemedRewardUserAdapter alrRedeemedRewardUserAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components.

        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_UserProfile).child(userID);
            databaseReferenceUserProfile.addValueEventListener(valuePointUserProfileEventListener());

            rewardArrayList = new ArrayList<>();
        }

        // TODO: init UI components
        userProfileImage = view.findViewById(R.id.userProfileImageView);
        fullNameTextView = view.findViewById(R.id.userNameTextView);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);

        // todo: set the adapter first.
        alrRedeemedRewardRecyclerView = view.findViewById(R.id.rewardsAlrRedeemRecyclerView);
        alrRedeemedRewardUserAdapter = new AlrRedeemedRewardUserAdapter(rewardArrayList,
                this);
        setAdapter();
        alrRedeemedRewardRecyclerView.setVerticalScrollBarEnabled(true);


    }

    @Override
    public void onClickRedeemedReward(int position)
    {
        // todo: on click implement it here.



    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valuePointUserProfileEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userProfile = snapshot.getValue(UserProfile.class);
                // FIXME: the problem is that userProfile is null.
                // Log.d(TAG, "onDataChange: " + userProfile);

                // set the first name and last name in the UI
                if (userProfile != null)
                {
                    // set full name here
                    String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                    fullNameTextView.setText(fullName);

                    // todo: set point here
                    userPointsTextView.setText(String.valueOf(userProfile.getPoints()));

                    // set profile picture here
                    String profileImageUrl = userProfile.getProfileImageUrl();
                    Uri profileImageUri = Uri.parse(profileImageUrl);
                    // FIXME: the image doesn't show because the image source is from Gallery within android device.
                    // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                    Picasso.get()
                            .load(profileImageUri)
                            .resize(150, 150)
                            .error(R.drawable.error)
                            .into(userProfileImage);

                    // todo: if the user alr redeem should add it in to UserProfile in array list.
                    rewardArrayList.addAll(userProfile.getAlrRedeemedRewardList());
                    alrRedeemedRewardUserAdapter.notifyItemChanged(rewardArrayList.size() - 1);
                    // todo: testing done.
                    // Log.d(TAG, "NAH HERE: " + rewardArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        alrRedeemedRewardRecyclerView.setLayoutManager(layoutManager);
        alrRedeemedRewardRecyclerView.setItemAnimator(new DefaultItemAnimator());
        alrRedeemedRewardRecyclerView.setAdapter(alrRedeemedRewardUserAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_my_rewards_view, container, false);
    }
}