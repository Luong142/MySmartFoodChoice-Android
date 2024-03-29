package com.example.myfoodchoice.AuthenticationActivity;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.SignUpCreateProfileActivities.BusinessProfileCreateActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class RegisterBusinessActivity extends AppCompatActivity
{
    private static final String TAG = "Register Business Trainer";
    private static final String LABEL_USER = "Registered Users";
    // TODO: declare UI components
    EditText emailEditText, passwordEditText;

    String email, password;

    TextView navLoginClick;

    SpannableString spannableStringLoginNav;

    Button nextBtn;

    ProgressBar progressBar;

    Account account;

    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;

    Intent intentNavToBusinessProfileActivity;

    DatabaseReference databaseReferenceRegisteredUser;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);

        // TODO: init firebase user
        // init firebase database, paste the correct link as reference.
        firebaseDatabase = FirebaseDatabase.getInstance
                // TODO: we need to link the url so that the database can retrieve the data from
                        ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init UI components
        emailEditText = findViewById(R.id.sign_up_email);
        passwordEditText = findViewById(R.id.sign_up_password);
        nextBtn = findViewById(R.id.nextBtn);
        navLoginClick = findViewById(R.id.clickableLoginNavText);

        // set on click for text
        spannableStringLoginNav = new SpannableString(navLoginClick.getText());
        spannableStringLoginNav.setSpan(clickableLoginNavSpan(), 18, navLoginClick.length(), 0);
        navLoginClick.setText(spannableStringLoginNav);
        navLoginClick.setMovementMethod(LinkMovementMethod.getInstance());

        // set progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // set on click
        nextBtn.setOnClickListener(onSignUpListener());

    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            Log.d("RegisterActivity", "signup button activated! ");
            email = emailEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();

            Log.d(TAG, "onSignUpListener: " + email + " " + password);

            // validation
            if (TextUtils.isEmpty(email))
            {
                emailEditText.setError("Email is required.");
                emailEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                passwordEditText.setError("Password is required.");
                passwordEditText.requestFocus();
                return;
            }

            if (password.length() < 4)
            {
                passwordEditText.setError("Password must be at least 6 characters.");
                passwordEditText.requestFocus();
                return;
            }

            // make the progressBar appear.
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);

            // register user to the firebase.
            // FIXME: the problem is the data is not saved in this realtime database.
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onNextListener());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<AuthResult> onNextListener()
    {
        return task ->
        {
            if (task.isSuccessful()) {
                Toast.makeText(RegisterBusinessActivity.this, "Vendor registered successfully.",
                        Toast.LENGTH_SHORT).show();
                firebaseUser = firebaseAuth.getCurrentUser();
                // Log.d(TAG, "createUserWithEmail:success " + Objects.requireNonNull(firebaseUser).getUid());

                // init database reference

                // for user account
                // databaseReferenceRegisteredUser =
                        // firebaseDatabase.getReference(LABEL_USER).child(firebaseUser.getUid());

                // init user account
                account = new Account(email, password); // with email and password

                // through we move to next if complete
                    /*
                        databaseReferenceRegisteredUser.setValue(userAccount).addOnCompleteListener
                        (onCompleteUserAccountListener());
                     */
                // auto create a new path with name as string value and assign to a variable.
                // databaseReference = firebaseDatabase.getReference(LABEL);

                // TODO: set the value based on the model class ReadWriteUserDetail
                // create a new child of this user and set that value.
                // databaseReference.child
                // (firebaseUser.getUid()).setValue(userProfile).addOnCompleteListener(onCompleteListener());
                // FIXME: not yet to do this, need to set up the user profile before uploading to firebase.

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Security Measure Applied");
                alertDialog.setMessage("Your data is now secure. " +
                        "Only authorized vendors can access their personal information. " +
                        "If you encounter any issues or have questions, please contact support.");
                alertDialog.setPositiveButton("OK", (dialog, which) -> {
                    // dismiss the dialog
                    dialog.dismiss();
                    // move to business profile for default business profile page.
                    // FIXME: need to set up the business profile before uploading to firebase.
                    intentNavToBusinessProfileActivity = new Intent(RegisterBusinessActivity.this,
                            BusinessProfileCreateActivity.class);
                    intentNavToBusinessProfileActivity.putExtra("userAccount", account);
                    startActivity(intentNavToBusinessProfileActivity);
                    finish();
                });
                alertDialog.show();
            }
            else
            {
                try {

                    throw Objects.requireNonNull(task.getException());
                }
                catch (FirebaseAuthWeakPasswordException e)
                {
                    passwordEditText.setError("Your password is too weak. Kindly use a mix of alphabets, numbers," +
                            " and special characters.");
                    // this function is to focus on this edit text UI part so that user can focus on this error.
                    passwordEditText.requestFocus();
                }
                catch (FirebaseAuthInvalidCredentialsException e)
                {
                    emailEditText.setError("Invalid credentials.");
                    emailEditText.requestFocus();
                }
                catch (FirebaseAuthUserCollisionException e)
                {
                    // this one is for email I suppose.
                    emailEditText.setError("User is already registered with this email. Use another email");
                    emailEditText.requestFocus();
                }
                catch (Exception e)
                {
                    // FIXME: debug purpose
                    Log.d(TAG, "Error: " + e.getMessage());
                    Toast.makeText(RegisterBusinessActivity.this, "Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                // to make the progress bar gone.
                progressBar.setVisibility(View.GONE);
                nextBtn.setVisibility(View.VISIBLE);
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ClickableSpan clickableLoginNavSpan()
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(@NonNull View widget)
            {
                Intent intent = new Intent(RegisterBusinessActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }
}