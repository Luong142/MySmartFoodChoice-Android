package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnHealthTipsClickListener;
import com.example.myfoodchoice.AdapterRecyclerView.HealthTipsUserAdapter;
import com.example.myfoodchoice.ModelDietitian.HealthTips;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;


public class UserViewHealthTipsFragment extends Fragment implements OnHealthTipsClickListener
{
    // todo: declare firebase here
    static final String PATH_HEALTH_TIPS = "Dietitian Health Tips";
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.
    static final String TAG = "UserViewHealthTipsFragment";

    DatabaseReference databaseReferenceHealthTips;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    HealthTips healthTips;

    String userID;

    // TODO: declare components
    RecyclerView healthTipsRecyclerView;

    HealthTipsUserAdapter healthTipsUserAdapter;

    private ArrayList<HealthTips> healthTipsArrayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // Initialize the recipeList
            healthTipsArrayList = new ArrayList<>();

            databaseReferenceHealthTips = firebaseDatabase.getReference(PATH_HEALTH_TIPS);

            databaseReferenceHealthTips.addChildEventListener(onChildHealthTipsListener());
        }
        // TODO: init UI component.
        // for init recycle view component
        healthTipsRecyclerView = view.findViewById(R.id.healthTipsRecyclerView);
        healthTipsUserAdapter = new HealthTipsUserAdapter(healthTipsArrayList, this);
        setAdapter();
        healthTipsRecyclerView.setVerticalScrollBarEnabled(true);
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener onChildHealthTipsListener()
    {
        return new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    healthTips = dataSnapshot.getValue(HealthTips.class);
                    if (healthTips != null)
                    {
                        if (healthTips.getUserKey().equals(userID))
                        {
                            healthTipsArrayList.add(healthTips);
                            healthTipsUserAdapter.notifyItemInserted(healthTipsArrayList.size() - 1);
                        }
                    }
                    else
                    {
                        Log.d(TAG, "onChildAdded: " + "null");
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    healthTips = dataSnapshot.getValue(HealthTips.class);
                    if (healthTips != null)
                    {
                        if (healthTips.getUserKey().equals(userID))
                        {
                            healthTipsArrayList.add(healthTips);
                            healthTipsUserAdapter.notifyItemChanged(healthTipsArrayList.size() - 1);
                        }
                    }
                    else
                    {
                        Log.d(TAG, "onChildChanged: " + "null");
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    healthTips = dataSnapshot.getValue(HealthTips.class);
                    if (healthTips != null)
                    {
                        if (healthTips.getUserKey().equals(userID))
                        {
                            healthTipsArrayList.remove(healthTips);
                            healthTipsUserAdapter.notifyItemRemoved(healthTipsArrayList.size() - 1);
                        }
                    }
                    else
                    {
                        Log.d(TAG, "onChildRemoved: " + "null");
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    healthTips = dataSnapshot.getValue(HealthTips.class);
                    if (healthTips != null)
                    {
                        if (healthTips.getUserKey().equals(userID))
                        {
                            healthTipsArrayList.add(healthTips);
                            healthTipsUserAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        Log.d(TAG, "onChildMoved: " + "null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    @Override
    public void onHealthTipsClick(int position)
    {
        // TODO: implement onClick
        // Toast.makeText(getContext(), "pls update this next", Toast.LENGTH_SHORT).show();
    }

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        healthTipsRecyclerView.setLayoutManager(layoutManager);
        healthTipsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        healthTipsRecyclerView.setAdapter(healthTipsUserAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_health_tips, container, false);
    }
}