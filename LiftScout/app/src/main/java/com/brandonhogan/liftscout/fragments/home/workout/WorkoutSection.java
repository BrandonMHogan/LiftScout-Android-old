package com.brandonhogan.liftscout.fragments.home.workout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutSection extends AbstractItem<WorkoutSection, WorkoutSection.ViewHolder> implements IExpandable<WorkoutSection, IItem> {
    //the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public String name;
    public double volume;

    private List<IItem> mSubItems;
    private boolean mExpanded = false;

    private FastAdapter.OnClickListener<WorkoutSection> mOnClickListener;

    public WorkoutSection(String name, double volume) {
        this.name = name;
        this.volume = volume;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public WorkoutSection withIsExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    @Override
    public List<IItem> getSubItems() {
        return mSubItems;
    }

    @Override
    public boolean isAutoExpanding() {
        return true;
    }

    public WorkoutSection withSubItems(List<IItem> subItems) {
        this.mSubItems = subItems;
        return this;
    }

    public FastAdapter.OnClickListener<WorkoutSection> getOnClickListener() {
        return mOnClickListener;
    }

    public WorkoutSection withOnClickListener(FastAdapter.OnClickListener<WorkoutSection> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

    //we define a clickListener in here so we can directly animate
    final private FastAdapter.OnClickListener<WorkoutSection> onClickListener = new FastAdapter.OnClickListener<WorkoutSection>() {
        @Override
        public boolean onClick(View v, IAdapter adapter, WorkoutSection item, int position) {
            if (item.getSubItems() != null) {
                if (!item.isExpanded()) {
                    ViewCompat.animate(v.findViewById(R.id.arrow_expand_imageview)).rotation(180).start();
                } else {
                    ViewCompat.animate(v.findViewById(R.id.arrow_expand_imageview)).rotation(0).start();
                }
                return mOnClickListener != null ? mOnClickListener.onClick(v, adapter, item, position) : true;
            }
            return mOnClickListener != null ? mOnClickListener.onClick(v, adapter, item, position) : false;
        }
    };

    /**
     * we overwrite the item specific click listener so we can automatically animate within the item
     *
     * @return
     */
    @Override
    public FastAdapter.OnClickListener<WorkoutSection> getOnItemClickListener() {
        return onClickListener;
    }

    @Override
    public boolean isSelectable() {
        //this might not be true for your application
        return getSubItems() == null;
    }

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    @Override
    public int getType() {
        return R.id.fastadapter_expandable_today_item_id;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.workout_section;
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param viewHolder the viewHolder of this item
     */
    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        //get the context
        Context ctx = viewHolder.itemView.getContext();

        viewHolder.name.setText(name);
        viewHolder.volume.setText(Double.toString(volume));

        //make sure all animations are stopped
        viewHolder.icon.clearAnimation();

        if (isExpanded()) {
            ViewCompat.setRotation(viewHolder.icon, 0);
        } else {
            ViewCompat.setRotation(viewHolder.icon, 180);
        }
    }


    /**
     * our ItemFactory implementation which creates the ViewHolder for our adapter.
     * It is highly recommended to implement a ViewHolderFactory as it is 0-1ms faster for ViewHolder creation,
     * and it is also many many times more efficient if you define custom listeners on views within your item.
     */
    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    /**
     * return our ViewHolderFactory implementation here
     *
     * @return
     */
    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }


    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected final View view;

        @Bind(R.id.workout_name)
        TextView name;

        @Bind(R.id.workout_volume)
        TextView volume;

        @Bind(R.id.arrow_expand_imageview)
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}