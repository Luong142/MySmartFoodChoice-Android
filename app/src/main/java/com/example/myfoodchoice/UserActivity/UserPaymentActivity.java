package com.example.myfoodchoice.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserPaymentActivity extends AppCompatActivity
{
    private static final String PREMIUM_USER = "Premium User";
    String secretKey = "sk_test_51PE9Gp2Lluf0ZsJHi8QSjt4aqh1ZDC31O1yDcMATa5lzTH95zmNP6fY6CHyim6DcxXze7ntfJtV5WFcUhpxOUUfE00SkNAEOHu";
    String publishableKey = "pk_test_51PE9Gp2Lluf0ZsJHLz9trebrLFYm75gSsTJXCYq4X0kG5BtrT6p21ydg6lR2SmRbo7gNIqkwYWQM15ysxpkgbkxN009EdwKmNc";

    String customerID;

    String emphericalKey;

    String clientSecret;

    StringRequest request;

    RequestQueue requestQueue;

    PaymentSheet paymentSheet;

    static final String TAG = "UserPaymentActivity";

    static final String PATH_DATABASE = "Registered Accounts";

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReferenceUserAccounts;

    FirebaseUser firebaseUser;

    FirebaseAuth firebaseAuth;

    String userId;

    Account userAccount;

    // todo: for UI
    FloatingActionButton backBtn;

    Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);

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

        // todo: STRIPE API, the only way is to create an activity instead of fragment.
        PaymentConfiguration.init(this, this.publishableKey);
        paymentSheet = new PaymentSheet(this, onPaymentCallback());

        // first request
        request = new StringRequest(StringRequest.Method.POST, "https://api.stripe.com/v1/customers",
                response ->
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);

                        customerID = jsonObject.getString("id");

                        Log.d(TAG, "customer id: " + customerID);

                        getEmphericalKey();
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                },
                error ->
                {
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secretKey);

                return header;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        // todo: for UI
        backBtn = findViewById(R.id.backBtn);
        paymentBtn = findViewById(R.id.paymentBtn);

        backBtn.setOnClickListener(onBackListener());
        paymentBtn.setOnClickListener(onPaymentBtnListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onPaymentBtnListener()
    {
        return  v ->
        {
            if (emphericalKey == null)
            {
                Toast.makeText(this, "Please wait, loading...", Toast.LENGTH_SHORT).show();
                return;
            }

            if (secretKey == null)
            {
                Toast.makeText(this, "Please wait, loading...", Toast.LENGTH_SHORT).show();
                return;
            }

            paymentFlow();
        };
    }

    private void paymentFlow()
    {
        paymentSheet.presentWithPaymentIntent(clientSecret, new
                PaymentSheet.Configuration("MySmartFoodChoice LTD PTE"));
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
    private OnCompleteListener<Void> onUpgradeCompleteListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {

            }
            else
            {
                Toast.makeText(this, "Upgrade failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private PaymentSheetResultCallback onPaymentCallback()
    {
        return this::onPaymentResult;
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult)
    {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed)
        {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
            // todo: this is where we upgrade user account.
            if (userAccount != null)
            {
                userAccount.setAccountType(PREMIUM_USER);
                databaseReferenceUserAccounts.setValue(userAccount)
                        .addOnCompleteListener(onUpgradeCompleteListener());
            }
        }
        else
        {
            Log.d(TAG, "onPaymentResult: " + paymentSheetResult.toString());
        }
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

                        emphericalKey = jsonObject.getString("id");

                        Log.d(TAG, "empherical key: " + emphericalKey);

                        getClientSecret(customerID, emphericalKey);
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                },
                error ->
                {
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

        requestQueue = Volley.newRequestQueue(this);
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

                        Log.d(TAG, "onResponse client secret: " + clientSecret);
                    }
                    catch (JSONException e)
                    {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                },
                error ->
                {
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onBackListener()
    {
        return v ->
        {
            if (userAccount == null)
            {
                Log.d(TAG, "onBackListener: userAccount is null");
                return;
            }

            if (userAccount.getAccountType().equals("Premium User"))
            {
                Intent intent = new Intent(this, UserPremiumMainMenuActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(this, UserMainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }
}