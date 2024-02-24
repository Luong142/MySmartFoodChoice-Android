package com.example.myfoodchoice.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myfoodchoice.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PartnershipActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership);

        // init bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_partnership);

        bottomNavigationView.setOnItemSelectedListener(item ->
        {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_welcome)
            {
                // Handle navigation to Home
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.nav_about_us)
            {
                Intent intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.nav_partnership)
            {
                Intent intent2 = new Intent(this, PartnershipActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.nav_review)
            {
                Intent intent3 = new Intent(this, ReviewActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

                return true;
            }
            else if (itemId == R.id.nav_contact_us)
            {
                Intent intent5 = new Intent(this, ContactUsActivity.class);
                startActivity(intent5);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }
}