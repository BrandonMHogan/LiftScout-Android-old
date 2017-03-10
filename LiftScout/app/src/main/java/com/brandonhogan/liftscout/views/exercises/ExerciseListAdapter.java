package com.brandonhogan.liftscout.views.exercises;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {
    LayoutInflater inflater;
    ArrayList<Exercise> modelList;

    public ExerciseListAdapter(Context context, ArrayList<Exercise> list) {
        super();
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_exercise_list_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bindData(Exercise rowModel) {
            name.setText(rowModel.getName());
        }
    }

    public void setList(ArrayList<Exercise> list) {
        modelList.clear();
        modelList.addAll(list);
        notifyDataSetChanged();
    }
}