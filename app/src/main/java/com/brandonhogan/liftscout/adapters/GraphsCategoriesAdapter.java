package com.brandonhogan.liftscout.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.repository.model.CategoryGraph;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

@SuppressWarnings("WeakerAccess")
public class GraphsCategoriesAdapter extends RecyclerView.Adapter<GraphsCategoriesAdapter.ViewHolder> {

    private static final int NO_POSITION_DEFAULT = 9999;

    private LayoutInflater inflater;
    private ArrayList<CategoryGraph> modelList;
    private int selected_position = NO_POSITION_DEFAULT;
    private RecyclerViewClickListener listener;

    @SuppressWarnings("WeakerAccess")
    public GraphsCategoriesAdapter(Context context, ArrayList<CategoryGraph> list, RecyclerViewClickListener listener) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_graphs_category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.display_layout)
        LinearLayout displayLayout;

        @BindView(R.id.color)
        ImageView color;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.value)
        TextView value;


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

        private void bindData(CategoryGraph rowModel) {
            name.setText(rowModel.getName());

            String formattedValue = NumberFormat.getIntegerInstance().format(rowModel.getValue());
            value.setText(formattedValue);

            ((GradientDrawable)color.getBackground()).setColor(rowModel.getColor());

        }
    }

    public void setList(ArrayList<CategoryGraph> list) {
        modelList.clear();
        modelList.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    public void selected(int position) {
        notifyItemChanged(selected_position);
        selected_position = position;
        notifyItemChanged(selected_position);
    }

    @SuppressWarnings("WeakerAccess")
    public void clearSelected() {
        notifyItemChanged(selected_position);
        selected_position = NO_POSITION_DEFAULT;
        notifyItemChanged(selected_position);
    }
}
