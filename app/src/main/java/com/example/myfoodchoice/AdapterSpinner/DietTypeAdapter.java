package com.example.myfoodchoice.AdapterSpinner;

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

public class DietTypeAdapter extends ArrayAdapter<UserProfile>
{
    public DietTypeAdapter(Context context, ArrayList<UserProfile> objects)
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
                    (R.layout.custom_spinner_diet_type, parent, false);
        }

        ImageView dietTypeImage = convertView.findViewById(R.id.dietTypeImage);
        TextView dietTypeText = convertView.findViewById(R.id.dietType);

        UserProfile userProfile = getItem(pos);

        dietTypeImage.setImageResource(Objects.requireNonNull(userProfile).getDietTypeImage());
        dietTypeText.setText(userProfile.getDietType());

        return convertView;
    }
}
