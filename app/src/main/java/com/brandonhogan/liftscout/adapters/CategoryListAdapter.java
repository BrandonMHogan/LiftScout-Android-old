package com.brandonhogan.liftscout.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.models.CategoryListModel;
import com.brandonhogan.liftscout.utils.DiffUtil.CategoryDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<CategoryListModel> fullList;
    private List<CategoryListModel> modelList;
    private List<CategoryListModel> filteredList;
    private RecyclerViewClickListener listener;

    @SuppressWarnings("WeakerAccess")
    public CategoryListAdapter(Context context, List<CategoryListModel> list, RecyclerViewClickListener listener) {
        inflater = LayoutInflater.from(context);

        fullList = new ArrayList<>();
        modelList = new ArrayList<>();
        filteredList = new ArrayList<>();

        for (CategoryListModel model : list) {
            fullList.add(model);
            modelList.add(model);
            filteredList.add(model);
        }

        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    public void setAdapterList(List<CategoryListModel> list) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CategoryDiffCallback(modelList, list));
        diffResult.dispatchUpdatesTo(this);

        modelList.clear();
        for(CategoryListModel model : list)
            modelList.add(model);
    }

    //call when you want to filter
    public void filterList(String text) {

        filteredList.clear();
        //here you need to add proper items do filteredContactList
        for (final CategoryListModel item : fullList) {
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

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.color)
        ImageView color;

        @Bind(R.id.name)
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

            for (CategoryListModel exercise : fullList) {
                if (exercise.getId() == id)
                    listener.onClick(view,fullList.indexOf(exercise));
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int id = filteredList.get(this.getLayoutPosition()).getId();

            for (CategoryListModel exercise : fullList) {
                if (exercise.getId() == id)
                    listener.onLongClick(view,fullList.indexOf(exercise));
            }
            return false;
        }

        public void bindData(CategoryListModel rowModel) {
            name.setText(rowModel.getName());

            if(rowModel.getColor() == 0) {
                color.setVisibility(View.GONE);
            }
            else {
                color.setVisibility(View.VISIBLE);
                ((GradientDrawable) color.getBackground()).setColor(rowModel.getColor());
            }
        }
    }
}