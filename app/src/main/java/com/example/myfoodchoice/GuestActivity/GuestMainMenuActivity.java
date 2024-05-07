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

import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserFragment.UserHomeAlvinFragment;
import com.example.myfoodchoice.UserFragment.UserLogMealFragment;
import com.example.myfoodchoice.UserFragment.UserViewMealHistoryFragment;
import com.example.myfoodchoice.WelcomeActivity.WelcomeActivity;
import com.google.android.material.navigation.NavigationView;

public class GuestMainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    // todo: there is an issue with the guest where
    //  Calories Ninja API doesn't work onCancelled:
    //  This client does not have permission to perform this operation

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

    /*
    <item
            android:id="@+id/nav_bmi_calculator"
            android:title="@string/bmi_calculator"
            android:icon="@drawable/calculator"/>
     */

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

        // todo: to update the part separate the button into two items in menu here
        else if (itemId == R.id.nav_log_my_meal)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserLogMealFragment()).commit();
        }

        else if (itemId == R.id.nav_meal_history)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserViewMealHistoryFragment()).commit();
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