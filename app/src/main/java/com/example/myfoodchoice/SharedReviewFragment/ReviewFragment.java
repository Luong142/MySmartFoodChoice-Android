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

import com.example.myfoodchoice.AdapterInterfaceListener.OnReviewClickListener;
import com.example.myfoodchoice.AdapterRecyclerView.SharedReviewAdapter;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.ModelUtilities.Review;
import com.example.myfoodchoice.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;


public class ReviewFragment extends Fragment implements OnReviewClickListener
{
    // TODO: declare components

    private static final String TAG = "SharedReviewFragment";

    FloatingActionButton createReviewBtn;

    ImageView profileImage;

    TextView fullNameTextView, userRoleTextView;

    RecyclerView reviewRecyclerView;

    SharedReviewAdapter sharedReviewAdapter;

    private ArrayList<Review> reviewArrayList;


    // TODO: declare firebase components here
    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_ACCOUNT = "Registered Accounts";

    final static String PATH_REVIEW = "Reviews";

    final static String PATH_BUSINESS_PROFILE = "Business Profile";

    DatabaseReference databaseReferenceUserProfile;

    DatabaseReference databaseReferenceAccount;

    DatabaseReference databaseReferenceReviewUserID;

    DatabaseReference databaseReferenceReview;

    DatabaseReference databaseReferenceBusinessProfile;

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

            // todo: init database reference for review
            databaseReferenceReviewUserID = firebaseDatabase.getReference(PATH_REVIEW).child(userID);
            databaseReferenceReview = firebaseDatabase.getReference(PATH_REVIEW);

            databaseReferenceReview.addChildEventListener(valueReviewEventListener());
            // databaseReferenceReview.addChildEventListener(valueChildReviewEventListener());

            // todo: init database reference for business profile
            databaseReferenceBusinessProfile = firebaseDatabase.getReference(PATH_BUSINESS_PROFILE).child(userID);
            databaseReferenceBusinessProfile.addListenerForSingleValueEvent(valueBusinessProfileEventListener());
         }

        // TODO: init UI components
        profileImage = view.findViewById(R.id.userProfileImageView);
        fullNameTextView = view.findViewById(R.id.userNameTextView);
        userRoleTextView = view.findViewById(R.id.userRoleTextView);
        createReviewBtn = view.findViewById(R.id.createReviewBtn);

        // set on click button
        createReviewBtn.setOnClickListener(onCreateReviewListener());

        // Initialize the recipeList
        reviewArrayList = new ArrayList<>();
        // populateReviewList();

        // for recycle view
        reviewRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        sharedReviewAdapter = new SharedReviewAdapter(reviewArrayList, this);
        setAdapter();
        reviewRecyclerView.setVerticalScrollBarEnabled(true);
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener valueReviewEventListener()
    {
        return new ChildEventListener()
        {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                reviewArrayList.clear();
                // todo: this one is ok
                for (DataSnapshot reviewSnapshot : snapshot.getChildren())
                {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null)
                    {
                        reviewArrayList.add(review);
                        if (sharedReviewAdapter != null)
                        {
                            sharedReviewAdapter.notifyItemInserted(reviewArrayList.size() - 1);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                reviewArrayList.clear();
                // fixme: this one haven't tested yet.
                for (DataSnapshot reviewSnapshot : snapshot.getChildren())
                {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null)
                    {
                        reviewArrayList.add(review);
                        if (sharedReviewAdapter != null)
                        {
                            sharedReviewAdapter.notifyItemChanged(findReviewIndexById(review.getKey()));
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                reviewArrayList.clear();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren())
                {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null)
                    {
                        reviewArrayList.add(review);
                        if (sharedReviewAdapter != null)
                        {
                            sharedReviewAdapter.notifyItemChanged(findReviewIndexById(review.getKey()));
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private int findReviewIndexById(String reviewId)
    {
        // todo: the purpose is to find the correct review in this list based on the key attribute.
        for (int i = 0; i < reviewArrayList.size(); i++)
        {
            if (reviewArrayList.get(i).getKey().equals(reviewId))
            {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateReviewListener()
    {
        return v ->
        {
            // Assuming this code is inside a fragment method
            if (isAdded())
            {
                ReviewDialogFragment reviewDialogFragment = new ReviewDialogFragment();
                reviewDialogFragment.show(getChildFragmentManager(), "ReviewDialogFragment");
            }
            else
            {
                Log.e(TAG, "Fragment not attached to an activity, cannot show dialog");
                // Handle the situation appropriately, e.g., by showing a message to the user
            }

        };
    }

    @Override
    public void onReviewClick(int position)
    {
        // TODO: implement onClick
        // Toast.makeText(getContext(), "pls update this next", Toast.LENGTH_SHORT).show();

    }

    private void populateReviewList()
    {
        // create 5 more reviews here
        Review review = new Review("Test", "full name here", 5);

        reviewArrayList.add(review);
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
    private ValueEventListener valueBusinessProfileEventListener()
    {
        return new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                BusinessProfile businessProfile = snapshot.getValue(BusinessProfile.class);
                // Log.d(TAG, "onDataChange: " + userProfile);

                // set the first name and last name in the UI
                if (businessProfile != null)
                {
                    // set full name here
                    String fullName = businessProfile.getFirstName() + " " + businessProfile.getLastName();
                    fullNameTextView.setText(fullName);

                    // set profile picture here
                    String profileImageUrl = businessProfile.getProfileImageUrl();
                    Uri profileImageUri = Uri.parse(profileImageUrl);
                    // FIXME: the image doesn't show because the image source is from Gallery within android device.
                    // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                    Picasso.get()
                            .load(profileImageUri)
                            .resize(150, 150)
                            .error(R.drawable.error)
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

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
                            .into(profileImage);
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