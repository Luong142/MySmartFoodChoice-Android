package com.example.myfoodchoice.GuestFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

public class GuestUpgradeAccountFragment extends Fragment
{
    // TODO: here is our plan: make the guest account can be upgraded to user account for more accessing features.
    // TODO: this fragment should be able to upgrade by changing the account type in the realtime database.
    // TODO: 3 days trial too

    TextView currentDateTrial, endDateTrial;

    Button upgradeToUserButton;

    DatabaseReference databaseReferenceGuest;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;

    String userId;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init Firebase database, paste the correct link as reference.
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        // TODO: init UI component
        currentDateTrial = view.findViewById(R.id.currentDateTextView);
        endDateTrial = view.findViewById(R.id.endDateTextView);
        upgradeToUserButton = view.findViewById(R.id.upgradeBtn);





        // set on click
        upgradeToUserButton.setOnClickListener(onUpgradeToUserListener());

    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpgradeToUserListener()
    {
        return v ->
        {
            // TODO: implement update here.



        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_upgrade_account, container, false);
    }
}