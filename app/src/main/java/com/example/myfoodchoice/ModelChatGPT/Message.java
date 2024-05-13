package com.example.myfoodchoice.ModelChatGPT;

import androidx.annotation.NonNull;

public class Message
{
    public static String SEND_BY_ME = "me";
    public static String SEND_BY_BOT = "bot";
    private String message;

    private String sendBy;

    private String leftImage;

    private String rightImage;

    public Message(String message, String sendBy)
    {
        this.message = message;
        this.sendBy = sendBy;
    }

    public Message(String message, String sendBy, String leftImage, String rightImage) {
        this.message = message;
        this.sendBy = sendBy;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public Message() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sendBy='" + sendBy + '\'' +
                ", leftImage='" + leftImage + '\'' +
                ", rightImage='" + rightImage + '\'' +
                '}';
    }

    public String getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(String leftImage) {
        this.leftImage = leftImage;
    }

    public String getRightImage() {
        return rightImage;
    }

    public void setRightImage(String rightImage) {
        this.rightImage = rightImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }
}
