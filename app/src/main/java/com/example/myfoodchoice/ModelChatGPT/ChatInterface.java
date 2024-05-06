package com.example.myfoodchoice.ModelChatGPT;

import androidx.annotation.NonNull;

public class ChatInterface
{
    private String message;

    private String replies;

    public ChatInterface(String message, String replies) {
        this.message = message;
        this.replies = replies;
    }

    public ChatInterface() {
    }

    @NonNull
    @Override
    public String toString()
    {
        return "ChatInterface{" +
                "message='" + message + '\'' +
                ", replies='" + replies + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }
}
