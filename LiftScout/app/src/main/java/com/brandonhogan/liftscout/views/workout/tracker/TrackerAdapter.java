package com.brandonhogan.liftscout.views.workout.tracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Measurements;

import java.util.ArrayList;
import java.util.List;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.TrackerViewHolder> {

    private static final int NO_POSITION_DEFAULT = 9999;

    LayoutInflater inflater;
    List<TrackerListModel> modelList;
    private int selected_position = NO_POSITION_DEFAULT;

    public TrackerAdapter(Context context, List<TrackerListModel> list) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }

    @Override
    public TrackerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_tracker_list_item, parent, false);
        return new TrackerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackerViewHolder holder, int position) {
        holder.bindData(modelList.get(position));

        if(selected_position == position){
            holder.displayLayout.setAlpha(0.3f);
        }else{
            holder.displayLayout.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class TrackerViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout displayLayout;
        private LinearLayout repLayout;
        private TextView reps;
        private TextView weight;
        private TextView noRep;
        private TextView measurement;
        private ImageView recordImage;
        private View divider;
        int id;

        public TrackerViewHolder(View itemView) {
            super(itemView);

            this.displayLayout = (RelativeLayout) itemView.findViewById(R.id.display_layout);
            this.repLayout = (LinearLayout) itemView.findViewById(R.id.rep_layout);
            this.reps = (TextView) itemView.findViewById(R.id.item_reps);
            this.weight = (TextView) itemView.findViewById(R.id.item_weight);
            this.measurement = (TextView) itemView.findViewById(R.id.item_measurement);
            this.noRep = (TextView) itemView.findViewById(R.id.no_rep);
            this.recordImage = (ImageView) itemView.findViewById(R.id.record_image);
            this.divider = (View) itemView.findViewById(R.id.divider);
        }

        public void bindData(TrackerListModel rowModel) {
            repLayout.setVisibility(View.VISIBLE);
            noRep.setVisibility(View.GONE);

            //set the text for the reps
            reps.setText(Integer.toString(rowModel.getCount()));
            //set the text for the description or hide
            weight.setText(Double.toString(rowModel.getWeight()));
            measurement.setText(Measurements.getCompressedType(rowModel.getWeightMetric(), rowModel.getWeight() > 1));

            recordImage.setVisibility(rowModel.isRecord() ? View.VISIBLE : View.GONE);

            divider.setVisibility(rowModel.isLastItem() ? View.GONE : View.VISIBLE);
        }
    }

    public void setList(List<TrackerListModel> list) {
        modelList.clear();
        modelList.addAll(list);
        notifyDataSetChanged();
    }

    public void selected(int position) {
        notifyItemChanged(selected_position);
        selected_position = position;
        notifyItemChanged(selected_position);
    }

    public void clearSelected() {
        notifyItemChanged(selected_position);
        selected_position = NO_POSITION_DEFAULT;
        notifyItemChanged(selected_position);
    }
}