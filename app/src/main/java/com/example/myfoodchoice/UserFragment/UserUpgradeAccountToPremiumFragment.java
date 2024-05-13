package com.example.myfoodchoice.UserFragment;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.example.myfoodchoice.UserActivity.UserPaymentActivity;

import org.jetbrains.annotations.Contract;

public class UserUpgradeAccountToPremiumFragment extends Fragment
{

    static final String TAG = "UserUpgradeAccountToPremiumFragment";
    static final String PREMIUM_USER = "Premium User";

    // todo: the user has two tiers: non-premium and premium.
    //  the user can upgrade to premium by paying a certain amount of money.
    // todo: declare UI

    TextView advertiseText;

    Button upgradeBtn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // todo: init UI
        advertiseText = view.findViewById(R.id.advertiseText);
        upgradeBtn = view.findViewById(R.id.upgradeBtn);
        String advertise = "Upgrade to Premium for:\n" +
                "- Exclusive virtual assistant service\n" +
                "- Special discounts and vouchers\n" +
                "- Personalized daily check-in\n" +
                "Elevate your experience now!";
        advertiseText.setText(advertise);
        // todo: do this tmr!

        // upgrade here
        upgradeBtn.setOnClickListener(onUpgradeAccountListener());
    }


    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpgradeAccountListener()
    {
        return v ->
        {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Proceed to Payment?");
            alertDialog.setMessage("Perform payment to upgrade your account now.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                    (dialog, which) ->
                    {
                        // dismiss and move the guest user to login page.
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), UserPaymentActivity.class);
                        startActivity(intent);
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            // API here
            // paymentFlow();
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_upgrade_account_to_premium, container, false);
    }
}