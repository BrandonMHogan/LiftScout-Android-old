package com.brandonhogan.liftscout.views.categories;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    LayoutInflater inflater;
    List<CategoryListModel> modelList;

    public CategoryListAdapter(Context context, List<CategoryListModel> list) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_category_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView color;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            color = (ImageView) itemView.findViewById(R.id.color);
        }

        public void bindData(CategoryListModel rowModel) {
            name.setText(rowModel.getName());
            ((GradientDrawable)color.getBackground()).setColor(rowModel.getColor());
        }
    }

    public void setList(List<CategoryListModel> list) {
        modelList.clear();
        modelList.addAll(list);
        notifyDataSetChanged();
    }
}