package com.brandonhogan.liftscout.views.workout.graph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.constants.Bundles;
import com.brandonhogan.liftscout.core.controls.graphs.line.MyLineGraph;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.brandonhogan.liftscout.views.workout.TrackerEvent;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
    private MyLineGraph graph;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_graph_exercise, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        graph.setGraph(data, uniqueDateCount);
        setSelected("","");
    }

    @Override
    public void populateGraphTypes(ArrayList<String> types, int position) {
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
    public void setSelected(String date, String value) {

        if(date.isEmpty())
            lineChart.highlightValue(null);

        selectedItemDate.setText(date);
        selectedItemVale.setText(value);
    }

    private void setupGraph() {
        graph = new MyLineGraph(getActivity().getTheme(), lineChart);
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


    // Subscriptions
    //

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrackerEvent(TrackerEvent event) {
        presenter.update();
    }
}
