package com.example.myfoodchoice.GuestActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myfoodchoice.GuestFragment.GuestUpgradeAccountFragment;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.SharedReviewFragment.ReviewFragment;
import com.example.myfoodchoice.UserFragment.UserHealthTipsFragment;
import com.example.myfoodchoice.UserFragment.UserHomeAlvinFragment;
import com.example.myfoodchoice.UserFragment.UserProfileViewFragment;
import com.example.myfoodchoice.UserFragment.UserRecipeFragment;
import com.example.myfoodchoice.UserFragment.UserWorkOutFragment;
import com.example.myfoodchoice.WelcomeActivity.WelcomeActivity;
import com.google.android.material.navigation.NavigationView;

public class GuestMainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    // TODO: declare
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main_menu);

        // TODO: init
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);


        // TODO: for navigation drawer
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Smart Food Choice");
        navigationView.setNavigationItemSelectedListener(this);
        // TODO: the animation is here in navigation drawer should record this in report file.
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserHomeAlvinFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_view);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */)
        {
            @Override
            public void handleOnBackPressed()
            {
                // handle the back button press
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();

        // TODO: implement more tab here
        // TODO: also need to update nav_header with the image and the email

        if (itemId == R.id.nav_home)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserHomeAlvinFragment()).commit();
            // Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        }

        else if (itemId == R.id.nav_upgrade_account)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GuestUpgradeAccountFragment()).commit();
        }

        // TODO: pls take note that Guest can access some User fragment

        else if (itemId == R.id.nav_food_recipe)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserRecipeFragment()).commit();
        }

        else if (itemId == R.id.nav_health_tips)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserHealthTipsFragment()).commit();
        }

        else if (itemId == R.id.nav_work_out)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserWorkOutFragment()).commit();
        }

        else if (itemId == R.id.nav_review)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ReviewFragment()).commit();
        }

        else if (itemId == R.id.nav_manage_userProfile)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserProfileViewFragment()).commit();
        }

        else if (itemId == R.id.nav_log_off)
        {
            // TODO: destroy this temporary db.
            // Paper.book().destroy();
            // log out
            // FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        super.onPointerCaptureChanged(hasCapture);
    }
}