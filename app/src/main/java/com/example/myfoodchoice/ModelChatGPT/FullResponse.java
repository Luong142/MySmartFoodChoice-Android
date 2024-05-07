package com.example.myfoodchoice.ModelChatGPT;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class FullResponse implements Serializable
{
  private Integer created;

  private Usage usage;

  private String model;

  private String id;

  private List<Choices> choices;

  private String object;


  @NonNull
  @Override
  public String toString() {
    return "FullResponse{" +
            "created=" + created +
            ", usage=" + usage +
            ", model='" + model + '\'' +
            ", id='" + id + '\'' +
            ", choices=" + choices +
            ", object='" + object + '\'' +
            '}';
  }

  public Integer getCreated() {
    return this.created;
  }

  public void setCreated(Integer created) {
    this.created = created;
  }

  public Usage getUsage() {
    return this.usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public String getModel() {
    return this.model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Choices> getChoices() {
    return this.choices;
  }

  public void setChoices(List<Choices> choices) {
    this.choices = choices;
  }

  public String getObject() {
    return this.object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public static class Usage implements Serializable {
    private Integer completion_tokens;

    private Integer prompt_tokens;

    private Integer total_tokens;

    @NonNull
    @Override
    public String toString()
    {
      return "Usage{" +
              "completion_tokens=" + completion_tokens +
              ", prompt_tokens=" + prompt_tokens +
              ", total_tokens=" + total_tokens +
              '}';
    }

    public Integer getCompletion_tokens() {
      return this.completion_tokens;
    }

    public void setCompletion_tokens(Integer completion_tokens) {
      this.completion_tokens = completion_tokens;
    }

    public Integer getPrompt_tokens() {
      return this.prompt_tokens;
    }

    public void setPrompt_tokens(Integer prompt_tokens) {
      this.prompt_tokens = prompt_tokens;
    }

    public Integer getTotal_tokens() {
      return this.total_tokens;
    }

    public void setTotal_tokens(Integer total_tokens) {
      this.total_tokens = total_tokens;
    }
  }

  public static class Choices implements Serializable {
    private String finish_reason;

    private Integer index;

    private Message message;

    private Object logprobs;

    @NonNull
    @Override
    public String toString()
    {
      return "Choices{" +
              "finish_reason='" + finish_reason + '\'' +
              ", index=" + index +
              ", message=" + message +
              ", logprobs=" + logprobs +
              '}';
    }

    public String getFinish_reason() {
      return this.finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
      this.finish_reason = finish_reason;
    }

    public Integer getIndex() {
      return this.index;
    }

    public void setIndex(Integer index) {
      this.index = index;
    }

    public Message getMessage() {
      return this.message;
    }

    public void setMessage(Message message) {
      this.message = message;
    }

    public Object getLogprobs() {
      return this.logprobs;
    }

    public void setLogprobs(Object logprobs) {
      this.logprobs = logprobs;
    }

    public static class Message implements Serializable
    {
      private String role;

      private String content;

      @NonNull
      @Override
      public String toString()
      {
        return "Message{" +
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
}
