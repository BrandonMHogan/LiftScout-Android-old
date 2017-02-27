package com.brandonhogan.liftscout.views.workout.graph;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @Bind(R.id.selected_item_date)
    TextView selectedItemDate;

    @Bind(R.id.selected_item_value)
    TextView selectedItemVale;

    @Bind(R.id.graph_type)
    MaterialSpinner graphType;

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

        setupSelector();
        setupGraph();
        setupSlider();
        presenter.viewCreated(1);
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

        graphMaxValue = 100;

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

        setSelected("","");
    }

    @Override
    public void setSelected(String date, String value) {

        if(date.isEmpty())
            lineChart.highlightValue(null);

        selectedItemDate.setText(date);
        selectedItemVale.setText(value);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        presenter.update();
    }

    private void setupSelector() {

        final ArrayList<String> types = new ArrayList<>();
        types.add(getString(R.string.graph_volume));

        graphType.setItems(types);
        graphType.setSelectedIndex(0);

        graphType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onTypeSelected(types.get(position));
            }
        });
    }

    private void setupGraph() {

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                presenter.onItemSelected(e);
            }

            @Override
            public void onNothingSelected() {
                setSelected("","");
            }
        });

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

        presenter = new GraphPresenter(this, getArguments().getInt(BUNDLE_EXERCISE_ID, Bundles.SHIT_ID));
    }

    private void setupSlider() {
        slider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {

                switch (position) {
                    case 0:
                        rangeText.setText(getString(R.string.one_week));
                        break;
                    case 1:
                        rangeText.setText(getString(R.string.one_month));
                        break;
                    case 2:
                        rangeText.setText(getString(R.string.three_months));
                        break;
                    case 3:
                        rangeText.setText(getString(R.string.six_months));
                        break;
                    case 4:
                        rangeText.setText(getString(R.string.one_year));
                        break;
                    default:
                        rangeText.setText(getString(R.string.all));
                        break;
                }

                presenter.update(position);
            }
        });
    }
}
