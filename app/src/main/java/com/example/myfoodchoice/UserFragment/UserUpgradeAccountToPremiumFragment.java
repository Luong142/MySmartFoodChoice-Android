package com.example.myfoodchoice.UserFragment;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfoodchoice.AuthenticationActivity.LoginActivity;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserUpgradeAccountToPremiumFragment extends Fragment
{
    // todo: https://www.youtube.com/watch?v=-1dX7mEV80M&ab_channel=CodewithArvind

    String secretKey = "sk_test_51PE9Gp2Lluf0ZsJHi8QSjt4aqh1ZDC31O1yDcMATa5lzTH95zmNP6fY6CHyim6DcxXze7ntfJtV5WFcUhpxOUUfE00SkNAEOHu";
    String publishableKey = "pk_test_51PE9Gp2Lluf0ZsJHLz9trebrLFYm75gSsTJXCYq4X0kG5BtrT6p21ydg6lR2SmRbo7gNIqkwYWQM15ysxpkgbkxN009EdwKmNc";

    String customerID;

    String emphericalKey;

    String clientSecret;

    StringRequest request;

    RequestQueue requestQueue;

    static final String TAG = "UserUpgradeAccountToPremiumFragment";
    static final String PREMIUM_USER = "Premium User";

    // todo: the user has two tiers: non-premium and premium.
    //  the user can upgrade to premium by paying a certain amount of money.
    DatabaseReference databaseReferenceUserAccounts;

    static final String PATH_DATABASE = "Registered Accounts";

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;

    String userId;

    Account userAccount;

    // todo: declare UI

    TextView advertiseText;

    Button upgradeBtn;

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
            databaseReferenceUserAccounts = firebaseDatabase.getReference(PATH_DATABASE).child(userId);

            databaseReferenceUserAccounts.addValueEventListener(valueAccountTypeListener());
        }

        // first request
        request = new StringRequest(StringRequest.Method.POST, "https://api.stripe.com/v1/customers",
                response ->
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);

                        customerID = jsonObject.getString("id");

                        Toast.makeText(getContext(), customerID, Toast.LENGTH_SHORT).show();

                        getEmphericalKey();
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                },
                error ->
                {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);

                return header;
            }
        };

        requestQueue = Volley.newRequestQueue(this.requireContext());
        requestQueue.add(request);

        // todo: init UI
        advertiseText = view.findViewById(R.id.advertiseText);
        upgradeBtn = view.findViewById(R.id.upgradeBtn);
        String advertise = "Unlock a world of benefits with our premium features\n" +
                "Enjoy a virtual assistant at your service, exclusive discounts and vouchers, and a daily check-in for a personalized experience.\n" +
                "Upgrade now and elevate your experience!";

        advertiseText.setText(advertise);

        // todo: do this tmr!

        // upgrade here
        upgradeBtn.setOnClickListener(onUpgradeAccountListener());
    }

    private void getEmphericalKey()
    {
        // second request
        request = new StringRequest(StringRequest.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                response ->
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);

                        customerID = jsonObject.getString("id");

                        Toast.makeText(getContext(), customerID, Toast.LENGTH_SHORT).show();

                        getClientSecret(customerID, emphericalKey);
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                },
                error ->
                {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                header.put("Stripe-Version", "2024-04-10");
                return header;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this.requireContext());
        requestQueue.add(request);
    }

    private void getClientSecret(String customerID, String emphericalKey)
    {
        request = new StringRequest(StringRequest.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response ->
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);

                        clientSecret = jsonObject.getString("client_secret");

                        Toast.makeText(getContext(), clientSecret, Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                },
                error ->
                {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);
                return header;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", "100"+"00");
                params.put("currency", "SGD");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this.requireContext());
        requestQueue.add(request);
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueAccountTypeListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    userAccount = snapshot.getValue(Account.class);
                }
                else
                {
                    Log.d(TAG, "onDataChange: no data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onUpgradeAccountListener()
    {
        return v ->
        {
            // todo: we need to add payment page.
            if (userAccount != null)
            {
                userAccount.setAccountType(PREMIUM_USER);
                databaseReferenceUserAccounts.setValue(userAccount).addOnCompleteListener(onUpgradeCompleteListener());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onUpgradeCompleteListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
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
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);

                            if (getActivity() != null)
                            {
                                getActivity().finish();
                            }
                        });
                alertDialog.show();
            }
            else
            {
                Toast.makeText(getContext(), "Upgrade failed", Toast.LENGTH_SHORT).show();
            }
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