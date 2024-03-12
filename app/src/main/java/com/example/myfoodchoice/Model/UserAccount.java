package com.example.myfoodchoice.Model;

import androidx.annotation.NonNull;

public class UserAccount
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

    public String getAccountType() {
        return accountType;
    }

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

    @NonNull
    @Override
    public String toString()
    {
        return "UserAccount{" +
                "username='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
