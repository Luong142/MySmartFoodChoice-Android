package com.example.myfoodchoice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myfoodchoice.Database.AppWriteDatabase;
import com.example.myfoodchoice.R;

public class LoginActivity extends AppCompatActivity
{
    private AppWriteDatabase appWriteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // TODO: research about FireBase on this link:
        // TODO:
        //  https://www.youtube.com/watch?v=HYzw8LFvmw4&list=PLS1QulWo1RIbKsL9GqxOLbToLNFFQFJW_&ab_channel=ProgrammingKnowledge
        appWriteDatabase = new AppWriteDatabase(this  );
        // appWriteDatabase.
    }

    // TODO: to implement the login functionalities for this activity.


}