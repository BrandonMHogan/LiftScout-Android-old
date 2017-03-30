package com.brandonhogan.liftscout.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.models.ExerciseListModel;
import com.brandonhogan.liftscout.utils.DiffUtil.ExerciseDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {
    private LayoutInflater inflater;
    private List<ExerciseListModel> fullList;
    private List<ExerciseListModel> modelList;
    private List<ExerciseListModel> filteredList;

    private RecyclerViewClickListener listener;


    public ExerciseListAdapter(Context context, List<ExerciseListModel> list, RecyclerViewClickListener listener) {
        inflater = LayoutInflater.from(context);

        fullList = new ArrayList<>();
        modelList = new ArrayList<>();
        filteredList = new ArrayList<>();

        for (ExerciseListModel model : list) {
            fullList.add(model);
            modelList.add(model);
            filteredList.add(model);
        }

        this.listener = listener;
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

    public void setAdapterList(List<ExerciseListModel> list) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ExerciseDiffCallback(modelList, list));
        diffResult.dispatchUpdatesTo(this);

        modelList.clear();
        for(ExerciseListModel model : list)
            modelList.add(model);
    }

    //call when you want to filter
    public void filterList(String text) {

        filteredList.clear();
        //here you need to add proper items do filteredContactList
        for (final ExerciseListModel item : fullList) {
            if (item.getName().toLowerCase().trim().contains(text)) {
                filteredList.add(item);
            }
        }
        setAdapterList(filteredList);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.name)
        TextView name;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = filteredList.get(this.getLayoutPosition()).getId();

            for (ExerciseListModel exercise : fullList) {
                if (exercise.getId() == id)
                    listener.onClick(view,fullList.indexOf(exercise));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(view, filteredList.get(this.getLayoutPosition()).getId());
            return false;
        }

        public void bindData(ExerciseListModel rowModel) {
            name.setText(rowModel.getName());
        }
    }
}