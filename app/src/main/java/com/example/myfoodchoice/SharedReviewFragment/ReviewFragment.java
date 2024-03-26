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
import java.util.Objects;


public class ReviewFragment extends Fragment implements OnReviewClickListener
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

    final static String PATH_REVIEW = "Reviews";

    DatabaseReference databaseReferenceUserProfile;

    DatabaseReference databaseReferenceAccount;

    DatabaseReference databaseReferenceReview;

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
            databaseReferenceReview = firebaseDatabase.getReference(PATH_REVIEW).child(userID);
            databaseReferenceReview.addChildEventListener(valueChildReviewEventListener());
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
        // populateReviewList();

        // for recycle view
        reviewRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        sharedReviewAdapter = new SharedReviewAdapter(reviewArrayList, this);
        setAdapter();
        reviewRecyclerView.setVerticalScrollBarEnabled(true);
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener valueChildReviewEventListener()
    {
        return new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                Review review = snapshot.getValue(Review.class);
                if (review != null)
                {
                    reviewArrayList.add(review);
                    if (sharedReviewAdapter != null)
                    {
                        // todo: notify to update instantly to the UI.
                        sharedReviewAdapter.notifyItemInserted(reviewArrayList.size() - 1);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                // todo: unless we want to update the review, we can just ignore it.
                // todo: for future use if we want to update
                Review review = snapshot.getValue(Review.class);
                if (review != null)
                {
                    reviewArrayList.set(findReviewIndexById(review.getKey()), review);
                    if (sharedReviewAdapter != null)
                    {
                        // todo: notify to update instantly to the UI.
                        sharedReviewAdapter.notifyItemChanged(findReviewIndexById(review.getKey()));
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                // todo: unless we want to remove, we can ignore this
                // todo: for future use if we want to remove
                Review review = snapshot.getValue(Review.class);
                if (review != null)
                {
                    reviewArrayList.remove(findReviewIndexById(review.getKey()));
                    if (sharedReviewAdapter != null)
                    {
                        // todo: notify to update instantly to the UI.
                        sharedReviewAdapter.notifyItemRemoved(findReviewIndexById(review.getKey()));
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
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
            if (isAdded()) {
                ReviewDialogFragment reviewDialogFragment = new ReviewDialogFragment();
                reviewDialogFragment.show(getChildFragmentManager(), "ReviewDialogFragment");
            } else {
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