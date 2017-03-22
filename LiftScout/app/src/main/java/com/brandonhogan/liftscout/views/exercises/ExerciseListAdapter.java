package com.brandonhogan.liftscout.views.exercises;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.Exercise;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Exercise> modelList;
    private RecyclerViewClickListener listener;


    public ExerciseListAdapter(Context context, List<Exercise> list, RecyclerViewClickListener listener) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_exercise_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, this.getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, this.getLayoutPosition());
            return false;
        }

        public void bindData(Exercise rowModel) {
            name.setText(rowModel.getName());
        }
    }

    public void setList(List<Exercise> list) {
        modelList.clear();
        modelList.addAll(list);
        notifyDataSetChanged();
    }
}