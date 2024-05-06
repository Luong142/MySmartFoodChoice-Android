package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.ModelChatGPT.ChatInterface;

import java.util.ArrayList;

public class UserChatMessageAdapter extends RecyclerView.Adapter<UserChatMessageAdapter.myViewHolder>
{
    public ArrayList<ChatInterface> chatInterfaces;


    public static class myViewHolder extends RecyclerView.ViewHolder
    {



        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);




        }
    }

    @NonNull
    @Override
    public UserChatMessageAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatMessageAdapter.myViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return chatInterfaces.size();
    }
}
