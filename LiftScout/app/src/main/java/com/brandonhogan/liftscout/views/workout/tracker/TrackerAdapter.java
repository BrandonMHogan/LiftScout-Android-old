package com.brandonhogan.liftscout.views.workout.tracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;

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
            holder.displayLayout.setAlpha(0.5f);
        }else{
            holder.displayLayout.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class TrackerViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout displayLayout;
        TextView rowNum;
        TextView weight;
        TextView weightMetric;
        TextView count;
        TextView countMetric;

        public TrackerViewHolder(View itemView) {
            super(itemView);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.display_layout);
            rowNum = (TextView) itemView.findViewById(R.id.row_num);
            weight = (TextView) itemView.findViewById(R.id.weight);
            weightMetric = (TextView) itemView.findViewById(R.id.weight_metric);
            count = (TextView) itemView.findViewById(R.id.count);
            countMetric = (TextView) itemView.findViewById(R.id.count_metric);
        }

        public void bindData(TrackerListModel rowModel) {
            rowNum.setText(Integer.toString(rowModel.getRowNum()));
            weight.setText(Double.toString(rowModel.getWeight()));
            weightMetric.setText(rowModel.getWeightMetric());
            count.setText(Integer.toString(rowModel.getCount()));
            countMetric.setText(rowModel.getCountMetric());

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