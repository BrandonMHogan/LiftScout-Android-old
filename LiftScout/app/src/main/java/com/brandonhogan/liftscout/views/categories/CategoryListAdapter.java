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
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<CategoryListModel> modelList;
    private RecyclerViewClickListener listener;


    @SuppressWarnings("WeakerAccess")
    public CategoryListAdapter(Context context, List<CategoryListModel> list, RecyclerViewClickListener listener) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
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

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.color)
        ImageView color;

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