package com.example.myfoodchoice.AuthenticationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.BusinessDietitianActivity.DietitianMainMenuActivity;
import com.example.myfoodchoice.BusinessTrainerActivity.TrainerMainMenuActivity;
import com.example.myfoodchoice.GuestActivity.GuestMainMenuActivity;
import com.example.myfoodchoice.Model.UserAccount;
import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.Prevalent.Prevalent;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserMainMenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{
    // TODO: declare UI component

    // button
    Button loginBtn;

    // Edit text
    EditText loginEmailEditText, loginPasswordEditText;

    // check box
    CheckBox rememberMe;

    // clickable text
    TextView clickableForgotPassword, clickableSignUpNav, clickableLoginAsGuest;

    // for clickable text
    SpannableString spannableStringSignUpNav, spannableStringLoginAsGuestNav, spannableStringForgotPassword;

    // for progress bar
    ProgressBar progressBar;

    // firebase login
    FirebaseAuth mAuth;

    FirebaseUser firebaseUser;

    static final int INDEXSTART = 0;

    FirebaseDatabase firebaseDatabase;

    String email, password, userID, accountType;

    Intent intent;

    UserAccount userAccount;

    DatabaseReference databaseReferenceAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Smart Food Choice");

        // TODO: init paper
        Paper.init(LoginActivity.this);

        // TODO: init Firebase auth
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        // TODO: init Firebase database, paste the correct link as reference.
        firebaseDatabase = FirebaseDatabase.getInstance(
                "https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        // for testing
        // firebaseDatabase.getReference().child("Test").child("new child").setValue("new value");

        // by default is User.
        accountType = "User";

        // this one is to monitor the auth state changes.
        mAuth.addAuthStateListener(authStateListener);

        // TODO: init UI components
        loginEmailEditText = findViewById(R.id.login_email);
        loginPasswordEditText = findViewById(R.id.login_password);

        // init user profile
        // TODO: do we need userProfile here to pass to main-menu?

        // check box
        rememberMe = findViewById(R.id.rememberMe_checkBox);

        // clickable text
        clickableForgotPassword = findViewById(R.id.clickableForgotPassword);
        clickableSignUpNav = findViewById(R.id.clickableSignUpNavText);
        clickableLoginAsGuest = findViewById(R.id.clickableLoginGuestNavText);

        // button
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setVisibility(View.VISIBLE);
        loginBtn.setOnClickListener(onLoginListener());

        // nav to sign up page based on text click
        spannableStringSignUpNav = new SpannableString(clickableSignUpNav.getText());
        spannableStringSignUpNav.setSpan(clickableSignUpNavSpan(), 17, clickableSignUpNav.length(), 0);
        clickableSignUpNav.setText(spannableStringSignUpNav);
        clickableSignUpNav.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to guest main menu page based on text click
        spannableStringLoginAsGuestNav = new SpannableString(clickableLoginAsGuest.getText());
        spannableStringLoginAsGuestNav.setSpan(clickableLoginAsGuestNavSpan(), INDEXSTART, clickableLoginAsGuest.length(), 0);
        clickableLoginAsGuest.setText(spannableStringLoginAsGuestNav);
        clickableLoginAsGuest.setMovementMethod(LinkMovementMethod.getInstance());

        // nav to forgot password page based on text click
        spannableStringForgotPassword = new SpannableString(clickableForgotPassword.getText());
        spannableStringForgotPassword.setSpan(clickableForgotPasswordNavSpan(), INDEXSTART, clickableForgotPassword.length(), 0);
        clickableForgotPassword.setText(spannableStringForgotPassword);
        clickableForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // check box listener
        rememberMe.setOnCheckedChangeListener(onCheckedListener());

    }

    private final FirebaseAuth.AuthStateListener authStateListener =
            firebaseAuth ->
    {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            // user signed in
            Log.d("LoginActivity", "UID here: " + firebaseUser.getUid());
            userID = firebaseUser.getUid();
            databaseReferenceAccountType = firebaseDatabase.getReference("Registered Businesses").child(userID);
            databaseReferenceAccountType.addListenerForSingleValueEvent(valueAccountTypeEventListener());
        }

    };

    @NonNull
    @Contract(" -> new") // the purpose to is to recognise the account type
    // and auto choose the correct layout for normal user, dietitian, trainer.
    private ValueEventListener valueAccountTypeEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userAccount = snapshot.getValue(UserAccount.class);
                accountType = "User"; // by default is user, unless changed.
                if (userAccount != null)
                {
                    accountType = userAccount.getAccountType();
                }
                Log.d("LoginActivity", "onDataChange: " + accountType);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText
                        (LoginActivity.this, "Error database connection", Toast.LENGTH_SHORT).show();
                // Log.w("LoginActivity", "loadUserProfile:onCancelled ", error.toException());
            }
        };
    }

    // TODO: the purpose is to save data via Paper.
    public CompoundButton.OnCheckedChangeListener onCheckedListener()
    {
        return (buttonView, isChecked) ->
        {
            email = loginEmailEditText.getText().toString().trim();
            password = loginPasswordEditText.getText().toString().trim();
            if (isChecked)
            {
                Log.d("LoginActivity", "remember me checked! ");
                Paper.book().write(Prevalent.UserEmailKey, email);
                Paper.book().write(Prevalent.UserPasswordKey, password);
            }
        };
    }
    // TODO: to implement the login functionalities for this activity.

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onLoginListener()
    {
        return v ->
        {
            Log.d("LoginActivity", "login button activated! ");
            email = loginEmailEditText.getText().toString().trim();
            password = loginPasswordEditText.getText().toString().trim();
            allowLogin(email, password);
        };
    }

    public void allowLogin(String email, String password)
    {
        // TODO: login function

        // validation
        if (TextUtils.isEmpty(email))
        {
            loginEmailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            loginPasswordEditText.setError("Password is required.");
            return;
        }

        // loading
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);

        // authentication login
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            // check the condition based on the account type.
            // if the account type is not recognized, then show a toast message.
            // if the account type is recognized, then navigate to the correct main menu page.
            if (task.isSuccessful())
            {
                switch (accountType)
                {
                    case "User":
                        intent = new Intent(LoginActivity.this, UserMainMenuActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "Trainer":
                        intent = new Intent(LoginActivity.this, TrainerMainMenuActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "Dietitian":
                        intent = new Intent(LoginActivity.this, DietitianMainMenuActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    default:
                        Toast.makeText(LoginActivity.this,

                                "Account type is not recognized, please try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this,
                        "Email or Password incorrect, please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener); // avoid memory leaks
        }
    }

    public ClickableSpan clickableForgotPasswordNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to forgot password page! ");
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                }
            };
        }
    }

    public ClickableSpan clickableSignUpNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to sign up page! ");
                    Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }

    public ClickableSpan clickableLoginAsGuestNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Log.d("LoginActivity", "navigating to guest main menu page! ");
                    Intent intent = new Intent(LoginActivity.this, GuestMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }
}