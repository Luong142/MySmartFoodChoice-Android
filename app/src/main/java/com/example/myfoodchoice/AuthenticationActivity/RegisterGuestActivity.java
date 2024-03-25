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
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.SignUpCreateProfileActivities.UserProfileCreateFirstActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class RegisterGuestActivity extends AppCompatActivity
{
    // TODO: declare UI components

    // buttons
    Button nextBtn;

    // log in clickable text
    TextView navLoginClick;

    // Edit text
    EditText signupEmailEditText, signupPasswordEditText, firstNameEditText, lastNameEditText;

    // progress bar
    ProgressBar progressBar;

    SpannableString spannableStringLoginNav;

    // for authentication
    FirebaseAuth firebaseAuth;

    // for database
    FirebaseDatabase firebaseDatabase;

    static final String TAG = "RegisterGuestActivity";

    static final String LABEL_USER = "Registered Accounts";

    DatabaseReference databaseReferenceRegisteredGuest;
    String email, password, firstName, lastName;

    Account account;

    UserProfile userProfile;

    FirebaseUser firebaseUser;

    Intent intentNavToUserProfileFirstActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_guest);

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
        firstNameEditText = findViewById(R.id.firstNameProfile);
        lastNameEditText = findViewById(R.id.lastNameProfile);

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // button
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(onSignUpListener());

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
                signupEmailEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                signupPasswordEditText.setError("Password is required.");
                signupPasswordEditText.requestFocus();
                return;
            }

            if (password.length() < 4)
            {
                signupPasswordEditText.setError("Password must be at least 6 characters.");
                signupPasswordEditText.requestFocus();
                return;
            }

            if (containsNumber(firstNameEditText))
            {
                firstNameEditText.setError("First name cannot contain numbers.");
                firstNameEditText.requestFocus();
                return;
            }

            if (containsNumber(lastNameEditText))
            {
                lastNameEditText.setError("Last name cannot contain numbers.");
                lastNameEditText.requestFocus();
                return;
            }

            // make the progressBar appear.
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);

            // register user to the firebase.
            // FIXME: the problem is the data is not saved in this realtime database.
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegisterGuestActivity.this, "Guest registered successfully.",
                            Toast.LENGTH_SHORT).show();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    // Log.d(TAG, "createUserWithEmail:success " + Objects.requireNonNull(firebaseUser).getUid());

                    // retrieve the first name and last name from the user.
                    firstName = firstNameEditText.getText().toString().trim();
                    lastName = lastNameEditText.getText().toString().trim();

                    // init database reference

                    // for user account
                    databaseReferenceRegisteredGuest =
                            firebaseDatabase.getReference(LABEL_USER).child(firebaseUser.getUid());


                    // init user account
                    account = new Account(email, password);
                    account.setAccountType("Guest");

                    Log.d(TAG, "onCreate: " + account.toString());

                    // start the trial day?
                    account.startGuestTrialPeriod();

                    // intent to carry this too
                    userProfile = new UserProfile();
                    userProfile.setFirstName(firstName);
                    userProfile.setLastName(lastName);

                    // set display name
                    firebaseUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + firstName).build())
                            .addOnFailureListener(onFailureUpdateDisplayName());

                    // through we move to next if complete
                    databaseReferenceRegisteredGuest.setValue(account).addOnCompleteListener
                            (onCompleteUserAccountListener());
                    // auto create a new path with name as string value and assign to a variable.
                    // databaseReference = firebaseDatabase.getReference(LABEL);

                    // TODO: set the value based on the model class ReadWriteUserDetail
                    // create a new child of this user and set that value.
                    // databaseReference.child
                            // (firebaseUser.getUid()).setValue(userProfile).addOnCompleteListener(onCompleteListener());
                    // FIXME: not yet to do this, need to set up the user profile before uploading to firebase.
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
                        signupEmailEditText.setError("Invalid credentials.");
                        signupEmailEditText.requestFocus();
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
                        Toast.makeText(RegisterGuestActivity.this, "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    // to make the progress bar gone.
                    progressBar.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.VISIBLE);
                }
            });
        };
    }

    public boolean containsNumber(@NonNull EditText editText)
    {
        String text = editText.getText().toString();
        return text.matches(".*\\d.*");
    }

    @NonNull
    @Contract(pure = true)
    private OnFailureListener onFailureUpdateDisplayName()
    {
        return task ->
        {
            if (task.getMessage() != null)
            {
                Log.d(TAG, "onFailureUpdateDisplayName: " + task.getMessage());
            }
        };
    }
    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteUserAccountListener()
    {
        return v ->
        {
            // Log.d(TAG, "onCompleteUserAccountListener: " + userProfile); // debug message
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Security Measure Applied");
            alertDialog.setMessage("Your data is now secure. Only authorized users can access their personal information. " +
                    "If you encounter any issues or have questions, please contact support.");
            alertDialog.setPositiveButton("OK", (dialog, which) -> {
                // dismiss the dialog
                dialog.dismiss();
                // move to user profile for default user profile page.
                intentNavToUserProfileFirstActivity = new Intent(RegisterGuestActivity.this, UserProfileCreateFirstActivity.class);
                intentNavToUserProfileFirstActivity.putExtra("userProfile", userProfile);
                startActivity(intentNavToUserProfileFirstActivity);
                finish();
            });
            alertDialog.show();
        };
    }

    public ClickableSpan clickableLoginNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(@NonNull View widget)
                {
                    Intent intent = new Intent(RegisterGuestActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }
}