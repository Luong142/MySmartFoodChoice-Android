package com.example.myfoodchoice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity
{
    // todo: for this we can use Stripe API, https://docs.stripe.com/api
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }
}