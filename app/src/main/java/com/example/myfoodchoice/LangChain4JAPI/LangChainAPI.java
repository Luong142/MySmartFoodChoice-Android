package com.example.myfoodchoice.LangChain4JAPI;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.List;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

public class LangChainAPI
{
    // todo: the key is wrong, update the key later
    // todo: the key from OpenAI is not free to use
    // todo: must use the hugging face as it is free, Mistral model.
    //  Testing is required in welcome activity
    // static String apiKey = System.getenv("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");

    static ChatLanguageModel model = new ChatLanguageModel()
    {
        @Override
        public String generate(String userMessage)
        {
            return ChatLanguageModel.super.generate(userMessage);
        }

        @Override
        public Response<AiMessage> generate(ChatMessage... messages)
        {
            return ChatLanguageModel.super.generate(messages);
        }

        @Nullable
        @Contract(pure = true)
        @Override
        public Response<AiMessage> generate(List<ChatMessage> messages)
        {
            return null;
        }

        @Override
        public Response<AiMessage> generate(List<ChatMessage> messages,
                                            List<ToolSpecification> toolSpecifications)
        {
            return ChatLanguageModel.super.generate(messages, toolSpecifications);
        }

        @Override
        public Response<AiMessage> generate(List<ChatMessage> messages,
                                            ToolSpecification toolSpecification)
        {
            return ChatLanguageModel.super.generate(messages, toolSpecification);
        }
    };

    // static OpenAiChatModel openAiChatModel = OpenAiChatModel.withApiKey(apiKey);

    public static String testMessage()
    {
        return model.generate("Hello world!");
    }
}
