package com.brandonhogan.liftscout.adapters;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<ExerciseListModel> fullList;
    private List<ExerciseListModel> modelList;
    private List<ExerciseListModel> filteredList;

    private RecyclerViewClickListener listener;

    @SuppressWarnings("WeakerAccess")
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_exercise_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    private void setAdapterList(List<ExerciseListModel> list, String text) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ExerciseDiffCallback(modelList, list));
        diffResult.dispatchUpdatesTo(this);

        modelList.clear();
        for(ExerciseListModel model : list)
            modelList.add(model);


        listener.onListUpdated(modelList.isEmpty(), text.equals(""));
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

        setAdapterList(filteredList, text);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.name)
        TextView name;

        protected ViewHolder(View itemView) {
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
            int id = filteredList.get(this.getLayoutPosition()).getId();

            for (ExerciseListModel exercise : fullList) {
                if (exercise.getId() == id)
                    listener.onLongClick(view,fullList.indexOf(exercise));
            }
            return false;
        }

        @SuppressWarnings("WeakerAccess")
        public void bindData(ExerciseListModel rowModel) {
            name.setText(rowModel.getName());
        }
    }
}