package com.example.myfoodchoice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfoodchoice.ModelSignUp.UserProfile;
import com.example.myfoodchoice.R;

import java.util.ArrayList;
import java.util.Objects;

public class ActivityLevelAdapter extends ArrayAdapter<UserProfile>
{
    public ActivityLevelAdapter(Context context, ArrayList<UserProfile> objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @NonNull
    private View initView(int pos, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.custom_spinner_activity_level, parent, false);
        }

        ImageView activityLevelImage = convertView.findViewById(R.id.activityLevelImage);
        TextView activityLevel = convertView.findViewById(R.id.activityLevel);

        UserProfile userProfile = getItem(pos);

        return convertView;
    }
}
