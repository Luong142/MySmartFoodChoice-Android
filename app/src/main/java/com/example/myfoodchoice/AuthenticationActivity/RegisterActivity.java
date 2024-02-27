package com.example.myfoodchoice.AuthenticationActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myfoodchoice.Model.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.UserActivity.UserProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity
{
    // TODO: declare UI components

    // buttons
    Button signupBtn;

    // log in clickable text
    TextView navLoginClick;

    // Edit text
    EditText signupEmailEditText, signupPasswordEditText;

    // progress bar
    ProgressBar progressBar;

    SpannableString spannableStringLoginNav;

    // for authentication
    FirebaseAuth firebaseAuth;

    // for database
    FirebaseDatabase firebaseDatabase;

    static final String TAG = "RegisterActivity";

    static final String LABEL = "Registered Users";


    DatabaseReference databaseReference;

    String email, password, firstName, lastName;

    UserProfile userProfile;

    FirebaseUser firebaseUser;

    Intent intentNavToUserProfileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // init firebase database, paste the correct link as reference.
        firebaseDatabase = FirebaseDatabase.getInstance
                // TODO: we need to link the url so that the database can retrieve the data from
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init UI components
        // edit text
        signupEmailEditText = findViewById(R.id.sign_up_email);
        signupPasswordEditText = findViewById(R.id.sign_up_password);

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // button
        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setVisibility(View.VISIBLE);
        signupBtn.setOnClickListener(onSignUpListener());

        // clickable text
        navLoginClick = findViewById(R.id.clickableLoginNavText);
        spannableStringLoginNav = new SpannableString(navLoginClick.getText());
        spannableStringLoginNav.setSpan(clickableLoginNavSpan(), 18, navLoginClick.length(), 0);
        navLoginClick.setText(spannableStringLoginNav);
        navLoginClick.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            Log.d("RegisterActivity", "signup button activated! ");
            email = signupEmailEditText.getText().toString().trim();
            password = signupPasswordEditText.getText().toString().trim();

            Log.d(TAG, "onSignUpListener: " + firstName + " " + lastName);
            Log.d(TAG, "onSignUpListener: " + email + " " + password);

            // validation
            if (TextUtils.isEmpty(email))
            {
                signupEmailEditText.setError("Email is required.");
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                signupPasswordEditText.setError("Password is required.");
                return;
            }

            if (password.length() < 4)
            {
                signupPasswordEditText.setError("Password must be at least 6 characters.");
                return;
            }

            // make the progressBar appear.
            progressBar.setVisibility(View.VISIBLE);

            // register user to the firebase.
            // FIXME: the problem is the data is not saved in this realtime database.
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "User registered successfully.",
                            Toast.LENGTH_SHORT).show();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "createUserWithEmail:success " + Objects.requireNonNull(firebaseUser).getUid());

                    // init user profile
                    userProfile = new UserProfile(email, password);

                    intentNavToUserProfileActivity = new Intent(RegisterActivity.this, UserProfileActivity.class);

                    // auto create a new path with name as string value and assign to a variable.
                    // databaseReference = firebaseDatabase.getReference(LABEL);

                    // TODO: set the value based on the model class ReadWriteUserDetail
                    // create a new child of this user and set that value.
                    // databaseReference.child
                            // (firebaseUser.getUid()).setValue(userProfile).addOnCompleteListener(onCompleteListener());
                    // FIXME: not yet to do this, need to set up the user profile before uploading to firebase.

                    Log.d(TAG, "onCompleteListener: " + task.isSuccessful());
                    // move to user profile for default user profile page.
                    intentNavToUserProfileActivity.putExtra("userProfile", userProfile);
                    intentNavToUserProfileActivity.setFlags
                            (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentNavToUserProfileActivity);
                    finish(); // to close the register page.
                }
                else
                {
                    try
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch (FirebaseAuthWeakPasswordException e)
                    {
                        signupPasswordEditText.setError("Your password is too weak. Kindly use a mix of alphabets, numbers," +
                                " and special characters.");
                        // this function is to focus on this edit text UI part so that user can focus on this error.
                        signupPasswordEditText.requestFocus();
                    }
                    catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        signupPasswordEditText.setError("Invalid credentials.");
                        signupPasswordEditText.requestFocus();
                    }
                    catch(FirebaseAuthUserCollisionException e)
                    {
                        // this one is for email I suppose.
                        signupEmailEditText.setError("User is already registered with this email. Use another email");
                        signupEmailEditText.requestFocus();

                    }
                    catch (Exception e)
                    {
                        // FIXME: debug purpose
                        Log.d(TAG, "Error: " + e.getMessage());
                        Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    // to make the progress bar gone.
                    progressBar.setVisibility(View.GONE);
                }
            });
        };
    }

    public ClickableSpan clickableLoginNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }

    public OnCompleteListener<Void> onCompleteListener()
    {
        return task ->
        {

        };
    }
}