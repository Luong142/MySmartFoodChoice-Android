package com.example.myfoodchoice.ModelChatGPT;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitChatGPTAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallChatAPI
{
    private static final String TAG = "Test";

    public static String answer = ""; // fixme: it is not possible to make this a separate function

    public static void makeChatGPTRequest(String question, String context)
    {
        // Create an instance of ChatGPT
        ChatRequest chatRequest = new ChatRequest();
        // Populate the chatGPTRequest object with your data
        // For example:
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
}
