package com.example.myfoodchoice.UserFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterRecyclerView.UserChatMessageAdapter;
import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;
import com.example.myfoodchoice.ModelChatGPT.ChatRequest;
import com.example.myfoodchoice.ModelChatGPT.FullResponse;
import com.example.myfoodchoice.ModelChatGPT.Message;
import com.example.myfoodchoice.ModelNutrition.NutritionMeal;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitChatGPTAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChatBotMessageFragment extends Fragment
{
    // todo: our plan is to make this as a premium feature.

    final static String PATH_USERPROFILE = "User Profile"; // FIXME: the path need to access the account.

    final static String PATH_DAILY_FOOD_INTAKE = "Meals"; // fixme:  the path need to access daily globalMeal.
    DatabaseReference databaseReferenceUserProfile, databaseReferenceMeals;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String userID;

    UserProfile userProfile;

    NutritionMeal globalNutritionMeal;

    FoodItem foodItem;

    List<FoodItem.Item> items;

    private static final String TAG = "UserChatBotMessageFragment";
    // todo: declare UI components
    RecyclerView chatRecyclerView;

    EditText editMessageText;

    FloatingActionButton sendMessageBtn;

    ArrayList<Message> messageArrayList;

    UserChatMessageAdapter userChatMessageAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_USERPROFILE).child(userID);
            databaseReferenceMeals =  firebaseDatabase.getReference(PATH_DAILY_FOOD_INTAKE).child(userID);

            databaseReferenceUserProfile.addValueEventListener(valueUserProfileEventListener());
            databaseReferenceMeals.addChildEventListener(valueChildMealEventListener());
        }

        // todo: init ui here
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        editMessageText = view.findViewById(R.id.editMessageText);
        sendMessageBtn = view.findViewById(R.id.sendMessageBtn);
        messageArrayList = new ArrayList<>();

        sendMessageBtn.setOnClickListener(onSendMessageListener());

        // set adapter here
        userChatMessageAdapter = new UserChatMessageAdapter(messageArrayList);
        chatRecyclerView.setAdapter(userChatMessageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.requireContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @NonNull
    @Contract(" -> new")
    private ChildEventListener valueChildMealEventListener()
    {
        return new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                globalNutritionMeal = snapshot.getValue(NutritionMeal.class);

                if (globalNutritionMeal == null)
                {
                    Log.d(TAG, "Meal is null error here! ");
                    return;
                }

                foodItem = globalNutritionMeal.getDishes();
                items = foodItem.getItems();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                globalNutritionMeal = snapshot.getValue(NutritionMeal.class);

                if (globalNutritionMeal == null)
                {
                    Log.d(TAG, "Meal is null error here! ");
                    return;
                }

                foodItem = globalNutritionMeal.getDishes();
                items = foodItem.getItems();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                globalNutritionMeal = snapshot.getValue(NutritionMeal.class);

                if (globalNutritionMeal == null)
                {
                    Log.d(TAG, "Meal is null error here! ");
                    return;
                }

                foodItem = globalNutritionMeal.getDishes();
                items = foodItem.getItems();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                globalNutritionMeal = snapshot.getValue(NutritionMeal.class);
                if (globalNutritionMeal != null)
                {
                    Log.d(TAG, "onChildAdded: " + globalNutritionMeal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueUserProfileEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    userProfile = snapshot.getValue(UserProfile.class);
                }
                else
                {
                    addResponse("Error, " + snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                addResponse("Error, " + error.getMessage());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onSendMessageListener()
    {
        return v ->
        {
            String question = editMessageText.getText().toString().trim();
            addToChat(question, Message.SEND_BY_ME);

            StringBuilder sbContext = new StringBuilder();
            sbContext.append("You are the best assistant in the world. " +
                    "You are a chat-bot in my Smart Food Choice Android App\n");
            sbContext.append("Your job is to provide friendly and formal to our user as they are valuable customer.");
            sbContext.append("Here is my user profile details\n").append(userProfile.getDetail());

            if (globalNutritionMeal != null)
            {
                sbContext.append("\nHere is my meal details\n").append(globalNutritionMeal.toString());
            }

            if (items != null)
            {
                sbContext.append("\nHere is my meal items details\n");
                for (FoodItem.Item item : items)
                {
                    sbContext.append(item.toString());
                }
            }

            makeChatGPTRequest(question, sbContext.toString());
            editMessageText.setText("");
        };
    }

    public void makeChatGPTRequest(String question, String context)
    {
        // todo: add initial "typing..."
        messageArrayList.add(new Message("Typing...", Message.SEND_BY_BOT));
        userChatMessageAdapter.notifyItemInserted(messageArrayList.size());

        // todo: it is cheap to use gpt 3.5
        ChatRequest chatRequest = getChatRequest(question, context);

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
                            addResponse(choices.getMessage().getContent().trim());
                        }
                    }
                }
                else
                {
                    addResponse("Error, please contact our developer. For feedback.\n"
                            + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FullResponse> call, @NonNull Throwable t)
            {
                addResponse("Error, please contact our developer. For feedback.\n"
                        + t.getMessage());
            }
        });
    }

    @NonNull // todo: this method for init the message.
    private static ChatRequest getChatRequest(String question, String context)
    {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");

        ArrayList<ChatRequest.Messages> messages = new ArrayList<>();

        // init message with two types one is for AI, one is for user.
        ChatRequest.Messages messages0 = new ChatRequest.Messages("system", context);
        ChatRequest.Messages messages1 = new ChatRequest.Messages("user", question);

        // add two of them to list.
        messages.add(messages1);
        messages.add(messages0);

        chatRequest.setMessages(messages);
        return chatRequest;
    }

    private void addResponse(String response)
    {
        if (!messageArrayList.isEmpty())
        {
            messageArrayList.remove(messageArrayList.size() - 1);
            userChatMessageAdapter.notifyItemRemoved(messageArrayList.size());
        }
        addToChat(response, Message.SEND_BY_BOT);
    }
    private void addToChat(String message, String sentBy)
    {
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(() ->
            {
                messageArrayList.add(new Message(message, sentBy));
                userChatMessageAdapter.notifyItemInserted(messageArrayList.size() - 1);
                chatRecyclerView.smoothScrollToPosition(userChatMessageAdapter.getItemCount());
            });
        }
        else
        {
            Log.d(TAG, "addToChat: activity is null");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_chat_bot_message, container, false);
    }
}