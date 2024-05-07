package com.example.myfoodchoice.RetrofitProvider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatGPTAPIKey
{
    public DatabaseReference databaseReference;

    public FirebaseDatabase firebaseDatabase;

    public String apiKey;


    public String getApiKey()
    {

        firebaseDatabase = FirebaseDatabase.getInstance(
                "https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("API key");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot)
            {
                apiKey = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError)
            {
                Log.d("API key", databaseError.getMessage());
            }
        });
        return apiKey;
    }
}
