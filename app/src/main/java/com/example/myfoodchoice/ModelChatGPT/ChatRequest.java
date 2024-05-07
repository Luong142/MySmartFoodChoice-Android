package com.example.myfoodchoice.ModelChatGPT;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatRequest implements Serializable
{
  private ArrayList<Messages> messages;

  private String model;

  public ChatRequest()
  {

  }

  public ChatRequest(ArrayList<Messages> messages, String model)
  {
    this.messages = messages;
    this.model = model;
  }



  @NonNull
  @Override
  public String toString()
  {
    return "ChatGPT{" +
            "messages=" + messages +
            ", model='" + model + '\'' +
            '}';
  }

  public ArrayList<Messages> getMessages() {
    return this.messages;
  }

  public void setMessages(ArrayList<Messages> messages) {
    this.messages = messages;
  }

  public String getModel() {
    return this.model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public static class Messages implements Serializable {
    private String role;

    private String content;

    public Messages() {
    }

    public Messages(String role, String content)
    {
      this.role = role;
      this.content = content;
    }

    @NonNull
    @Override
    public String toString()
    {
      return "Messages{" +
              "role='" + role + '\'' +
              ", content='" + content + '\'' +
              '}';
    }

    public String getRole() {
      return this.role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getContent() {
      return this.content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }
}

/*
@NonNull
  public static String callChatGPTAPI(String question)
  {
    // todo: this doesn't work
      String url = "https://api.openai.com/v1/chat/completions";
      String key = "sk-proj-hRgNqOrGwAbzZEsnohLoT3BlbkFJ9QZksy1vZGTz8GHXnWcs";
      String model = "gpt-3.5-turbo";
      String answer = "";

      try
      {
        // build header
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + key);
        con.setRequestProperty("Content-Type", "application/json");

        // build body
        String body = "{\"model\":\"" + model + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + question + "\"}]}";
        con.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();

        // get response
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = bufferedReader.readLine()) != null)
        {
          response.append(inputLine);
        }
        bufferedReader.close();

        answer = (response.toString().split("\"content\":\"")[1].split("\"")[0]).substring(4);
        return answer;
      }
      catch (IOException e)
      {
          if (e.getMessage() != null)
          {
              Log.d("Error", e.getMessage());
          }
      }
      return answer;
  }
 */
