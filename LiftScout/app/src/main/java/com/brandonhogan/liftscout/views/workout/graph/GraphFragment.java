package com.brandonhogan.liftscout.views.workout.graph;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.Bind;

public class GraphFragment extends BaseFragment implements GraphContract.View {


    // Bindings
    //
    @Bind(R.id.chart)
    LineChart lineChart;

    @Bind(R.id.slider)
    DiscreteSlider slider;

    @Bind(R.id.range_text)
    TextView rangeText;

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
    private XAxis xAxis;


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

        setupGraph();
        setupSlider();
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
    public void setGraph(List<GraphDataSet> data, int uniqueDateCount) {

        if (data.isEmpty())
            return;

        if (uniqueDateCount > 6) {
            xAxis.setLabelCount(6);
            lineChart.setScaleEnabled(true);
        }
        else {
            xAxis.setLabelCount(uniqueDateCount);
            lineChart.setScaleEnabled(false);
        }

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {

            float volume = (float)data.get(i).getValue();
            float date = (float)data.get(i).getId();

            values.add(new Entry(date, volume));

            if (volume > graphMaxValue) {
                graphMaxValue = volume + 20;
                lineChart.getAxisLeft().setAxisMaximum(graphMaxValue);
            }
        }

        LineDataSet set1;

        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        presenter.update();
    }

    private void setupGraph() {
        // Typeface mTf = Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf");

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(true);

        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(false);

        lineChart.getXAxis().setDrawGridLines(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        //  leftAxis.setTypeface(mTf);
        leftAxis.setTextSize(9f);
       // leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(4));

        xAxis = lineChart.getXAxis();
        //  xAxis.setTypeface(mTf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(9f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");
            private boolean apple = false;

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                Date date = new Date((long)value);
                return mFormat.format(date);
            }
        });

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMaximum(graphMaxValue);
        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);

        presenter = new GraphPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
    }

    private void setupSlider() {
        slider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {

                switch (position) {
                    case 0:
                        rangeText.setText("1 Week");
                        break;
                    case 1:
                        rangeText.setText("1 Month");
                        break;
                    case 2:
                        rangeText.setText("3 Months");
                        break;
                    case 3:
                        rangeText.setText("6 Months");
                        break;
                    default:
                        rangeText.setText("All");
                        break;
                }
            }
        });
    }
}
