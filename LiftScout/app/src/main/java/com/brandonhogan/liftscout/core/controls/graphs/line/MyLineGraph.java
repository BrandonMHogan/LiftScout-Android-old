package com.brandonhogan.liftscout.core.controls.graphs.line;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.TypedValue;

import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.views.workout.graph.GraphDataSet;
import com.brandonhogan.liftscout.views.workout.graph.GraphPresenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class MyLineGraph {

    private static final int GRAPH_MAX_VALUE_DEFAULT = 30;

    private float graphMaxValue = GRAPH_MAX_VALUE_DEFAULT;
    private XAxis xAxis;
    private LineChart lineChart;


    public MyLineGraph(Resources.Theme theme, LineChart lineChart) {
        this.lineChart = lineChart;

        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        lineChart.animate();
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setEnabled(false);

        // Y Axis setup
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setTextSize(11f);
        leftAxis.setAxisMaximum(graphMaxValue);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(color);
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(4));

        // X Axis setup
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(color);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(new Date((long)value));
            }
        });
    }


    public void setGraph(List<GraphDataSet> data, int uniqueDateCount) {

        if (data.isEmpty())
            return;

        graphMaxValue = GRAPH_MAX_VALUE_DEFAULT;
        lineChart.getAxisLeft().setAxisMaximum(graphMaxValue);

        if (uniqueDateCount > 5) {
            xAxis.setLabelCount(5);
            lineChart.setScaleEnabled(true);
        }
        else {
            xAxis.setLabelCount(uniqueDateCount);
            lineChart.setScaleEnabled(false);
        }

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {

            float volume = ((float)data.get(i).getValue());
            float date = data.get(i).getId();


            values.add(new Entry(date, volume));

            if (volume > graphMaxValue) {
                graphMaxValue = volume;
                lineChart.getAxisLeft().setAxisMaximum(graphMaxValue+ 20);
            }
        }

        LineDataSet set1;

        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
            lineChart.animateXY(0,400);
        }
        else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            //set1.enableDashedLine(10f, 5f, 0f);
            // set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                //  Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillColor(Color.BLUE);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data2 = new LineData(dataSets);

            // set data
            lineChart.setData(data2);
        }
    }

}
