package com.example.myfoodchoice.UserFragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.myfoodchoice.AdapterInterfaceListener.OnDailyCheckInListener;
import com.example.myfoodchoice.AdapterRecyclerView.CheckInDayAdapter;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.ModelUtilities.CheckInDay;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


public class UserCheckInFragment extends Fragment implements OnDailyCheckInListener
{
    // TODO: declare firebase components here
    final static String PATH_ACCOUNT = "Registered Accounts"; // FIXME: the path need to access the account.

    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceRewards,
            databaseReferenceCheckInDate, databaseReferenceDay;

    final static String PATH_REWARDS = "Rewards";

    final static String PATH_UserProfile = "Android User Profile";

    final static String PATH_CheckInDate = "Check In";

    final static String PATH_DAY = "Day Item";

    private static final String TAG = "UserCheckInFragment";

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID;

    // TODO: declare components

    RecyclerView dayRecyclerView;

    CheckInDayAdapter dayAdapter;

    private ArrayList<CheckInDay> dayList;

    Date lastCheckInDate, currentDate;

    TextView currentDateTextView, fullNameTextView, userPointsTextView;

    ImageView userProfileImage;

    LocalDateTime now;
    private Handler handler;

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
            databaseReferenceRewards = firebaseDatabase.getReference(PATH_REWARDS).child(userID);
            databaseReferenceCheckInDate = firebaseDatabase.getReference(PATH_CheckInDate).child(userID);
            databaseReferenceDay = firebaseDatabase.getReference(PATH_DAY).child(userID);

            databaseReferenceUserProfile.addValueEventListener(valuePointUserProfileEventListener());

            // firebaseStorage storage = FirebaseStorage.getInstance();
            generateDayData();

