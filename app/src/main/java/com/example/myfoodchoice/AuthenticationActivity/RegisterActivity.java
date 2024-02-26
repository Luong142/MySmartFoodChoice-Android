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

import com.example.myfoodchoice.Model.User;
import com.example.myfoodchoice.R;
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
    private Button signupBtn;

    // log in clickable text
    private TextView navLoginClick;

    // Edit text
    private EditText signupEmailEditText, signupFirstNameEditText, signupLastNameEditText, signupPasswordEditText;

    // progress bar
    private ProgressBar progressBar;

    private SpannableString spannableStringLoginNav;

    // for authentication
    private FirebaseAuth firebaseAuth;

    // for database
    private FirebaseDatabase firebaseDatabase;

    private static final String TAG = "RegisterActivity";

    private static final String LABEL = "Register Users";

    private User user;

    private DatabaseReference databaseReference;

    private String email, password, firstName, lastName;

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
        signupFirstNameEditText = findViewById(R.id.sign_up_firstname);
        signupLastNameEditText = findViewById(R.id.sign_up_lastname);
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

    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            Log.d("RegisterActivity", "signup button activated! ");
            email = signupEmailEditText.getText().toString().trim();
            password = signupPasswordEditText.getText().toString().trim();
            firstName = signupFirstNameEditText.getText().toString().trim();
            lastName = signupLastNameEditText.getText().toString().trim();

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
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "createUserWithEmail:success " + Objects.requireNonNull(firebaseUser).getUid());

                    // create an object based on this model
                    user = new User(firstName, lastName);

                    // auto create a new path with name as string value and assign to a variable.
                    databaseReference = firebaseDatabase.getReference(LABEL);

                    // TODO: set the value based on the model class ReadWriteUserDetail
                    // create a new child of this user and set that value.
                    databaseReference.child
                            (firebaseUser.getUid()).setValue(user);

                    // move to login page.
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

    private ClickableSpan clickableLoginNavSpan()
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
}