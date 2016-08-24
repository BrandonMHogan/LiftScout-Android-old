package com.brandonhogan.liftscout.views.workout.tracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;

import java.util.ArrayList;
import java.util.List;

public class WorkoutTrackerAdapter extends RecyclerView.Adapter<WorkoutTrackerAdapter.TrackerViewHolder> {
    LayoutInflater inflater;
    List<TrackerListModel> modelList;

    public WorkoutTrackerAdapter(Context context, List<TrackerListModel> list) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }

    @Override
    public TrackerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_workout_tracker_list_item, parent, false);
        return new TrackerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackerViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class TrackerViewHolder extends RecyclerView.ViewHolder {

        TextView rowNum;
        TextView weight;
        TextView weightMetric;
        TextView count;
        TextView countMetric;

        public TrackerViewHolder(View itemView) {
            super(itemView);
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
}