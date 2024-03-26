package com.example.myfoodchoice.SharedReviewFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myfoodchoice.ModelSignUp.Account;
import com.example.myfoodchoice.ModelUtilities.Review;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

public class ReviewDialogFragment extends DialogFragment
{
    RatingBar ratingBar;
    EditText reviewTextEdit;

    // for firebase
    DatabaseReference databaseReferenceReview;

    DatabaseReference databaseReferenceUserType;

    DatabaseReference databaseReferenceNewChild;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID, userType, displayName;

    Review review;

    Account account;

    final static String PATH_REVIEW = "Reviews";

    final static String PATH_ACCOUNT = "Registered Accounts";

    final static String PATH_PROFILE = "Profile";

    final static String TAG = "ReviewDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        if (getActivity() == null) {
            Log.e(TAG, "Fragment not attached to an activity");
            // Handle the error appropriately, e.g., by returning a dummy dialog or not showing the dialog at all
            return new Dialog(requireContext()); // Fallback to a dummy dialog
        }

        // Use requireActivity() to ensure a non-null context
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_review, null);

        // TODO: init firebase components.

        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // todo: init database reference for review
            databaseReferenceReview = firebaseDatabase.getReference(PATH_REVIEW).child(userID);

            // todo: init user type and retrieve it from database reference
            databaseReferenceUserType = firebaseDatabase.getReference(PATH_ACCOUNT).child(userID);

            // get the first name and last name together as display name
            displayName = firebaseUser.getDisplayName();
            // Log.d(TAG, "onCreateDialog: " + displayName);
            databaseReferenceUserType.addListenerForSingleValueEvent(valueUserTypeEventListener());
        }

        reviewTextEdit = view.findViewById(R.id.reviewEditText);
        ratingBar = view.findViewById(R.id.ratingBar);

        // todo: the problem is that the theme color can't be changed
        builder.setView(view).setPositiveButton("Submit", onSubmitListener());
        builder.setView(view).setNegativeButton("Cancel", onCancelListener());

        return builder.create();
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueUserTypeEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                account = snapshot.getValue(Account.class);
                // get the user type from account
                if (account != null)
                {
                    userType = account.getAccountType();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private DialogInterface.OnClickListener onSubmitListener()
    {
        return (dialog, which) ->
        {
            String reviewText = reviewTextEdit.getText().toString();
            float rating = ratingBar.getRating();
            // TODO: Handle the review submission here.
            databaseReferenceNewChild = databaseReferenceReview.push();
            review = new Review(reviewText, displayName, rating);
            review.setKey(databaseReferenceNewChild.getKey());
            databaseReferenceNewChild.setValue(review).addOnCompleteListener(onCompleteSetValueListener())
                    .addOnFailureListener(onFailureSetValueListener());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnFailureListener onFailureSetValueListener()
    {
        return e -> Log.d(TAG, "onFailureSetValueListener: " + e.getMessage());
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteSetValueListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Log.d(TAG, "onCompleteSetValueListener: " + task);
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private DialogInterface.OnClickListener onCancelListener()
    {
        return (dialog, which) -> dismiss();
    }
}
