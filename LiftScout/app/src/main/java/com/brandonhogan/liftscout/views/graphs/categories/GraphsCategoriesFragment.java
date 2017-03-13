package com.brandonhogan.liftscout.views.graphs.categories;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.CategoryGraph;
import com.brandonhogan.liftscout.core.utils.AttrUtil;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class GraphsCategoriesFragment extends BaseFragment implements GraphsCategoriesContract.View,
        RecyclerTouchListener.RecyclerTouchListenerHelper {


    // Bindings
    //
    @Bind(R.id.display_layout)
    LinearLayout displayLayout;

    @Bind(R.id.no_data)
    TextView noDataTextView;

    @Bind(R.id.pie_chart)
    PieChart pieChart;

    @Bind(R.id.graph_type)
    MaterialSpinner graphType;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.total)
    TextView totalTextView;

    @Bind(R.id.total_date_range)
    TextView totalDateRangeTextView;


    // Instance
    //
    public static GraphsCategoriesFragment newInstance()
    {
        GraphsCategoriesFragment frag = new GraphsCategoriesFragment();
        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private GraphsCategoriesContract.Presenter presenter;
    private GraphsCategoryAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;
    private int currentSelected = 999;
    private OnChartValueSelectedListener pieChartListener;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_graphs_categories, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new GraphsCategoriesPresenter(this);

        setupGraph();
        presenter.viewCreated();
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    private void setupGraph() {
        int fontColor = AttrUtil.getAttributeRes(getActivity().getTheme(),android.R.attr.textColorPrimary);

        pieChart.setUsePercentValues(true);
        pieChart.animate();
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setTouchEnabled(true);
        //pieChart.setNoDataText(getResources().getString(R.string.charts_empty_exercise));
        pieChart.setNoDataTextColor(fontColor);

        // The label which shows the category or exercise
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(getResources().getInteger(R.integer.graph_pie_font_size));

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        //pieChart.setCenterTextTypeface(mTfLight);
        pieChart.setCenterTextSize(getResources().getInteger(R.integer.graph_pie_center_font_size));
        pieChart.setCenterTextColor(fontColor);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(getResources().getInteger(R.integer.graph_pie_radius_hole));
        pieChart.setTransparentCircleRadius(getResources().getInteger(R.integer.graph_pie_radius_hole_transparent));

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChartListener = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                itemSelected(h);
            }

            @Override
            public void onNothingSelected() {
                itemSelected(currentSelected);
            }
        };

        pieChart.setOnChartValueSelectedListener(pieChartListener);
    }

    private void updateAdapter(ArrayList<CategoryGraph> data) {

        if (mAdapter == null) {

            mAdapter = new GraphsCategoryAdapter(getActivity(), data);
            recyclerView.setAdapter(mAdapter);

            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);

            onTouchListener = new RecyclerTouchListener(getActivity(), recyclerView);

            onTouchListener
                    .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                        @Override
                        public void onRowClicked(int position) {
                            itemSelected(position);
                        }

                        @Override
                        public void onIndependentViewClicked(int independentViewID, int position) {
                        }
                    });

            onTouchListener.setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
                @Override
                public void onRowLongClicked(int position) {
                   // presenter.onSelect(position);
                    mAdapter.selected(position);
                    itemSelected(position);

                }
            });


        }
        else {
            mAdapter.setList(data);
        }
    }

    private void itemSelected(Highlight h) {
        itemSelected((int)h.getX());
    }

    private void itemSelected(int position) {
        pieChart.setOnChartValueSelectedListener(null);

        if (currentSelected == position) {
            currentSelected = 999;
            mAdapter.clearSelected();
            pieChart.highlightValue(0, -1);
        }
        else {
            currentSelected = position;
            pieChart.highlightValue(position, 0);
            mAdapter.selected(position);
            linearLayoutManager.scrollToPosition(position);
        }

        pieChart.setOnChartValueSelectedListener(pieChartListener);
    }

    private void updateTotalLabels(int total, long startDate, long endDate) {
        totalTextView.setText(Integer.toString(total));

        if (startDate > 0 && endDate > 0)
            totalDateRangeTextView.setText(BhDate.toSimpleDateRange(startDate, endDate));
    }

    private void updateVisibilities(boolean isData) {
        noDataTextView.setVisibility(isData ? View.GONE : View.VISIBLE);
        displayLayout.setVisibility(isData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void populateGraphType(ArrayList<Integer> values, int position) {
        ArrayList<String> types = new ArrayList<>();

        for (Integer type : values) {
            types.add(getString(type));
        }

        graphType.setItems(types);
        graphType.setSelectedIndex(position);
        graphType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onGraphTypeSelected(position);
            }
        });
    }

    @Override
    public void setPieChart(ArrayList<CategoryGraph> categories, int total, long startDate, long endDate) {


        if (categories == null || categories.isEmpty()) {
            updateVisibilities(false);
            return;
        }

        updateVisibilities(true);

//        pieChart.setCenterText(getString(title));

        // add a selection listener
        //pieChart.setOnChartValueSelectedListener(this);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        List<Integer> colors = new ArrayList<>();

        for (CategoryGraph category : categories) {
            entries.add(new PieEntry(category.getValue(), category.getName()));
            colors.add(category.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(getResources().getInteger(R.integer.graph_pie_font_size));
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);

        pieChart.invalidate();
        pieChart.animate();


        updateTotalLabels(total, startDate, endDate);
        updateAdapter(categories);
    }


}
