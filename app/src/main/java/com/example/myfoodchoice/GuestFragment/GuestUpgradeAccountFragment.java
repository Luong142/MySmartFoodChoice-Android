package com.example.myfoodchoice.GuestFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.Date;
import java.util.Locale;

public class GuestUpgradeAccountFragment extends Fragment
{
    // TODO: here is our plan: make the guest account can be upgraded to user account for more accessing features.
    // TODO: this fragment should be able to upgrade by changing the account type in the realtime database.
    // TODO: 3 days trial too

    TextView currentDateTrial, endDateTrial;

    Button upgradeToUserButton;

    DatabaseReference databaseReferenceGuest;

    static final String PATH_DATABASE = "Registered Accounts";

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;

    String userId;

    Account guestAccount;

    Intent intent;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init Firebase database, paste the correct link as reference.
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
        {
            userId = firebaseUser.getUid();
            databaseReferenceGuest = firebaseDatabase.getReference(PATH_DATABASE).child(userId);
        }

        // TODO: init UI component
        currentDateTrial = view.findViewById(R.id.currentDateTextView);
        endDateTrial = view.findViewById(R.id.endDateTextView);
        upgradeToUserButton = view.findViewById(R.id.upgradeBtn);

        // to display the current date and end date of the trial.
        databaseReferenceGuest.addListenerForSingleValueEvent(valueDisplayDateForTrialEvent());

        // set on click
        upgradeToUserButton.setOnClickListener(onUpgradeToUserListener());

    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueDisplayDateForTrialEvent()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                guestAccount = snapshot.getValue(Account.class);

                if (guestAccount != null)
                {
                    // update the current trial date.
                    guestAccount.updateTrialStartDate();

                    // get date
                    Date currentDateTrialDate = guestAccount.getCurrentDateTrial();
                    Date endDateTrialDate = guestAccount.getEndDateTrial();

                    String currentDate = formatDate(currentDateTrialDate);
                    String endDate = formatDate(endDateTrialDate);

                    // set text
                    currentDateTrial.setText(String.format("%s%s", "Current Date Trial: ", currentDate));
                    endDateTrial.setText(String.format("%s%s", "End Date Trial: ", endDate));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText
                        (getContext(), "Error database connection", Toast.LENGTH_SHORT).show();
                Log.w("LoginActivity", "loadUserProfile:onCancelled ", error.toException());
            }
        };
    }

    private String formatDate(Date date)
    {
        if (date == null)
        {
            return "N/A"; // default value if null
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
        return simpleDateFormat.format(date);
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueTrialOverEvent()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                guestAccount = snapshot.getValue(Account.class);
                // TODO: set the account type to "User" since the trial is over.
                if (guestAccount != null)
                {
                    guestAccount.setAccountType("User");
                    guestAccount.setGuestTrialActive(false);
                    guestAccount.setEndDateTrial(null);
                    guestAccount.setCurrentDateTrial(null);
                    databaseReferenceGuest.setValue(guestAccount);

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Upgrade Success");
                    alertDialog.setMessage("You have successfully upgraded to user account. \n" +
                            "Press confirm to proceed upgrading account. \n" +
                            "Or press cancel." );
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                            (dialog, which) ->
                            {
                                // dismiss and move the guest user to login page.
                                dialog.dismiss();
                                intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);

                                if (getActivity() != null)
                                {
                                    getActivity().finish();
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            (dialog, which) -> dialog.dismiss());

                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText
                        (getContext(), "Error database connection", Toast.LENGTH_SHORT).show();
                Log.w("LoginActivity", "loadUserProfile:onCancelled ", error.toException());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpgradeToUserListener()
    {
        return v ->
        {
            // TODO: implement update here.
            databaseReferenceGuest.addListenerForSingleValueEvent(valueTrialOverEvent());
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