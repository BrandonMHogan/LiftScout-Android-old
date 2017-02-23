package com.brandonhogan.liftscout.views.workout.history;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Measurements;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryListSection extends AbstractItem<HistoryListSection, HistoryListSection.ViewHolder> implements IExpandable<HistoryListSection, IItem> {
    //the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public int setId;
    public String name;
    public Date date;
    public double volume;
    public int exerciseId;
    public int setCount;
    public String measurement;

    private List<IItem> mSubItems;
    private boolean mExpanded = false;
    private boolean isEmpty;

    private FastAdapter.OnClickListener<HistoryListSection> mOnClickListener;

    public HistoryListSection(int setId, Date date, String name, int exerciseId, double volume, int setCount, String measurement, boolean isEmpty) {
        this.setId = setId;
        this.name = name;
        this.date = date;
        this.exerciseId = exerciseId;
        this.volume = volume;
        this.setCount = setCount;
        this.measurement = measurement;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public HistoryListSection withIsExpanded(boolean expanded) {
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

    public HistoryListSection withSubItems(List<IItem> subItems) {
        this.mSubItems = subItems;
        return this;
    }

    public FastAdapter.OnClickListener<HistoryListSection> getOnClickListener() {
        return mOnClickListener;
    }

    public HistoryListSection withOnClickListener(FastAdapter.OnClickListener<HistoryListSection> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

    //we define a clickListener in here so we can directly animate
    final private FastAdapter.OnClickListener<HistoryListSection> onClickListener = new FastAdapter.OnClickListener<HistoryListSection>() {
        @Override
        public boolean onClick(View v, IAdapter adapter, HistoryListSection item, int position) {
            if (item.getSubItems() != null) {
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
    public FastAdapter.OnClickListener<HistoryListSection> getOnItemClickListener() {
        return onClickListener;
    }



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
        return R.id.fastadapter_expandable_item_id;
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
        viewHolder.date = date;
        viewHolder.exerciseId = exerciseId;

        String count = Integer.toString(setCount);
        count += " " + (setCount == 1 ? ctx.getResources().getString(R.string.set) : ctx.getResources().getString(R.string.sets));

        viewHolder.setCount.setText(count);

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
        private SweetAlertDialog dialog;
        private Date date;
        private int exerciseId;

        @Bind(R.id.workout_name)
        TextView name;

        @Bind(R.id.workout_set_count)
        TextView setCount;

        @Bind(R.id.edit_section_icon)
        ImageView editIcon;

        @Bind(R.id.arrow_expand_imageview)
        ImageView expandIcon;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
            expandIcon.setVisibility(View.GONE);
        }

        @OnClick(R.id.edit_section_icon)
        void onEditClick() {
            dialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText(view.getContext().getString(R.string.dialog_edit_history_title))
                    .setContentText(view.getContext().getString(R.string.dialog_edit_history_message, name.getText()))
                    .setConfirmText(view.getContext().getString(R.string.edit))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.cancel();
                            EventBus.getDefault().post(new HistoryTrackerEvent(1, date, exerciseId));
                        }
                    })
                    .setCancelText(view.getContext().getString(R.string.cancel))
                    .showCancelButton(true);

            dialog.show();
        }
    }
}