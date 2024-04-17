package com.example.myfoodchoice.UserFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.myfoodchoice.AdapterInterfaceListener.OnRewardItemRedeemClickListener;
import com.example.myfoodchoice.AdapterRecyclerView.RewardUserAdapter;
import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.ModelUtilities.Reward;
import com.example.myfoodchoice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class UserRewardsFragment extends Fragment implements OnRewardItemRedeemClickListener
{
    // TODO: declare components

    private static final String TAG = "UserRewardsFragment";

    ImageView userProfileImage;

    TextView fullNameTextView, userPointsTextView;

    RecyclerView rewardRecyclerView;

    RewardUserAdapter rewardUserAdapter;

    private ArrayList<Reward> rewardList, tempRewardList;


    // TODO: declare firebase components here
    DatabaseReference databaseReferenceUserProfile, databaseReferenceRewards;

    StorageReference storageReferenceRewardsImage, storageReferenceStaticRewardsImage;

    StorageTask<UploadTask.TaskSnapshot> storageTask;

    final static String PATH_REWARDS = "Rewards";

    final static String PATH_UserProfile = "User Profile";

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser firebaseUser;

    UserProfile userProfile;

    String userID;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // TODO: init firebase components.

        firebaseDatabase = FirebaseDatabase.getInstance
                ("https://myfoodchoice-dc7bd-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // TODO: init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // TODO: init user id
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            userID = firebaseUser.getUid();

            // TODO: init database reference for user profile
            databaseReferenceUserProfile = firebaseDatabase.getReference(PATH_UserProfile).child(userID);
            databaseReferenceRewards = firebaseDatabase.getReference(PATH_REWARDS);

            storageReferenceRewardsImage = FirebaseStorage.getInstance().getReference()
                    .child("Rewards Images");

            storageReferenceStaticRewardsImage = FirebaseStorage.getInstance().getReference()
                    .child("Static Rewards Images");

            databaseReferenceUserProfile.addValueEventListener(valuePointUserProfileEventListener());

            // set display reward here.
            databaseReferenceRewards.addValueEventListener(valueReadRewardListener());
        }

        // TODO: init UI components
        userProfileImage = view.findViewById(R.id.userProfileImageView);
        fullNameTextView = view.findViewById(R.id.userNameTextView);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);

        // Initialize the recipeList
        rewardList = new ArrayList<>();
        populateTempRewardList();

        // for recycle view
        rewardRecyclerView = view.findViewById(R.id.rewardsRecyclerView);
        rewardUserAdapter = new RewardUserAdapter(rewardList, this);
        setAdapter();
        rewardRecyclerView.setVerticalScrollBarEnabled(true);

        // Set the adapter to the RecyclerView
        // recipeRecyclerView.setAdapter(adapter);

        // TODO: Populate the recipeList with your Recipe data


        // Notify the adapter that the data has changed
        // recipeItemAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valueReadRewardListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                rewardList.clear();
                for (DataSnapshot rewardSnapshot : dataSnapshot.getChildren())
                {
                    Reward reward = rewardSnapshot.getValue(Reward.class);
                    rewardList.add(reward);
                }
                rewardUserAdapter.notifyItemChanged(rewardList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };
    }

    @Override
    public void onRewardItemRedeemClick(int position) // click on button not the entire view.
    {
        // TODO: implement onClick
        double currentPoint = userProfile.getPoints();
        Map<String, Object> updates = new HashMap<>();

        // to check if the point <= 0
        if (currentPoint <= 0 || currentPoint < rewardList.get(position).getPoints())
        {
            Toast.makeText(getContext(), "You don't have enough points to redeem", Toast.LENGTH_SHORT).show();
            return;
        }

        updates.put("points", currentPoint - rewardList.get(position).getPoints());

        databaseReferenceUserProfile.updateChildren(updates, onRedeemItemListener());
    }

    @NonNull
    @Contract(" -> new")
    private DatabaseReference.CompletionListener onRedeemItemListener()
    {
        return (error, ref) ->
        {
            if (error != null)
            {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getContext(), "Redeem Success", Toast.LENGTH_SHORT).show();
        };
    }

    private void populateTempRewardList()
    {
        // init here
        Reward reward = new Reward("Discount",
                "10% off for Disney Land trip", 10);

        Reward reward1 = new Reward("Discount",
                "50% off for RTX 4090", 50);

        Reward reward2 = new Reward("Discount",
                "20% off for Premium User Account", 20);

        Reward reward3 = new Reward("Voucher",
                "Voucher to get free plastic bottle", 30);

        Reward reward4 = new Reward("Voucher",
                "Voucher to get free orange", 40);

        Reward reward5 = new Reward("Voucher",
                "Voucher to get free apple", 20);

        Reward reward6 = new Reward("Voucher",
                "Voucher to get free banana", 10);

        Reward reward7 = new Reward("Voucher",
                "Voucher to get free pear", 30);

        // add here

        tempRewardList = new ArrayList<>();
        tempRewardList.add(reward);
        tempRewardList.add(reward1);
        tempRewardList.add(reward2);
        tempRewardList.add(reward3);
        tempRewardList.add(reward4);
        tempRewardList.add(reward5);
        tempRewardList.add(reward6);
        tempRewardList.add(reward7);
        // to upload and set the image to reward object
        // uploadRewardImageToStorage();
        // must do storage task to upload it to Firebase.
        // Log.d(TAG, "populateRewardList: " + storageTask.isComplete());
        readRewardImageToStorage();
    }

    private void readRewardImageToStorage()
    {
        // todo: init read the file image name
        ArrayList<String> imageNames = new ArrayList<>();
        imageNames.add("voucher.png");
        imageNames.add("gpu.png");

        for (String imageName : imageNames)
        {
            StorageReference imageRef = storageReferenceStaticRewardsImage.child(imageName);
            imageRef.getDownloadUrl().addOnSuccessListener(onOkListener());
        }
    }

    @NonNull
    @Contract(" -> new")
    private OnSuccessListener<? super Uri> onOkListener()
    {
        return (OnSuccessListener<Uri>) uri ->
        {
            if (uri != null)
            {
                // Log.d(TAG, "onOkListener: " + uri);
                for (int i = 0; i < tempRewardList.size(); i++)
                {
                    String path = String.format(Locale.ROOT, "%d", i);
                    tempRewardList.get(i).setRewardImageUrl(uri.toString());
                    databaseReferenceRewards.child(path).setValue(tempRewardList.get(i));
                }
                rewardList = tempRewardList;
                rewardUserAdapter.notifyItemChanged(rewardList.size() - 1);
            }
            else
            {
                Log.d(TAG, "onOkListener: " + "uri is null");
            }
        };
    }

    @NonNull
    @Contract(" -> new")
    private ValueEventListener valuePointUserProfileEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // FIXME: the problem is that userProfile is null.
                userProfile = snapshot.getValue(UserProfile.class);
                // Log.d(TAG, "onDataChange: " + userProfile);

                // set the first name and last name in the UI
                if (userProfile != null)
                {
                    // set full name here
                    String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                    fullNameTextView.setText(fullName);

                    // todo: set point here
                    userPointsTextView.setText(String.valueOf(userProfile.getPoints()));

                    // set profile picture here
                    String profileImageUrl = userProfile.getProfileImageUrl();
                    Uri profileImageUri = Uri.parse(profileImageUrl);
                    // FIXME: the image doesn't show because the image source is from Gallery within android device.
                    // Log.d(TAG, "onDataChange: " + profileImageUri.toString());
                    Picasso.get()
                            .load(profileImageUri)
                            .resize(150, 150)
                            .error(R.drawable.error)
                            .into(userProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText
                        (getContext(),
                                "Error database connection", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "loadUserProfile:onCancelled ", error.toException());
            }
        };
    }

    /*
    @Nullable
    private Bitmap drawableToBitmap(int drawableId)
    {
        VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(getResources(), drawableId, null);
        if (vectorDrawable != null)
        {
            // to convert the vector drawable to bitmap
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            return bitmap;
        }
        return null;
    }
    private void uploadRewardImageToStorage()
    {
        ArrayList<Integer> image = new ArrayList<>();

        // fixme: the problem is that it should be SVG
        image.add(R.drawable.app_icon);
        image.add(R.drawable.rtx4090);
        image.add(R.drawable.premium);
        image.add(R.drawable.water_bottle);
        image.add(R.drawable.voucher_beauty);
        image.add(R.drawable.voucher_beauty);
        image.add(R.drawable.voucher_beauty);
        image.add(R.drawable.voucher_beauty);

        // to convert and add
        for (int i = 0; i < image.size(); i++)
        {
            String imageName = String.format(Locale.ROOT, "%d.jpg", i);
            Bitmap imageBitmap = drawableToBitmap(image.get(i));

            File file = new File(requireActivity().getCacheDir(), imageName);
            try (FileOutputStream fos = new FileOutputStream(file))
            {
                if (imageBitmap != null)
                {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                }
            }
            catch (IOException e)
            {
                Log.d(TAG,  "drawableToBitmap: " + e.getMessage());
            }
            Uri imageUri = Uri.fromFile(file);
            if (imageUri != null)
            {
                //storageTask = storageReferenceRewardsImage.child(imageName).putFile(imageUri)
                //.addOnFailureListener(onFailureUploadImage());

                // todo: probably we might need a non-static reward images.

                storageTask.continueWithTask(task ->
                {
                    if (!task.isSuccessful())
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    // Log.d(TAG, "here is " + storageReferenceRewardsImage.getDownloadUrl());
                    return storageReferenceRewardsImage.child(imageName).getDownloadUrl();
                }).addOnCompleteListener(onCompleteUploadImage());
            }
        }
    }

    @NonNull
    @Contract(" -> new")
    private OnCompleteListener<Uri> onCompleteUploadImage()
    {
        return task ->
        {
            if (task.isSuccessful())
            {
                Uri downloadUri = task.getResult();
                String imageUrl = downloadUri.toString();

                // set the image url to the rewardList
                for (int i = 0; i < tempRewardList.size(); i++)
                {
                    tempRewardList.get(i).setRewardImageUrl(imageUrl);
                    // Log.d(TAG, "onCompleteUploadImage: " + tempRewardList.get(i));
                }
                populateRewardDatabase(tempRewardList);
            }
            else
            {
                // go to here, error, path is not existed.
                Log.d(TAG, "onCompleteUploadImage: " + task.getException());
            }
        };
    }
     */

    private void setAdapter()
    {
        // set the adapter
        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(requireActivity().getApplicationContext());
        rewardRecyclerView.setLayoutManager(layoutManager);
        rewardRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rewardRecyclerView.setAdapter(rewardUserAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_rewards, container, false);
    }
}