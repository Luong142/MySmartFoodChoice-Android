package com.example.myfoodchoice.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class UserAccount implements Parcelable
{
    private String email;
    private String password;

    private String accountType;

    public UserAccount()
    {
        // for firebase to paste on this default constructor.
    }

    public UserAccount(String username, String password)
    {
        this.email = username;
        this.password = password;
    }

    public UserAccount(String username, String password, String accountType)
    {
        this.email = username;
        this.password = password;
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    protected UserAccount(@NonNull Parcel in)
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

    public static final Creator<UserAccount> CREATOR = new Creator<UserAccount>()
    {
        @NonNull
        @Contract("_ -> new")
        @Override
        public UserAccount createFromParcel(Parcel in) {
            return new UserAccount(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public UserAccount[] newArray(int size) {
            return new UserAccount[size];
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
