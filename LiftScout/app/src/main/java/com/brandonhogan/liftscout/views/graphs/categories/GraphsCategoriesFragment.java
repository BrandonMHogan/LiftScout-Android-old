package com.brandonhogan.liftscout.views.graphs.categories;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.model.CategoryGraph;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.jaredrummler.materialspinner.MaterialSpinner;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Brandon on 2/16/2017.
 * Description :
 */

public class GraphsCategoriesFragment extends BaseFragment implements GraphsCategoriesContract.View {


    // Bindings
    //
    @Bind(R.id.pie_chart)
    PieChart pieChart;

    @Bind(R.id.graph_type)
    MaterialSpinner graphType;


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
        presenter.viewCreated();
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
    public void setPieChart(ArrayList<CategoryGraph> categories, int title) {

        if (categories == null || categories.isEmpty())
            return;

        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int fontColor = typedValue.data;

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        //pieChart.setExtraOffsets(5, 10, 5, 5);

        // The label which shows the category or exercise
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(getResources().getInteger(R.integer.graph_pie_font_size));

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        //pieChart.setCenterTextTypeface(mTfLight);
        pieChart.setCenterText(getString(title));
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

        // add a selection listener
        //pieChart.setOnChartValueSelectedListener(this);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        List<Integer> colors = new ArrayList<>();

        for (CategoryGraph category : categories) {
            entries.add(new PieEntry(category.getValue(), category.getName()));
            colors.add(category.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, getString(title));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(getResources().getInteger(R.integer.graph_pie_font_size));
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);

        pieChart.invalidate();
    }


}
