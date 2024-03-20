package com.example.myfoodchoice.CallAPI;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatGPTAPI   // TODO: testing this class before using API for health tips and work out plan.
{
    private final static String API_KEY = "key here"; // for ChatGPT api key here

    private final static String API_URL = "https://api.openai.com/v1/completions";

    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_BACKOFF_DELAY = 500; // 500 milliseconds

    private String answer;

    public ChatGPTAPI()
    {
        answer = "";
    }

    public void generateOutput(String prompt, Context applicationContext)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("model", "davinci-002");
            jsonObject.put("prompt", prompt);
            jsonObject.put("max_tokens", 100);
            jsonObject.put("temperature", 1);
        }
        catch (JSONException e)
        {
            Log.e("API", "Error creating JSON object: " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                API_URL, jsonObject,
                responseListener(), error ->
        {
            int statusCode = error.networkResponse != null ? error.networkResponse.statusCode : 0;
            Log.e("API", "Error making API request: " + error.getMessage());
            Log.e("API", "Status Code: " + statusCode);
            Log.e("API", "Network Time (ms): " + error.getNetworkTimeMs());

            if (statusCode == 429) { // Too Many Requests
                int retryCount = 0;
                int backoffDelay = INITIAL_BACKOFF_DELAY;

                while (retryCount < MAX_RETRIES)
                {
                    try
                    {
                        Thread.sleep(backoffDelay);
                    }
                    catch (InterruptedException e)
                    {
                        Log.e("API", "Error during backoff delay: " + e.getMessage());
                    }

                    // Retry the request
                    generateOutput("Your prompt", applicationContext.getApplicationContext());
                    retryCount++;
                    backoffDelay *= 2; // Exponential backoff
                }
            }
        })
        {
            // FIXME: the problem is that the 404 error
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + API_KEY);
                headers.put("Content-Type", "application/json");
                Log.d("API", "Headers: " + headers);
                return headers;
            }
        };
        Volley.newRequestQueue(applicationContext).add(jsonObjectRequest);
    }

    @NonNull
    @Contract(" -> new")
    private Response.Listener<JSONObject> responseListener()
    {
        return response ->
        {
            // TODO: handle response.
            try
            {
                this.answer = this.answer + response.getJSONArray("choices")
                        .getJSONObject(0)
                        .getString("text");
                Log.d("API", this.answer);
            }
            catch (JSONException e)
            {
                Log.e("API", "Error parsing JSON response: " + e.getMessage());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private Response.ErrorListener errorListener()
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {

            }
        };
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
