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

import com.example.myfoodchoice.ModelSignUp.BusinessProfile;
import com.example.myfoodchoice.R;

import java.util.ArrayList;
import java.util.Objects;

public class BusinessRoleAdapter extends ArrayAdapter<BusinessProfile>
{
    public BusinessRoleAdapter(Context context, ArrayList<BusinessProfile> objects)
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
                    (R.layout.custom_spinner_business_role, parent, false);
        }

        ImageView businessImage = convertView.findViewById(R.id.businessImage);
        TextView businessText = convertView.findViewById(R.id.businessText);

        BusinessProfile businessProfile = getItem(pos);

        businessImage.setImageResource(Objects.requireNonNull(businessProfile).getBusinessImage());
        businessText.setText(businessProfile.getRole());
        return convertView;
    }
}
