package com.example.myfoodchoice.LangChain4JAPI;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LangChain4JAPI
{
    // todo: the key is wrong, update the key later
    // todo: the key from OpenAI is not free to use
    // todo: must use the hugging face as it is free, Mistral model.
    //  Testing is required in welcome activity

    public static void main(String[] args)
    {
        // we can use firebase to store this API-KEY
        String apiKey = System.getenv("OPENAI_API_KEY");
        ChatLanguageModel model = OpenAiChatModel.withApiKey(apiKey);

        String answer = model.generate("Hello world!");
        System.out.println(answer);
    }
}
