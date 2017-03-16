package com.brandonhogan.liftscout.core.controls.graphs.line;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Charts;
import com.brandonhogan.liftscout.core.managers.GraphManager;
import com.brandonhogan.liftscout.core.managers.NavigationManager;
import com.brandonhogan.liftscout.core.managers.ProgressManager;
import com.brandonhogan.liftscout.core.managers.UserManager;
import com.brandonhogan.liftscout.core.model.Rep;
import com.brandonhogan.liftscout.core.model.Set;
import com.brandonhogan.liftscout.core.utils.AttrUtil;
import com.brandonhogan.liftscout.core.utils.BhDate;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.workout.graph.GraphDataSet;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by Brandon on 2/27/2017.
 * Description :
 */

public class MyLineGraph extends FrameLayout {

    // Injections
    //
    @Inject
    ProgressManager progressManager;

    @Inject
    GraphManager graphManager;

    @Inject
    UserManager userManager;

    @Inject
    Application application;

    @Inject
    NavigationManager navigationManager;

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

    @Bind(R.id.exercise_selector)
    Button exerciseSelector;

    private static final int GRAPH_MAX_VALUE_DEFAULT = 30;

    private float graphMaxValue = GRAPH_MAX_VALUE_DEFAULT;
    private XAxis xAxis;
    private ArrayList<String> graphTypes;
    private int currentGraphType;
    private int exerciseId;
    private String exerciseName;
    private List<GraphDataSet> items;
    private boolean exerciseIsSet = false;
    private int newRangePosition;
    private int currentRangePosition;
    private boolean isFirstSet = true;
    private int textColor;
    private int fillColor;


    // Constructors

    public MyLineGraph(Context context) {
        this(context, null);
    }

    public MyLineGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Injector.getAppComponent().inject(this);

        View root = inflate(getContext(), R.layout.con_graph_line, this);
        ButterKnife.bind(this, root);

        exerciseSelector.setText(R.string.charts_select_exercise);

