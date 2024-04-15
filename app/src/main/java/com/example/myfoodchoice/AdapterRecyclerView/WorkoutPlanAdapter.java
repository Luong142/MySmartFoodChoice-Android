package com.example.myfoodchoice.AdapterRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodchoice.AdapterInterfaceListener.OnWorkoutPlanClickListener;
import com.example.myfoodchoice.ModelBusiness.WorkoutPlan;
import com.example.myfoodchoice.R;

import java.util.ArrayList;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.myViewHolder>
{
    private final ArrayList<WorkoutPlan> workoutPlans;
    private final OnWorkoutPlanClickListener onWorkoutPlanClickListener;

    public WorkoutPlanAdapter(ArrayList<WorkoutPlan> workoutPlans, OnWorkoutPlanClickListener onWorkoutPlanClickListener)
    {
        this.workoutPlans = workoutPlans;
        this.onWorkoutPlanClickListener = onWorkoutPlanClickListener;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        public TextView workoutPlanDuration;
        public TextView workoutPlanName;
        public TextView workoutPlanDesc;

        public myViewHolder(final View itemView, OnWorkoutPlanClickListener onWorkoutPlanClickListener)
        {
            super(itemView);
            workoutPlanName = itemView.findViewById(R.id.workoutNameTextView);
            workoutPlanDuration = itemView.findViewById(R.id.workoutDurationTextView);
            workoutPlanDesc = itemView.findViewById(R.id.workoutDescriptionTextView);

            itemView.setOnClickListener(v ->
            {
                if (onWorkoutPlanClickListener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        onWorkoutPlanClickListener.onWorkoutPlanClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.work_out_plan_user_layout,
                parent, false);
        return new myViewHolder(itemView, onWorkoutPlanClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position)
    {
        WorkoutPlan workoutPlan = workoutPlans.get(position);
        String name = workoutPlan.getName();
        String desc = workoutPlan.getDescription();
        int duration = workoutPlan.getDuration();

        holder.workoutPlanName.setText(name);
        holder.workoutPlanDesc.setText(desc);
        holder.workoutPlanDuration.setText(duration + " min"); // FIXME: set new string resource?
    }

    @Override
    public int getItemCount()
    {
        return workoutPlans.size();
    }
}