            databaseReferenceDay.addChildEventListener(valueEventListenerDay());
        }

        // init current date.
        now = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
        currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // TODO: init UI components
        currentDateTextView = view.findViewById(R.id.currentDateTextView);
        userProfileImage = view.findViewById(R.id.userProfileImageView);
        fullNameTextView = view.findViewById(R.id.userNameTextView);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);

        // set the current date.
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        stringBuilder.append(dateFormat.format(currentDate));
        currentDateTextView.setText(stringBuilder);

        // Initialize the recipeList
        dayList = new ArrayList<>();
        handler = new Handler();
        resetDayListAfterSevenDays();

        // for recycle view
        dayRecyclerView = view.findViewById(R.id.checkInDaysRecyclerView);
        dayAdapter = new CheckInDayAdapter(dayList, this);
        setAdapter();
        dayRecyclerView.setVerticalScrollBarEnabled(true);

        // TODO: Populate the recipeList with your Recipe data
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener valueEventListenerDay()
    {
        return new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                CheckInDay checkInDay = snapshot.getValue(CheckInDay.class);
                if (checkInDay != null)
                {
                    dayList.add(checkInDay);
                    // Log.d(TAG, "onChildAdded: " + checkInDay);
                    dayAdapter.notifyItemInserted(dayList.size() - 1);
                }
                else
                {
                    Log.d(TAG, "onChildAdded: " + "null");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };
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
                // todo: get the object and modify the point part.
                userProfile = snapshot.getValue(UserProfile.class);

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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.toException());
            }
        };
    }

    @Override
    public void onCheckInClick(int position)
    {
        // TODO: implement onClick
        // Toast.makeText(getContext(), "pls update this next: day click is: " + (position + 1),
        //                Toast.LENGTH_SHORT).show();

        // todo: one time only check in a day.
        lastCheckInDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        // init time counter

        HashMap<String, Object> updates = new HashMap<>();

        if (canCheckInToday())
        {
            // update the current date to the next day.
            LocalDateTime current = now.plusDays(1);
            currentDate = Date.from(current.atZone(ZoneId.systemDefault()).toInstant());

            // todo: per day is 50 points?
            updates.put("points", userProfile.getPoints() + 30);
            databaseReferenceUserProfile.updateChildren(updates, onCompleteCheckInPointListener());

            // fixme: to set the value for debugging.
            databaseReferenceCheckInDate.setValue(lastCheckInDate).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    databaseReferenceCheckInDate.setValue(currentDate).addOnCompleteListener(task1 ->
                    {
                        if (task1.isSuccessful())
                        {
                            Log.d(TAG, "onCheckInClick: check in date update successful");
                        }
                        else
                        {
                            Log.d(TAG, "onCheckInClick: check in date update failed");
                        }
                    });
                }
                else
                {
                    Log.d(TAG, "onCheckInClick: check in date update failed");
                }
            });

            // notify
            dayList.remove(position);
            dayAdapter.notifyItemRemoved(position);

            // Assuming each day's data is stored under a unique key in Firebase
            String dayKey = "Day " + (position++); // Adjust based on your Firebase structure

            // Remove the data for the specific day from Firebase
            databaseReferenceDay.child(dayKey).removeValue()
                    .addOnSuccessListener(aVoid ->
                    {
                        // Data successfully removed from Firebase
                        // Toast.makeText(getContext(), "Check-in removed successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                    {
                        // Handle any errors
                        Log.e(TAG, "Error removing check-in from Firebase", e);
                        // Toast.makeText(getContext(), "Failed to remove check-in", Toast.LENGTH_SHORT).show();
                    });
        }
        else
        {
            Toast.makeText(getContext(), "You have already checked in today", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Contract(" -> new")
    private DatabaseReference.CompletionListener onCompleteCheckInPointListener()
    {
        return (error, ref) ->
        {
            if (error != null)
            {
                Log.d(TAG, "onCompleteCheckInPointListener: check in point update failed");
                return;
            }

            Toast.makeText(getContext(), "Check in successful", Toast.LENGTH_SHORT).show();
        };
    }

    public boolean canCheckInToday()
    {
        return lastCheckInDate.equals(currentDate);
    }

    // todo: add in the timer after 7 days then reset this

    private void resetDayListAfterSevenDays()
    {
        // schedule the Runnable to be executed after 7 days
        handler.postDelayed(() ->
        {
            // reset the dayList
            dayList.clear();
            generateDayData();

            // ff you want to reset the list again after 7 days, you can schedule this Runnable again
            resetDayListAfterSevenDays();
        }, 7 * 24 * 60 * 60 * 1000); // 7 days in milliseconds
    }

    private void generateDayData()
    {
        StorageReference storageReference = FirebaseStorage.getInstance().
                getReference("Image Day/checked.svg");
        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
        {
            CheckInDay checkInDay = new CheckInDay("Day 1");
            CheckInDay checkInDay1 = new CheckInDay("Day 2");
            CheckInDay checkInDay2 = new CheckInDay("Day 3");
            CheckInDay checkInDay3 = new CheckInDay("Day 4");
            CheckInDay checkInDay4 = new CheckInDay("Day 5");
            CheckInDay checkInDay5 = new CheckInDay("Day 6");
            CheckInDay checkInDay6 = new CheckInDay("Day 7");

            checkInDay.setImageURL(uri.toString());
            checkInDay1.setImageURL(uri.toString());
            checkInDay2.setImageURL(uri.toString());
            checkInDay3.setImageURL(uri.toString());
            checkInDay4.setImageURL(uri.toString());
            checkInDay5.setImageURL(uri.toString());
            checkInDay6.setImageURL(uri.toString());

            // for day reference set value for them
            databaseReferenceDay.child("Day 1").setValue(checkInDay);
            databaseReferenceDay.child("Day 2").setValue(checkInDay1);
            databaseReferenceDay.child("Day 3").setValue(checkInDay2);
            databaseReferenceDay.child("Day 4").setValue(checkInDay3);
            databaseReferenceDay.child("Day 5").setValue(checkInDay4);
            databaseReferenceDay.child("Day 6").setValue(checkInDay5);
            databaseReferenceDay.child("Day 7").setValue(checkInDay6);
        }).addOnFailureListener(exception ->
        {
            // Handle any errors
            Log.e("FirebaseStorage", "Error getting download URL", exception);
        });
    }

    private void setAdapter() 
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        dayRecyclerView.setLayoutManager(layoutManager);
        dayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dayRecyclerView.setAdapter(dayAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_check_in, container, false);
    }
    
}