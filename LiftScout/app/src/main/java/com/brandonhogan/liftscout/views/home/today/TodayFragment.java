package com.brandonhogan.liftscout.views.home.today;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class TodayFragment extends BaseFragment implements TodayContact.View {


    // Instance
    //
    public static TodayFragment newInstance(Date date)
    {
        TodayFragment frag = new TodayFragment();
        Bundle bundle = new Bundle();

        bundle.putLong(DATE_BUNDLE, date.getTime());
        frag.setArguments(bundle);

        return frag;
    }


    // Static Properties
    //
    private static final String DATE_BUNDLE = "dateBundle";


    // Private Properties
    //
    private TodayPresenter presenter;
    private TextView weightView;
    private LinearLayout weightLayout;
    private FastItemAdapter mAdapter;


    // Binds
    //
    @Bind(R.id.title)
    TextView titleView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.workout_no_data)
    LinearLayout noDataLayout;
    

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_today, container, false);

        weightView = (TextView) view.findViewById(R.id.weightView);
        weightLayout = (LinearLayout) view.findViewById(R.id.weight_layout);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new TodayPresenter(this, getArguments().getLong(DATE_BUNDLE));
        presenter.viewCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Public Functions
    //
    public void update() {
        presenter.update();
    }

    public void showDeleteRepAlert(final TodayListSection section, final int position) {

        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_tracker_delete_rep_title)
                .content(R.string.dialog_tracker_delete_rep_message)
                .neutralText(R.string.delete)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.onDeleteSection(section, position);
                    }
                })
                .positiveText(R.string.cancel).show();
    }

    // Contracts
    //


    @Override
    public void setupTitle(int titleRes, String titleDate) {
        if (titleRes != 0)
            titleView.setText(getString(titleRes));
        else
            titleView.setText(titleDate);
    }

    @Override
    public void setupWeight(String weight) {
        if (weight == null)
            weightLayout.setVisibility(View.GONE);
        else {
            weightLayout.setVisibility(View.VISIBLE);
            weightView.setText(weight);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setupAdapter(List<TodayListSection> data, int expandPosition) {

        mAdapter = new FastItemAdapter<>();
        mAdapter.withSelectable(true);
        mAdapter.add(data);

        mAdapter.withOnClickListener(new FastAdapter.OnClickListener<TodayListItem>() {
            @Override
            public boolean onClick(View v, IAdapter<TodayListItem> adapter, TodayListItem item, int position) {
                getNavigationManager().startWorkoutContainer(item.exerciseId, false);
                return true;
            }
        });

        mAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                // Listener will trigger for both the section and the item, and was crashing if you
                // long clicked a child item.
                try {
                    showDeleteRepAlert(((TodayListSection) mAdapter.getAdapterItem(position)), position);
                }
                catch (Exception ex) {

                }
                return false;
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter.expand(expandPosition);

        checkIfEmpty();
    }

    @Override
    public void onSetDeleted(int position, int count) {
        mAdapter.removeItemRange(position, count);
        checkIfEmpty();
    }

    @Override
    public String getEmptySetMessage() {
        return getString(R.string.frag_today_empty_set_msg);
    }

    // Private Functions
    //

    private void checkIfEmpty() {
        if (mAdapter.getAdapterItemCount() < 1 )
            noDataLayout.setVisibility(View.VISIBLE);
        else
            noDataLayout.setVisibility(View.GONE);
    }
}