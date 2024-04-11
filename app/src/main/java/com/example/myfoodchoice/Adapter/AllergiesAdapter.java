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

import com.example.myfoodchoice.ModelAdapter.Allergies;
import com.example.myfoodchoice.R;

import java.util.ArrayList;
import java.util.Objects;

public class AllergiesAdapter extends ArrayAdapter<Allergies>
{
    // fixme: remove this we will use radio button.

    public AllergiesAdapter(Context context, ArrayList<Allergies> objects)
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
                    (R.layout.custom_spinner_allergies, parent, false);
        }

        ImageView allergiesImage = convertView.findViewById(R.id.allergiesImage);
        TextView allergiesText = convertView.findViewById(R.id.allergiesText);

        Allergies allergies = getItem(pos);

        allergiesImage.setImageResource(Objects.requireNonNull(allergies).getImage());
        allergiesText.setText(allergies.getName());

        return convertView;
    }
}
