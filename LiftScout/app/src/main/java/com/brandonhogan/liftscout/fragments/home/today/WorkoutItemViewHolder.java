package com.brandonhogan.liftscout.fragments.home.today;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.brandonhogan.liftscout.R;

public class WorkoutItemViewHolder extends ChildViewHolder {

    private TextView mReps;
    private TextView mWeight;

    public WorkoutItemViewHolder(View itemView) {
        super(itemView);
        mReps = (TextView) itemView.findViewById(R.id.item_reps);
        mWeight = (TextView) itemView.findViewById(R.id.item_weight);
    }

    public void bind(WorkoutItem item) {
        mReps.setText(Integer.toString(item.getmReps()));
        mWeight.setText(Double.toString(item.getmWeight()));
    }
}
