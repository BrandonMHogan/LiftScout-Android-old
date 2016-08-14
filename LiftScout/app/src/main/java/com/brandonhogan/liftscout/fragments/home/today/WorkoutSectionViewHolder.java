package com.brandonhogan.liftscout.fragments.home.today;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.brandonhogan.liftscout.R;

public class WorkoutSectionViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final ImageView expandArrow;
    private TextView workoutName;
    private TextView workoutVolume;

    public WorkoutSectionViewHolder(View itemView) {
        super(itemView);
        workoutName = (TextView) itemView.findViewById(R.id.workout_name);
        workoutVolume = (TextView) itemView.findViewById(R.id.workout_volume);

        expandArrow = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);
    }

    public void bind(WorkoutSection section) {
        workoutName.setText(section.getName());
        workoutVolume.setText(Double.toString(section.getVolume()));
    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                expandArrow.setRotation(ROTATED_POSITION);
            } else {
                expandArrow.setRotation(INITIAL_POSITION);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            expandArrow.startAnimation(rotateAnimation);
        }
    }
}
