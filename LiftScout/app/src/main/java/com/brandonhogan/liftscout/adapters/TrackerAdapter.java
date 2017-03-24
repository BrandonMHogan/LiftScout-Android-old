package com.brandonhogan.liftscout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.utils.constants.Measurements;
import com.brandonhogan.liftscout.interfaces.RecyclerViewClickListener;
import com.brandonhogan.liftscout.models.TrackerListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.ViewHolder> {

    private static final int NO_POSITION_DEFAULT = 9999;

    private LayoutInflater inflater;
    private List<TrackerListModel> modelList;
    private int selected_position = NO_POSITION_DEFAULT;
    private RecyclerViewClickListener listener;

    public TrackerAdapter(Context context, List<TrackerListModel> list, RecyclerViewClickListener listener) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.frag_tracker_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(modelList.get(position));

        if(selected_position == position){
            holder.displayLayout.setAlpha(0.3f);
        }else{
            holder.displayLayout.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.display_layout)
        RelativeLayout displayLayout;

        @Bind(R.id.rep_layout)
        LinearLayout repLayout;

        @Bind(R.id.item_reps)
        TextView reps;

        @Bind(R.id.item_weight)
        TextView weight;


        @Bind(R.id.no_rep)
        TextView noRep;

        @Bind(R.id.item_measurement)
        TextView measurement;


        @Bind(R.id.record_image)
        ImageView recordImage;

        @Bind(R.id.divider)
        View divider;


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

        public void bindData(TrackerListModel rowModel) {
            repLayout.setVisibility(View.VISIBLE);
            noRep.setVisibility(View.GONE);

            //set the text for the reps
            reps.setText(Integer.toString(rowModel.getCount()));
            //set the text for the description or hide
            weight.setText(Double.toString(rowModel.getWeight()));
            measurement.setText(Measurements.getCompressedType(rowModel.getWeightMetric(), rowModel.getWeight() > 1));

            recordImage.setVisibility(rowModel.isRecord() ? View.VISIBLE : View.GONE);

            divider.setVisibility(rowModel.isLastItem() ? View.GONE : View.VISIBLE);
        }
    }

    public void setList(List<TrackerListModel> list) {
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