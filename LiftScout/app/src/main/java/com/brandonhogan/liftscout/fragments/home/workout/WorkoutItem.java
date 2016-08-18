package com.brandonhogan.liftscout.fragments.home.workout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.mikepenz.fastadapter.items.AbstractItem;

public class WorkoutItem extends AbstractItem<WorkoutItem, WorkoutItem.ViewHolder> {

    public int setId;
    public int reps;
    public double weight;

    public WorkoutItem(int setId, int reps, double weight) {
        this.setId = setId;
        this.reps = reps;
        this.weight = weight;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_today_item_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.workout_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        //bind our data
        //set the text for the reps
        viewHolder.reps.setText(Integer.toString(reps));
        //set the text for the description or hide
        viewHolder.weight.setText(Double.toString(weight));
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView reps;
        protected TextView weight;

        public ViewHolder(View view) {
            super(view);
            this.reps = (TextView) view.findViewById(R.id.item_reps);
            this.weight = (TextView) view.findViewById(R.id.item_weight);
        }
    }
}