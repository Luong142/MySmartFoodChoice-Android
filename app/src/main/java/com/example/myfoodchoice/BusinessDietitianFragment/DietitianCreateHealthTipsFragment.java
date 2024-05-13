package com.example.myfoodchoice.BusinessDietitianFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodchoice.ModelChatGPT.ChatRequest;
import com.example.myfoodchoice.ModelChatGPT.FullResponse;
import com.example.myfoodchoice.ModelDietitian.HealthTips;
import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;
import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitChatGPTAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietitianCreateHealthTipsFragment extends Fragment
{
    // todo: declare firebase here
    static final String PATH_HEALTH_TIPS = "Dietitian Health Tips";

    static final String PATH_BUSINESS_PROFILE = "Business Profile";
    // todo: our plan is to let the dietitian to create the recipe manually
    //  or search for recipe to add for firebase database.
    // todo: the recipe should be recommended by the dietitian.
    static final String TAG = "DietitianCreateHealthTipsFragment";

    DatabaseReference databaseReferenceHealthTips,
            databaseReferenceHealthTipsChild, databaseReferenceDietitianProfile;

    BusinessProfile businessProfile;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    String dietitianID, generatedAnswer, dietitianProfileImage, dietitianInfo;

    // todo: declare UI components
    EditText titleHealthTips, contentHealthTips;

    Button generateAIContentBtn, createHealthTipsBtn, clearAllBtn;

    ProgressBar progressBarGenerateAI, progressBarHealthTips;

    UserProfile selectedUserProfile;

    HealthTips healthTips;

    Bundle bundleStore;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // todo: this will help not to push the content up
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // init bundle
        bundleStore = getArguments();
        if (bundleStore != null)
        {
            selectedUserProfile = bundleStore.getParcelable("selectedUserProfile");
        }

        // TODO: init Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            dietitianID = firebaseUser.getUid();

            databaseReferenceHealthTips = firebaseDatabase.getReference(PATH_HEALTH_TIPS).child(dietitianID);

            databaseReferenceDietitianProfile = firebaseDatabase
                    .getReference(PATH_BUSINESS_PROFILE).child(dietitianID);

            databaseReferenceDietitianProfile.addValueEventListener(onDietitianProfileListener());
        }

        // todo: init UI components
        titleHealthTips = view.findViewById(R.id.titleHealthTips);
        contentHealthTips = view.findViewById(R.id.contentHealthTips);
        generateAIContentBtn = view.findViewById(R.id.generateAIContentBtn);
        createHealthTipsBtn = view.findViewById(R.id.createHealthTipsBtn);
        progressBarGenerateAI = view.findViewById(R.id.progressBarGenerateAI);
        progressBarHealthTips = view.findViewById(R.id.progressBarHealthTips);
        clearAllBtn = view.findViewById(R.id.clearAllBtn);

        // set visibility
        progressBarGenerateAI.setVisibility(View.GONE);
        progressBarHealthTips.setVisibility(View.GONE);


        // todo: set on click for all buttons
        generateAIContentBtn.setOnClickListener(onGenerateAIContentListener());
        createHealthTipsBtn.setOnClickListener(onCreateHealthTipsListener());
        clearAllBtn.setOnClickListener(onClearAllListener());
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener onDietitianProfileListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot)
            {
                businessProfile = snapshot.getValue(BusinessProfile.class);

                if (businessProfile != null)
                {
                    dietitianProfileImage = businessProfile.getProfileImageUrl();
                    dietitianInfo = businessProfile.getDietitianInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error)
            {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onCreateHealthTipsListener()
    {
        return v ->
        {
            if (titleHealthTips.getText().toString().isEmpty())
            {
                titleHealthTips.setError("Please enter title");
                return;
            }

            if (contentHealthTips.getText().toString().isEmpty())
            {
                contentHealthTips.setError("Please enter content");
                return;
            }

            // set visibility
            createHealthTipsBtn.setVisibility(View.GONE);
            progressBarHealthTips.setVisibility(View.VISIBLE);

            String userKey = selectedUserProfile.getKey();
            String title = titleHealthTips.getText().toString().trim();
            String content = contentHealthTips.getText().toString().trim();

            // create health tips
            healthTips = new HealthTips(userKey, title, content);
            healthTips.setDietitianKey(dietitianID);
            healthTips.setDietitianProfileImage(dietitianProfileImage);
            healthTips.setDietitianInfo(dietitianInfo);

            databaseReferenceHealthTipsChild = databaseReferenceHealthTips.push();
            databaseReferenceHealthTipsChild.setValue(healthTips).addOnCompleteListener
                    (onCompleteCreateHealthTipsListener());
        };
    }

    @NonNull
    @Contract(pure = true)
    private OnCompleteListener<Void> onCompleteCreateHealthTipsListener()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                // make toast
                Toast.makeText(requireContext(),
                        "Create Health tip successfully.", Toast.LENGTH_LONG).show();

                // set visibility
                createHealthTipsBtn.setVisibility(View.VISIBLE);
                progressBarHealthTips.setVisibility(View.GONE);

                // reset all
                titleHealthTips.setText("");
                contentHealthTips.setText("");
            }
            else
            {
                Log.d(TAG, "Error: " + task.getException());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onGenerateAIContentListener()
    {
        return v ->
        {
            String detail = selectedUserProfile.getFullUserDetail();
            String question = "As a professional Dietitian, " +
                    "generate concise, informative, and easy-to-follow " +
                    "health tips based on the following detail: " + detail;
            String context = "You are the professional Dietitian, " +
                    "as I use EditText from Android Studio so it needs to be clean, " +
                    "short and informative. " +
                    "Must be short as much as possible";

            // to set the visibility.
            generateAIContentBtn.setVisibility(View.GONE);
            progressBarGenerateAI.setVisibility(View.VISIBLE);

            // call API here
            makeChatGPTRequest(question, context);
        };
    }

    @NonNull
    @Contract(" -> new")
    private Callback<FullResponse> onCallChatGPTResponseBack()
    {
        // todo: pls note that whenever using this feature we need to get the API key again from OpenAI.
        // because of security Github doesn't allow to commit API key (can be stolen by others)
        return new Callback<FullResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<FullResponse> call,
                                   @NonNull Response<FullResponse> response)
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
                            generatedAnswer = choices.getMessage().getContent();
                            contentHealthTips.setText(generatedAnswer);
                        }

                        // until we have response.
                        if (generatedAnswer != null)
                        {
                            generateAIContentBtn.setVisibility(View.VISIBLE);
                            progressBarGenerateAI.setVisibility(View.GONE);

                            // reset this
                            generatedAnswer = "";
                        }
                    }
                }
                else
                {
                    // handle error
                    Log.d(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FullResponse> call, @NonNull Throwable t)
            {
                // Handle failure
                Log.d(TAG, "Failure: " + t.getMessage());
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener onClearAllListener()
    {
        return v ->
        {
            titleHealthTips.setText("");
            contentHealthTips.setText("");
        };
    }


    public void makeChatGPTRequest(String question, String context)
    {
        // Create an instance of ChatGPT
        ChatRequest chatRequest = new ChatRequest();

        // set model here.
        chatRequest.setModel("gpt-3.5-turbo");

        ArrayList<ChatRequest.Messages> messages = new ArrayList<>();

        // init here
        ChatRequest.Messages messages0 = new ChatRequest.Messages("system",
                context);
        ChatRequest.Messages messages1 = new ChatRequest.Messages("user", question);

        // add here
        messages.add(messages0);
        messages.add(messages1);

        chatRequest.setMessages(messages);

        // Create the API service
        ChatGPTAPI chatGPTAPI = RetrofitChatGPTAPI.getRetrofitChatGPTInstance()
                .create(ChatGPTAPI.class);

        // Make the POST request
        Call<FullResponse> call = chatGPTAPI.getChatCompletion(chatRequest);
        call.enqueue(onCallChatGPTResponseBack());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dietitian_create_health_tips, container, false);
    }
}