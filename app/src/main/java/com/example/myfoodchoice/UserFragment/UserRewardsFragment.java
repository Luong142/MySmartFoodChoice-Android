package com.example.myfoodchoice.UserFragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.Adapter.RewardUserAdapter;
import com.example.myfoodchoice.AdapterInterfaceListener.OnRewardItemRedeemClickListener;
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


public class UserRewardsFragment extends Fragment implements OnRewardItemRedeemClickListener
{
    // TODO: declare components

    private static final String TAG = "UserRewardsFragment";

    ImageView userProfileImage;

    TextView fullNameTextView, userPointsTextView;

    RecyclerView rewardRecyclerView;

    RewardUserAdapter rewardUserAdapter;

    private ArrayList<Reward> rewardList;


    // TODO: declare firebase components here
    final static String LABEL = "Registered Accounts"; // FIXME: the path need to access the account.

    DatabaseReference databaseReferenceUserProfile;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID;

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
            databaseReferenceUserProfile = firebaseDatabase.getReference("User Profile").child(userID);
            databaseReferenceUserProfile.addListenerForSingleValueEvent(valueUserProfileEventListener());
        }

        // TODO: init UI components
        userProfileImage = view.findViewById(R.id.userProfileImageView);
        fullNameTextView = view.findViewById(R.id.userNameTextView);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);

        // Initialize the recipeList
        rewardList = new ArrayList<>();
        populateRewardList();

        // for recycle view
        rewardRecyclerView = view.findViewById(R.id.rewardsRecyclerView);
        rewardUserAdapter = new RewardUserAdapter(rewardList, this);
        setAdapter();
        rewardRecyclerView.setVerticalScrollBarEnabled(true);

        // Set the adapter to the RecyclerView
        // recipeRecyclerView.setAdapter(adapter);

        // TODO: Populate the recipeList with your Recipe data


        // Notify the adapter that the data has changed
        // recipeItemAdapter.notifyDataSetChanged();

    }


    @Override
    public void onRewardItemRedeemClick(int position) // click on button not the entire view.
    {
        // TODO: implement onClick
        Toast.makeText(getContext(), "Redeem is clicked, pls update this next", Toast.LENGTH_SHORT).show();
    }

    private void populateRewardList()
    {
        // init here
        Reward reward = new Reward("Discount",
                "10% off for Disney Land trip", R.drawable.discount, 1000);
        Reward reward1 = new Reward("Discount",
                "50% off for RTX 4090", R.drawable.discount, 9000);

        Reward reward2 = new Reward("Discount",
                "20% off for Premium User Account", R.drawable.discount, 3000);

        Reward reward3 = new Reward("Discount",
                "Voucher to get free plastic bottle", R.drawable.voucher, 2000);

        Reward reward4 = new Reward("Discount",
                "Voucher to get free orange", R.drawable.voucher, 2000);

        Reward reward5 = new Reward("Discount",
                "Voucher to get free apple", R.drawable.voucher, 2000);

        Reward reward6 = new Reward("Discount",
                "Voucher to get free banana", R.drawable.voucher, 2000);

        Reward reward7 = new Reward("Discount",
                "Voucher to get free pear", R.drawable.voucher, 2000);

        // add here
        rewardList.add(reward);
        rewardList.add(reward1);
        rewardList.add(reward2);
        rewardList.add(reward3);
        rewardList.add(reward4);
        rewardList.add(reward5);
        rewardList.add(reward6);
        rewardList.add(reward7);
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueUserProfileEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // FIXME: the problem is that userProfile is null.
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                // Log.d(TAG, "onDataChange: " + userProfile);

                // set the first name and last name in the UI
                if (userProfile != null)
                {
                    // set full name here
                    String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                    fullNameTextView.setText(fullName);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText
                        (getContext(),
                                "Error database connection", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "loadUserProfile:onCancelled ", error.toException());
            }
        };
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        rewardRecyclerView.setLayoutManager(layoutManager);
        rewardRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rewardRecyclerView.setAdapter(rewardUserAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_rewards, container, false);
    }
}