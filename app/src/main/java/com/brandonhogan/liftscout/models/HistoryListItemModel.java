package com.brandonhogan.liftscout.models;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.utils.constants.Measurements;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Date;

public class HistoryListItemModel extends AbstractItem<HistoryListItemModel, HistoryListItemModel.ViewHolder> {

    public int setId;
    public int exerciseId;
    public int reps;
    public double weight;
    public Date date;
    public String measurement;
    private boolean mIsDraggable = false;
    private boolean isEmpty = false;
    private String emptyMsg;
    private boolean isRecord;
    private boolean isLastItem;

    public HistoryListItemModel(int setId, int exerciseId, Date date, int reps, double weight, String measurement, boolean isRecord, boolean isLastItem) {
        this.setId = setId;
        this.exerciseId = exerciseId;
        this.date = date;
        this.reps = reps;
        this.weight = weight;
        this.measurement = measurement;
        this.isRecord = isRecord;
        this.isLastItem = isLastItem;
    }

    public HistoryListItemModel(int setId, int exerciseId, Date date, boolean isEmpty, String emptyMessage) {
        this.setId = setId;
        this.exerciseId = exerciseId;
        this.date = date;
        this.isEmpty = isEmpty;
        this.emptyMsg = emptyMessage;
        this.isRecord = false;
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

            viewHolder.recordImage.setVisibility(isRecord ? View.VISIBLE : View.GONE);

            viewHolder.divider.setVisibility(isLastItem ? View.GONE : View.VISIBLE);
        }
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout repLayout;
        private TextView reps;
        private TextView weight;
        private TextView noRep;
        private TextView measurement;
        private ImageView recordImage;
        private View divider;

        public ViewHolder(View view) {
            super(view);
            this.repLayout = (LinearLayout) view.findViewById(R.id.rep_layout);
            this.reps = (TextView) view.findViewById(R.id.item_reps);
            this.weight = (TextView) view.findViewById(R.id.item_weight);
            this.measurement = (TextView) view.findViewById(R.id.item_measurement);
            this.noRep = (TextView) view.findViewById(R.id.no_rep);
            this.recordImage = (ImageView) view.findViewById(R.id.record_image);
            this.divider = (View) view.findViewById(R.id.divider);
        }
    }
}