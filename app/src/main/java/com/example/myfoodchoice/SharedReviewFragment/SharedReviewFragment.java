package com.example.myfoodchoice.SharedReviewFragment;

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

import com.example.myfoodchoice.Adapter.SharedReviewAdapter;
import com.example.myfoodchoice.AdapterInterfaceListener.OnReviewClickListener;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.ModelUtilities.Review;
import com.example.myfoodchoice.ModelUtilities.ReviewerType;
import com.example.myfoodchoice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class SharedReviewFragment extends Fragment implements OnReviewClickListener
{
    // TODO: declare components

    private static final String TAG = "SharedReviewFragment";

    FloatingActionButton createReviewBtn;

    ImageView userProfileImage;

    TextView fullNameTextView, userRoleTextView;

    RecyclerView reviewRecyclerView;

    SharedReviewAdapter sharedReviewAdapter;

    private ArrayList<Review> reviewArrayList;


    // TODO: declare firebase components here
    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_ACCOUNT = "Registered Accounts";

    DatabaseReference databaseReferenceUserProfile;

    DatabaseReference databaseReferenceAccount;

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
            databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_USERPROFILE).child(userID);
            databaseReferenceUserProfile.addListenerForSingleValueEvent(valueUserProfileEventListener());

            // Todo: init next database reference
            databaseReferenceAccount = firebaseDatabase.getReference(PATH_ACCOUNT).child(userID);
            databaseReferenceAccount.addListenerForSingleValueEvent(valueAccountEventListener());
        }

        // TODO: init UI components
        userProfileImage = view.findViewById(R.id.userProfileImageView);
        fullNameTextView = view.findViewById(R.id.userNameTextView);
        userRoleTextView = view.findViewById(R.id.userRoleTextView);
        createReviewBtn = view.findViewById(R.id.createReviewBtn);

        // set on click button
        createReviewBtn.setOnClickListener(onCreateReviewListener());

        // Initialize the recipeList
        reviewArrayList = new ArrayList<>();
        populateReviewList();

        // for recycle view
        reviewRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        sharedReviewAdapter = new SharedReviewAdapter(reviewArrayList, this);
        setAdapter();
        reviewRecyclerView.setVerticalScrollBarEnabled(true);


    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateReviewListener()
    {
        return v ->
        {
            // TODO: create new review here.
            Toast.makeText(getContext(), "pls update this next", Toast.LENGTH_SHORT).show();

        };
    }

    @Override
    public void onReviewClick(int position)
    {
        // TODO: implement onClick
        Toast.makeText(getContext(), "pls update this next", Toast.LENGTH_SHORT).show();

    }

    private void populateReviewList()
    {
        // create 5 more reviews here
        Review review = new Review("Good App", 5, ReviewerType.USER);
        Review review1 = new Review("Good App", 4, ReviewerType.DIETITIAN);
        Review review2 = new Review("Good App", 2, ReviewerType.TRAINER);
        Review review3 = new Review("Good App", 5, ReviewerType.USER);
        Review review4 = new Review("Good App", 5, ReviewerType.USER);
        Review review5 = new Review("Good App", 5, ReviewerType.USER);


        reviewArrayList.add(review);
        reviewArrayList.add(review1);
        reviewArrayList.add(review2);
        reviewArrayList.add(review3);
        reviewArrayList.add(review4);
        reviewArrayList.add(review5);
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueAccountEventListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Account account = snapshot.getValue(Account.class);
                // Log.d(TAG, "onDataChange: " + account);

                if (account != null)
                {
                    String role = account.getAccountType();
                    userRoleTextView.setText(role);
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
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(sharedReviewAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared_review, container, false);
    }
}