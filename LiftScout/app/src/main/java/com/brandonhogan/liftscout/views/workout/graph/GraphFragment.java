package com.brandonhogan.liftscout.views.workout.graph;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.brandonhogan.liftscout.views.workout.history.HistoryListSection;
import com.brandonhogan.liftscout.views.workout.history.HistoryPresenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class GraphFragment extends BaseFragment implements GraphContract.View {

    @Bind(R.id.chart)
    LineChart lineChart;

    // Static Properties
    //
    private static final String BUNDLE_EXERCISE_ID = "exerciseIdBundle";


    // Instance
    //
    public static GraphFragment newInstance(int exerciseId)
    {
        GraphFragment frag = new GraphFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_EXERCISE_ID, exerciseId);
        frag.setArguments(bundle);

        return frag;
    }


    // Private Properties
    //
    private View rootView;
    private GraphContract.Presenter presenter;
    private float graphMaxValue = 100;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_workout_graph, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // Typeface mTf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf");

        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);

        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(false);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
      //  leftAxis.setTypeface(mTf);
        leftAxis.setTextSize(8f);
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setValueFormatter(new PercentFormatter());

        XAxis xAxis = lineChart.getXAxis();
      //  xAxis.setTypeface(mTf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.DKGRAY);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMaximum(graphMaxValue);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);

        presenter = new GraphPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
        presenter.viewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void setGraph(List<GraphDataSet> data) {

        if (data.isEmpty())
            return;


        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {

            float volume = (float)data.get(i).getValue();

            values.add(new Entry(i, volume));

            if (volume > graphMaxValue) {
                graphMaxValue = volume + 20;
                lineChart.getAxisLeft().setAxisMaximum(graphMaxValue);
            }
        }

        LineDataSet set1;

//        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            lineChart.getData().notifyDataChanged();
//            lineChart.notifyDataSetChanged();
//        }
//        else {
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
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        presenter.update();
    }
}
