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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class UserCheckInFragment extends Fragment implements OnDailyCheckInListener
{
    // TODO: declare firebase components here
    final static String PATH_ACCOUNT = "Registered Accounts"; // FIXME: the path need to access the account.

    DatabaseReference databaseReferenceUserProfile,
            databaseReferenceRewards,
            databaseReferenceCheckInDate;

    final static String PATH_REWARDS = "Rewards";

    final static String PATH_UserProfile = "User Profile";

    final static String PATH_CheckInDate = "Check In";
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

            databaseReferenceUserProfile.addValueEventListener(valuePointUserProfileEventListener());
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
        dayList.clear();
        populateDayList();
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
            updates.put("points", userProfile.getPoints() + 10);
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
    private void populateDayList()
    {
        // fixme: the problem is that the init will reset this list.
        if (dayList.isEmpty())
        {
            // we need a timer to reset this after 1 day.
            CheckInDay checkInDay = new CheckInDay(R.drawable.check_in, "Day 1");
            CheckInDay checkInDay1 = new CheckInDay(R.drawable.check_in, "Day 2");
            CheckInDay checkInDay2 = new CheckInDay(R.drawable.check_in, "Day 3");
            CheckInDay checkInDay3 = new CheckInDay(R.drawable.check_in, "Day 4");
            CheckInDay checkInDay4 = new CheckInDay(R.drawable.check_in, "Day 5");
            CheckInDay checkInDay5 = new CheckInDay(R.drawable.check_in, "Day 6");
            CheckInDay checkInDay6 = new CheckInDay(R.drawable.check_in, "Day 7");


            dayList.add(checkInDay);
            dayList.add(checkInDay1);
            dayList.add(checkInDay2);
            dayList.add(checkInDay3);
            dayList.add(checkInDay4);
            dayList.add(checkInDay5);
            dayList.add(checkInDay6);
        }
    }

    private void resetDayListAfterSevenDays()
    {
        // Schedule the Runnable to be executed after 7 days
        handler.postDelayed(() ->
        {
            // reset the dayList
            dayList.clear();
            populateDayList();

            // If you want to reset the list again after 7 days, you can schedule this Runnable again
            resetDayListAfterSevenDays();
        }, 7 * 24 * 60 * 60 * 1000); // 7 days in milliseconds
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