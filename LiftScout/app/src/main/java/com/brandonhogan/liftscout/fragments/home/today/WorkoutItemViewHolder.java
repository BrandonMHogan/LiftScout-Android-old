package com.brandonhogan.liftscout.fragments.home.today;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.brandonhogan.liftscout.R;

public class WorkoutItemViewHolder extends ChildViewHolder {

    private TextView mIngredientTextView;

    public WorkoutItemViewHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);
    }

    public void bind(WorkoutItem ingredient) {
        mIngredientTextView.setText(ingredient.getName());
    }
}
