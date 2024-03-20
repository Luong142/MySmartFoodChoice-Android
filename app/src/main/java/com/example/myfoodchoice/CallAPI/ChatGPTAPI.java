package com.example.myfoodchoice.CallAPI;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatGPTAPI
{
    public final static String API_KEY = "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"; // for ChatGPT api key here

    public final static String API_URL = "https://api.openai.com/v1/completions";

    private String answer;

    public ChatGPTAPI()
    {
        answer = "";
    }

    public String generateOutput(String prompt, Context applicationContext)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("model", "text-davinci-002");
            jsonObject.put("prompt", prompt);
            jsonObject.put("max_tokens", 100);
            jsonObject.put("temperature", 1);


        }
        catch (JSONException e)
        {
            Log.e("API", "Error creating JSON object: " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                API_URL, jsonObject, responseListener(), errorListener());

        Volley.newRequestQueue(applicationContext).add(jsonObjectRequest);

        return answer;
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
                Log.d("API", answer);
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
        return error ->
        {
            Log.e("Error", "Error occurred: " + error.getMessage());
        };
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
