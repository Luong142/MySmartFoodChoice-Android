package com.example.myfoodchoice.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Account implements Parcelable
{
    private String email;
    private String password;

    private String accountType;

    public Account()
    {
        // for firebase to paste on this default constructor.
        // account type can be guest, user, dietitian, trainer.
    }

    public Account(String username, String password)
    {
        this.email = username;
        this.password = password;
    }

    public Account(String username, String password, String accountType)
    {
        this.email = username;
        this.password = password;
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    protected Account(@NonNull Parcel in)
    {
        email = in.readString();
        password = in.readString();
        accountType = in.readString();
    }

    @NonNull
    @Override
    public String toString()
    {
        return "UserAccount{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }

    public static final Creator<Account> CREATOR = new Creator<Account>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setEmail(String username)
    {
        this.email = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags)
    {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(accountType);
    }
}
