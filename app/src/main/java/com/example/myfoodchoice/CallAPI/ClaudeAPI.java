package com.example.myfoodchoice.CallAPI;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
public class ClaudeAPI // FIXME: the problem is that we can't use the legacy model like Claude Instant.
    // FIXME: they don't support those models anymore. New model is more expensive and not free for Claude 3 family models.
{
    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String API_KEY =
            "put key here";
    // replace with your API key

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    public ClaudeAPI()
    {
        client = new OkHttpClient();
    }

    public void callAPI(String prompt, Callback callback)
    {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "claude-3-haiku-20240307");
            jsonBody.put("max_tokens", 1024);
            jsonBody.put("prompt", prompt);

            JSONArray messages = new JSONArray();
            JSONObject messageObj = new JSONObject();
            messageObj.put("role", "user");
            messageObj.put("content", prompt);
            messages.put(messageObj);

            Log.d("API", "Message: " + messageObj);
            Log.d("API", "Messages: " + messages);

            jsonBody.put("messages", messages);

            Log.d("API", "JSON body: " + jsonBody);
        }
        catch (JSONException e)
        {
            Log.d("JSON", "Error creating JSON body: " + e.getMessage());
            return;
        }

        // FIXME: the problem is that the format for this request body is incorrect

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Log.d("API", "Request body: " + jsonBody);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-api-key", API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("anthropic-version", "2023-06-01")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
