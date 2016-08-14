package com.brandonhogan.liftscout.fragments.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.accordionview.AccordionView;
import com.brandonhogan.liftscout.R;

import java.util.ArrayList;
import java.util.List;

public class TodayListAdapter {
//        extends RecyclerView.Adapter<TodayListAdapter.TodayViewHolder> {
//    LayoutInflater inflater;
//    List<TodayListModel> modelList;
//
//    public TodayListAdapter(Context context, List<TodayListModel> list) {
//        super();
//        inflater = LayoutInflater.from(context);
//        modelList = new ArrayList<>(list);
//    }
//
//    @Override
//    public TodayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.frag_today_list_item, parent, false);
//        return new TodayViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(TodayViewHolder holder, int position) {
//        holder.bindData(modelList.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return modelList.size();
//    }
//
//    class TodayViewHolder extends RecyclerView.ViewHolder {
//
//        AccordionView accordionView;
//        TextView reps;
//        TextView weight;
//
//        public TodayViewHolder(View itemView) {
//            super(itemView);
//
//            accordionView = (AccordionView) itemView.findViewById(R.id.accordion);
//            View content = inflater.inflate(R.layout.frag_today_list_section, accordionView, false);
//
//            reps = (TextView) content.findViewById(R.id.reps);
//            weight = (TextView) itemView.findViewById(R.id.weight);
//        }
//
//        public void bindData(TodayListModel rowModel) {
//            accordionView.setTitle(rowModel.getSet().getExercise().getName());
//
//            reps.setText(rowModel.getSet().getReps().first().getCount());
//            weight.setText(Double.toString(rowModel.getSet().getReps().first().getWeight()));
//        }
//    }
//
//    public void setList(List<TodayListModel> list) {
//        modelList.clear();
//        modelList.addAll(list);
//        notifyDataSetChanged();
//    }
}