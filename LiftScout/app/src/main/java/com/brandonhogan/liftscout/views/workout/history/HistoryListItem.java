package com.brandonhogan.liftscout.views.workout.history;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Measurements;
import com.mikepenz.fastadapter.items.AbstractItem;

public class HistoryListItem extends AbstractItem<HistoryListItem, HistoryListItem.ViewHolder> {

    public int setId;
    public int exerciseId;
    public int reps;
    public double weight;
    public String measurement;
    private boolean mIsDraggable = false;
    private boolean isEmpty = false;
    private String emptyMsg;

    public HistoryListItem(int setId, int exerciseId, int reps, double weight, String measurement) {
        this.setId = setId;
        this.exerciseId = exerciseId;
        this.reps = reps;
        this.weight = weight;
        this.measurement = measurement;
    }

    public HistoryListItem(int setId, int exerciseId, boolean isEmpty, String emptyMessage) {
        this.setId = setId;
        this.exerciseId = exerciseId;
        this.isEmpty = isEmpty;
        this.emptyMsg = emptyMessage;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_item_id;
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

        if (isEmpty) {
            viewHolder.repLayout.setVisibility(View.GONE);
            viewHolder.noRep.setVisibility(View.VISIBLE);

            viewHolder.noRep.setText(emptyMsg);
        }
        else {
            viewHolder.repLayout.setVisibility(View.VISIBLE);
            viewHolder.noRep.setVisibility(View.GONE);

            //set the text for the reps
            viewHolder.reps.setText(Integer.toString(reps));
            //set the text for the description or hide
            viewHolder.weight.setText(Double.toString(weight));
            viewHolder.measurement.setText(Measurements.getCompressedType(measurement, weight > 1));
        }
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout repLayout;
        protected TextView reps;
        protected TextView weight;
        protected TextView noRep;
        protected TextView measurement;

        public ViewHolder(View view) {
            super(view);
            this.repLayout = (LinearLayout) view.findViewById(R.id.rep_layout);
            this.reps = (TextView) view.findViewById(R.id.item_reps);
            this.weight = (TextView) view.findViewById(R.id.item_weight);
            this.measurement = (TextView) view.findViewById(R.id.item_measurement);
            this.noRep = (TextView) view.findViewById(R.id.no_rep);
        }
    }
}