        setupTypes();
        setupRangeSlider();
    }

    public void init(Resources.Theme theme, boolean showExerciseSelector) {

        exerciseSelector.setVisibility(showExerciseSelector ? VISIBLE : GONE);

        textColor = AttrUtil.getAttributeRes(theme, android.R.attr.textColorPrimary);
        fillColor = AttrUtil.getAttributeRes(theme, android.R.attr.colorAccent);

        lineChart.animate();
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setNoDataText(getResources().getString(R.string.charts_empty_exercise));
        lineChart.setNoDataTextColor(textColor);

        // Y Axis setup
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setTextSize(11f);
        leftAxis.setAxisMaximum(graphMaxValue);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(textColor);
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(4));

        // X Axis setup
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(textColor);
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

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                onChartValueSelected(e);
            }

            @Override
            public void onNothingSelected() {
                setSelected("","");
            }
        });
    }

    private void setupTypes() {
        graphTypes = new ArrayList<>();
        graphTypes.add(Charts.WORKOUT_VOLUME);
        graphTypes.add(Charts.MAX_WEIGHT);
        graphTypes.add(Charts.MAX_REPS);
        graphTypes.add(Charts.TOTAL_REPS);

        graphType.setItems(graphTypes);
        graphType.setSelectedIndex(0);

        graphType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                currentGraphType = position;
                update(true);
            }
        });
    }

    private void setupRangeSlider() {
        slider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {

                switch (position) {
                    case 0:
                        rangeText.setText(application.getString(R.string.one_week));
                        break;
                    case 1:
                        rangeText.setText(application.getString(R.string.one_month));
                        break;
                    case 2:
                        rangeText.setText(application.getString(R.string.three_months));
                        break;
                    case 3:
                        rangeText.setText(application.getString(R.string.six_months));
                        break;
                    case 4:
                        rangeText.setText(application.getString(R.string.one_year));
                        break;
                    default:
                        rangeText.setText(application.getString(R.string.all));
                        break;
                }

                newRangePosition = position;
                update(false);
            }
        });

    }

    private void setSelected(String date, String value) {

        if(date.isEmpty())
            lineChart.highlightValue(null);

        selectedItemDate.setText(date);

        selectedItemVale.setText(value);
    }

    private void onChartValueSelected(Entry entry) {

        String value = "";

        if (graphTypes.get(currentGraphType).equals(Charts.MAX_REPS) || graphTypes.get(currentGraphType).equals(Charts.TOTAL_REPS))
            value = (Float.toString(((long)entry.getY())).replaceAll("\\.0*$", "")) + " reps";

        else
            value = Float.toString(((long)entry.getY())) + " " + userManager.getMeasurementValue();

        setSelected(BhDate.toSimpleStringDate(getDateByFloat(entry.getX())), value);
    }

    private Date getDateByFloat(float value) {
        Calendar calendar = Calendar.getInstance();

        for(GraphDataSet item : items) {
            if ((float)item.getId() == value) {
                calendar.setTimeInMillis(item.getId());
                break;
            }
        }
        return calendar.getTime();
    }

    private void setupWorkoutVolume(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                double volume = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    volume += rep.getWeight() * rep.getCount();
                }

                if (volume > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), volume);
                    items.add(item);
                }

            }
        }
        setGraph(items, uniqueDateCount);
    }

    private void setupMaxWeight(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                double max = 0;
                double weight = 0;
                int reps = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    double value = rep.getWeight();

                    if (value > max) {
                        max = value;
                        weight = rep.getWeight();
                        reps = rep.getCount();
                    }
                }

                if (max > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), max, weight, reps);
                    items.add(item);
                }

            }
        }
        setGraph(items, uniqueDateCount);
    }

    private void setupMaxRep(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                double max = 0;
                double weight = 0;
                int reps = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    double value = rep.getCount();

                    if (value > max) {
                        max = value;
                        weight = rep.getWeight();
                        reps = rep.getCount();
                    }
                }

                if (max > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), max, weight, reps);
                    items.add(item);
                }

            }
        }
        setGraph(items, uniqueDateCount);
    }

    private void setupTotalReps(RealmResults<Set> sets) {
        int uniqueDateCount = 0;

        if (sets != null) {

            ArrayList<Date> dates = new ArrayList<>();

            for (int count = sets.size() -1; count >= 0; count--) {

                int value = 0;

                for (Rep rep : sets.get(count).getReps()) {
                    value += rep.getCount();
                }

                if (value > 0){
                    Date date = sets.get(count).getDate();

                    if (!dates.contains(date)) {
                        uniqueDateCount ++;
                        dates.add(date);
                    }

                    GraphDataSet item = new GraphDataSet(date.getTime(), value);
                    items.add(item);
                }

            }
        }
        setGraph(items, uniqueDateCount);
    }

    private void setGraph(List<GraphDataSet> data, int uniqueDateCount) {

        if (data.isEmpty()) {
            lineChart.setNoDataText(getResources().getString(R.string.charts_empty_exercise_no_data, exerciseName));
            return;
        }
        else if (data.size() < 2) {
            lineChart.setNoDataText(getResources().getString(R.string.charts_empty_exercise_not_enough_data));
            lineChart.setData(null);
            return;
        }

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
            set1.setFillColor(fillColor);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data2 = new LineData(dataSets);

            // set data
            lineChart.setData(data2);
            lineChart.animateXY(0,400);
        }
    }


    // Public Functions
    //

    public void setExercise(int exerciseId, String exerciseName) {
        setExercise(exerciseId, exerciseName, 0);
    }

    public void setExercise(int exerciseId, String exerciseName, int dateRange) {
        if (exerciseSelector.getVisibility() == VISIBLE && exerciseName != null && !exerciseName.isEmpty())
            exerciseSelector.setText(exerciseName);

        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        exerciseIsSet = true;
        currentRangePosition = newRangePosition = dateRange;
        slider.setPosition(currentRangePosition);
        update(true);
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getCurrentRangePosition() {
        return currentRangePosition;
    }

    public void update(boolean force) {

        if (!exerciseIsSet)
            return;

        // don't rebuild if the date range was not changed
        if (!force && newRangePosition == currentRangePosition)
            return;

        currentRangePosition = newRangePosition;

        RealmResults<Set> sets = progressManager.getSetsByExercise(exerciseId);
        Calendar calendar = Calendar.getInstance();

        switch (newRangePosition) {
            case 0:
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case 1:
                calendar.add(Calendar.MONTH, -1);
                break;
            case 2:
                calendar.add(Calendar.MONTH, -3);
                break;
            case 3:
                calendar.add(Calendar.MONTH, -6);
                break;
            case 4:
                calendar.add(Calendar.YEAR, -1);
                break;
            default:
                calendar.add(Calendar.YEAR, -10);
                break;

        }

        sets = sets.where().between("date", calendar.getTime(), new Date()).findAll();

        items = new LinkedList<>();

        switch (graphTypes.get(currentGraphType)) {
            case Charts.WORKOUT_VOLUME:
                setupWorkoutVolume(sets);
                break;
            case Charts.MAX_WEIGHT:
                setupMaxWeight(sets);
                break;
            case Charts.MAX_REPS:
                setupMaxRep(sets);
                break;
            case Charts.TOTAL_REPS:
                setupTotalReps(sets);
                break;
        }


        setSelected("", "");
        lineChart.invalidate();
    }


    @OnClick(R.id.exercise_selector)
    void onClick() {
        graphManager.setInSearch(true);
        graphManager.setGraphName(navigationManager.getCurrentFragmentName());
        navigationManager.startCategoryListGraphSearch();
    }

}
