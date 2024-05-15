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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity
{
    // TODO: declare UI components

    Button nextBtn;

    FloatingActionButton backBtn;

    // log in clickable text
    TextView navLoginClick, clickableTermNavText;

    CheckBox agreeTermCheckBox;

    // Edit text
    EditText signupEmailEditText, signupPasswordEditText, firstName, lastName;

    // progress bar
    ProgressBar progressBar;

    SpannableString spannableStringLoginNav, spannableStringTermNav;

    // for authentication
    FirebaseAuth firebaseAuth;

    // for database
    FirebaseDatabase firebaseDatabase;

    static final String TAG = "RegisterUserActivity";

    static final String PATH_ACCOUNTS = "Registered Accounts";

    static final String PATH_USERPROFILE = "Android User Profile";

    DatabaseReference databaseReferenceRegisteredUser, databaseReferenceUserProfile;
    String email, password;

    Account account;

    UserProfile userProfile;

    FirebaseUser firebaseUser;

    Intent intentNavToUserProfileFirstActivity;

    private String firstNameString;
    private String lastNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

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
        firstName = findViewById(R.id.firstNameProfile);
        lastName = findViewById(R.id.lastNameProfile);

        // progress bar
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // button
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setOnClickListener(onSignUpListener());
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(onBackBtnListener());

        // clickable text
        navLoginClick = findViewById(R.id.clickableLoginNavText);
        spannableStringLoginNav = new SpannableString(navLoginClick.getText());
        spannableStringLoginNav.setSpan(clickableLoginNavSpan(), 0, navLoginClick.length(), 0);
        navLoginClick.setText(spannableStringLoginNav);
        navLoginClick.setMovementMethod(LinkMovementMethod.getInstance());

        clickableTermNavText = findViewById(R.id.clickableTermNavText);
        spannableStringTermNav = new SpannableString(clickableTermNavText.getText());
        spannableStringTermNav.setSpan(clickableTermNavSpan(), 0, clickableTermNavText.length(), 0);
        clickableTermNavText.setText(spannableStringTermNav);
        clickableTermNavText.setMovementMethod(LinkMovementMethod.getInstance());

        // check box here
        agreeTermCheckBox = findViewById(R.id.agreeTermCheckBox);
    }

    public View.OnClickListener onSignUpListener()
    {
        return v ->
        {
            //Log.d("RegisterActivity", "signup button activated! ");
            email = signupEmailEditText.getText().toString().trim();
            password = signupPasswordEditText.getText().toString().trim();

            firstNameString = firstName.getText().toString().trim();
            lastNameString = lastName.getText().toString().trim();

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

            if (TextUtils.isEmpty(firstName.getText().toString().trim()))
            {
                firstName.setError("Please enter your first name.");
                firstName.requestFocus();
                return; // exit the method if age is not entered
            }

            if (TextUtils.isEmpty(lastName.getText().toString().trim()))
            {
                lastName.setError("Please enter your last name.");
                lastName.requestFocus();
                return; // exit the method if age is not entered
            }

            if (!agreeTermCheckBox.isChecked())
            {
                Toast.makeText(this, "Agreement required to continue.",
                        Toast.LENGTH_LONG).show();
                agreeTermCheckBox.requestFocus();
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
                    firebaseUser = firebaseAuth.getCurrentUser();
                    // Log.d(TAG, "createUserWithEmail:success " + Objects.requireNonNull(firebaseUser).getUid());

                    // init database reference

                    // for user account
                    databaseReferenceRegisteredUser =
                            firebaseDatabase.getReference(PATH_ACCOUNTS).child(firebaseUser.getUid());

                    databaseReferenceUserProfile =
                            firebaseDatabase.getReference(PATH_USERPROFILE).child(firebaseUser.getUid());


                    // init user account
                    account = new Account(email, password);
                    // set to user, because supervisor said that user
                    account.setAccountType("User");

                    // start the trial day?
                    account.startGuestTrialPeriod();

                    // intent to carry this too
                    userProfile = new UserProfile();
                    userProfile.setKey(firebaseUser.getUid());
                    userProfile.setFirstName(firstNameString);
                    userProfile.setLastName(lastNameString);

                    // set display name
                    firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstNameString + " " + lastNameString).build())
                            .addOnFailureListener(onFailureUpdateDisplayName());


                    // through we move to next if complete
                    databaseReferenceRegisteredUser.setValue(account).addOnCompleteListener
                            (onCompleteUserAccountListener()).addOnCompleteListener(onCompletedFullNameListener());
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
                    }
                    // to make the progress bar gone.
                    progressBar.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.VISIBLE);
                }
            });
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompletedFullNameListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "OSOSOSO: " + firstNameString + " " + lastNameString);
                Log.d(TAG, "onCompletedFullNameListener: " + task.getResult());
            }
            else
            {
                Log.d(TAG, "OSOSOSO: " + firstNameString + " " + lastNameString);
                Log.d(TAG, "onCompletedFullNameListener: " + task.getException());
            }
        };
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
        return task ->
        {
            if (task.isSuccessful())
            {
                databaseReferenceUserProfile.setValue(userProfile).addOnCompleteListener(onCompleteDefaultUserProfileListener());
            }
            else
            {
                Log.d(TAG, "onCompleteUserAccountListener: " + "failed");
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private OnCompleteListener<Void> onCompleteDefaultUserProfileListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                // todo: need to upgrade this into term and condition
                // Log.d(TAG, "onCompleteUserAccountListener: " + userProfile); // debug message
                AlertDialog.Builder alertDialog = getBuilder();
                alertDialog.setPositiveButton("OK", (dialog, which) ->
                {
                    // dismiss the dialog
                    dialog.dismiss();
                    // move to user profile for default user profile page.
                    intentNavToUserProfileFirstActivity = new Intent(RegisterUserActivity.this, UserProfileCreateFirstActivity.class);
                    intentNavToUserProfileFirstActivity.putExtra("userProfile", userProfile);
                    startActivity(intentNavToUserProfileFirstActivity);
                    finish();
                });
                alertDialog.show();
            }
            else
            {
                Log.d(TAG, "onCompleteDefaultUserProfileListener: " + "failed");
            }
        };
    }

    @NonNull
    private AlertDialog.Builder getBuilder()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Security Measure Applied");
        alertDialog.setMessage("Your data is now secure. Only authorized users can access their personal information. " +
                "If you encounter any issues or have questions, please contact support.");
        return alertDialog;
    }

    public ClickableSpan clickableLoginNavSpan()
    {
        {
            return new ClickableSpan()
            {
                @Override
                public void onClick(@NonNull View widget)
                {
                    Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }

    @NonNull
    @Contract(" -> new")
    private ClickableSpan clickableTermNavSpan()
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(@NonNull View widget)
            {
                // open the terms of service activity
                Intent intent = new Intent(RegisterUserActivity.this, TermAndConditionUserActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onBackBtnListener()
    {
        return v ->
        {
            Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        };
    }
}