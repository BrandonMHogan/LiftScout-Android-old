package com.brandonhogan.liftscout.fragments.home.workout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.brandonhogan.liftscout.R;

import java.util.ArrayList;

public class TodaySectionedExpandableGridAdapter extends RecyclerView.Adapter<TodaySectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private final TodayItemClickListener mItemClickListener;
    private final TodaySectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.frag_today_list_section;
    private static final int VIEW_TYPE_ITEM = R.layout.frag_today_list_item; //TODO : change this

    public TodaySectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager, TodayItemClickListener itemClickListener,
                                          TodaySectionStateChangeListener sectionStateChangeListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position)?gridLayoutManager.getSpanCount():1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof TodaySection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM :
                final TodayItem item = (TodayItem) mDataArrayList.get(position);

                holder.itemReps.setText(Integer.toString(item.getReps()));
                holder.itemWeight.setText(Double.toString(item.getWeight()));

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.todayItemClicked(item);
                    }
                });
                break;
            case VIEW_TYPE_SECTION :
                final TodaySection section = (TodaySection) mDataArrayList.get(position);

                holder.sectionName.setText(section.getName());
                holder.sectionVolume.setText(Double.toString(section.getVolume()));

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.todayItemClicked(section);
                        holder.flipIcon();
                     //   mSectionStateChangeListener.onTodaySectionStateChanged(section, !section.isExpanded);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;
        int iconAngle = 0;

        // Section
        TextView sectionName;
        TextView sectionVolume;
        ImageView icon;

        // Item
        TextView itemReps;
        TextView itemWeight;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                itemReps = (TextView) view.findViewById(R.id.item_reps);
                itemWeight = (TextView) view.findViewById(R.id.item_weight);
            } else {
                sectionName = (TextView) view.findViewById(R.id.section_name);
                sectionVolume = (TextView) view.findViewById(R.id.section_volume);
                icon = (ImageView) view.findViewById(R.id.expand_icon);
            }
        }

        public void flipIcon() {
            if (iconAngle == 0)
                iconAngle = 180;
            else
                iconAngle = 0;

            icon.animate().rotation(iconAngle).setDuration(500).start();
        }
    }
}