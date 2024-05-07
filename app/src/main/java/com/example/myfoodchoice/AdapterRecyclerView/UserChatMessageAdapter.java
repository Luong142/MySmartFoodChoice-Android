package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.ModelChatGPT.Message;
import com.example.myfoodchoice.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserChatMessageAdapter extends RecyclerView.Adapter<UserChatMessageAdapter.myViewHolder>
{
    public ArrayList<Message> messages;

    public UserChatMessageAdapter(ArrayList<Message> messages)
    {
        this.messages = messages;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout leftChatView, rightChatView;

        TextView leftChatText, rightChatText;

        ImageView botIcon, userIcon;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            // todo: init ui here
            leftChatView = itemView.findViewById(R.id.leftChatView);
            rightChatView = itemView.findViewById(R.id.rightChatView);
            leftChatText = itemView.findViewById(R.id.leftChatText);
            rightChatText = itemView.findViewById(R.id.rightChatText);
        }
    }

    @NonNull
    @Override
    public UserChatMessageAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.chat_item_layout, parent, false);
        return new UserChatMessageAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatMessageAdapter.myViewHolder holder, int position)
    {
        Message message = messages.get(position);

        if (message.getSendBy().equals(Message.SEND_BY_ME))
        {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightChatText.setText(message.getMessage());
        }
        else
        {
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatText.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount()
    {
        return messages.size();
    }
}
