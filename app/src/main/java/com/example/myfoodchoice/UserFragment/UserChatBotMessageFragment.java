package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterRecyclerView.UserChatMessageAdapter;
import com.example.myfoodchoice.ModelChatGPT.ChatRequest;
import com.example.myfoodchoice.ModelChatGPT.FullResponse;
import com.example.myfoodchoice.ModelChatGPT.Message;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitChatGPTAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChatBotMessageFragment extends Fragment
{
    // todo: our plan is to make this as a premium feature.
    private static final String TAG = "UserChatBotMessageFragment";
    // todo: declare UI components
    RecyclerView chatRecyclerView;

    EditText editMessageText;

    FloatingActionButton sendMessageBtn;

    ArrayList<Message> messageArrayList;

    UserChatMessageAdapter userChatMessageAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // todo: init ui here
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        editMessageText = view.findViewById(R.id.editMessageText);
        sendMessageBtn = view.findViewById(R.id.sendMessageBtn);
        messageArrayList = new ArrayList<>();

        sendMessageBtn.setOnClickListener(onSendMessageListener());

        // set adapter here
        userChatMessageAdapter = new UserChatMessageAdapter(messageArrayList);
        chatRecyclerView.setAdapter(userChatMessageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.requireContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onSendMessageListener()
    {
        return v ->
        {
            String question = editMessageText.getText().toString().trim();
            addToChat(question, Message.SEND_BY_ME);
            editMessageText.setText("");

        };
    }

    public void makeChatGPTRequest(String question, String context)
    {
        // Create an instance of ChatGPT
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");

        ArrayList<ChatRequest.Messages> messages = new ArrayList<>();
        ChatRequest.Messages messages0 = new ChatRequest.Messages("system",
                context);
        ChatRequest.Messages messages1 = new ChatRequest.Messages("user", question);
        messages.add(messages1);

        chatRequest.setMessages(messages);

        // Log.d(TAG, "makeChatGPTRequest: " + chatRequest);

        // Create the API service
        ChatGPTAPI chatGPTAPI = RetrofitChatGPTAPI.getRetrofitChatGPTInstance().create(ChatGPTAPI.class);

        // Make the POST request
        Call<FullResponse> call = chatGPTAPI.getChatCompletion(chatRequest);
        call.enqueue(new Callback<FullResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<FullResponse> call, @NonNull Response<FullResponse> response)
            {
                if (response.isSuccessful())
                {
                    FullResponse chatResponse = response.body();
                    // Handle the response
                    if (chatResponse != null)
                    {
                        for (FullResponse.Choices choices : chatResponse.getChoices())
                        {
                            // get content to get the value
                            Log.d(TAG, "onResponse: " + choices.getMessage().getContent());
                        }
                    }
                }
                else
                {
                    // Handle error
                    Log.d(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FullResponse> call, @NonNull Throwable t)
            {
                // Handle failure
                Log.d(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    private void addToChat(String message, String sentBy)
    {
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(() ->
            {
                messageArrayList.add(new Message(message, sentBy));
                userChatMessageAdapter.notifyItemInserted(messageArrayList.size() - 1);
                chatRecyclerView.smoothScrollToPosition(userChatMessageAdapter.getItemCount());
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_chat_bot_message, container, false);
    }
}