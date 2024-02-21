package com.example.myfoodchoice.Database;

import android.content.Context;

import io.appwrite.Client;
import io.appwrite.services.Account;

public class AppWriteDatabase
{
    // TODO: research about the app write or either ROOM database for better application.
    Client client;

    Account account;

    public AppWriteDatabase(Context context)
    {
        client = new Client(context);
        client.setEndpoint("https://cloud.appwrite.io/v1");
        client.setProject("63a7c9c7f5b3b6c6c7c2");
        client.setSelfSigned(true);

        account = new Account(client);
    }

}
