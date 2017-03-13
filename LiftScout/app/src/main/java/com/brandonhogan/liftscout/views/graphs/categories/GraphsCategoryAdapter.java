package com.brandonhogan.liftscout.views.graphs.categories;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.CategoryGraph;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/13/2017.
 * Description :
 */

public class GraphsCategoryAdapter extends RecyclerView.Adapter<GraphsCategoryAdapter.ViewHolder> {

        private static final int NO_POSITION_DEFAULT = 9999;

        LayoutInflater inflater;
        ArrayList<CategoryGraph> modelList;
        private int selected_position = NO_POSITION_DEFAULT;

        public GraphsCategoryAdapter(Context context, ArrayList<CategoryGraph> list) {
            inflater = LayoutInflater.from(context);
            modelList = new ArrayList<>(list);
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

        class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout displayLayout;
            ImageView color;
            TextView name;
            TextView value;

            public ViewHolder(View itemView) {
                super(itemView);
                displayLayout = (LinearLayout) itemView.findViewById(R.id.display_layout);
                color = (ImageView) itemView.findViewById(R.id.color);
                name = (TextView) itemView.findViewById(R.id.name);
                value = (TextView) itemView.findViewById(R.id.value);
            }

            public void bindData(CategoryGraph rowModel) {
                name.setText(rowModel.getName());
                value.setText(Integer.toString(rowModel.getValue()));
                ((GradientDrawable)color.getBackground()).setColor(rowModel.getColor());

            }
        }

        public void setList(ArrayList<CategoryGraph> list) {
            modelList.clear();
            modelList.addAll(list);
            notifyDataSetChanged();
        }

        public void selected(int position) {
            notifyItemChanged(selected_position);
            selected_position = position;
            notifyItemChanged(selected_position);
        }

        public void clearSelected() {
            notifyItemChanged(selected_position);
            selected_position = NO_POSITION_DEFAULT;
            notifyItemChanged(selected_position);
        }

}
