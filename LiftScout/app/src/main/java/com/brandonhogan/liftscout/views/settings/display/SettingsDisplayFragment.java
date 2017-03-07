package com.brandonhogan.liftscout.views.settings.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsDisplayFragment extends BaseFragment implements SettingsDisplayContract.View {


    // Instance
    //
    public static SettingsDisplayFragment newInstance() {
        return new SettingsDisplayFragment();
    }


    // Private Properties
    //
    private SettingsDisplayContract.Presenter presenter;
    private View rootView;
    private SweetAlertDialog dialog;


    // Bindings
    //
    @Bind(R.id.themeSpinner)
    MaterialSpinner themeSpinner;


    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_settings_display, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Injector.getAppComponent().inject(this);

        presenter = new SettingsDisplayPresenter(this);

        setTitle(getResources().getString(R.string.title_frag_settings_display));

        presenter.viewCreated();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_save).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                presenter.onSave(false);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void populateThemes(ArrayList<String> themes, int selected) {
        themeSpinner.setItems(themes);
        themeSpinner.setSelectedIndex(selected);

        themeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                presenter.onThemeSelected(position);
            }
        });
    }

    @Override
    public void saveSuccess(boolean restart) {

        if (restart)
            restartActivity();
        else
            getNavigationManager().navigateBack(getActivity());

    }

    @Override
    public void showAlert() {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_restarting_app_theme_title))
                .setContentText(getString(R.string.dialog_restarting_app_theme_message))
                .setConfirmText(getString(R.string.restart))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialogAccepted();
                    }
                })
                .setCancelText(getString(R.string.cancel))
                .showCancelButton(true);

        dialog.show();
    }

    private void dialogAccepted() {
        dialog.cancel();
        presenter.onSave(true);
    }

    private void restartActivity() {
        getActivity().finish();
        final Intent intent = getActivity().getIntent();
        intent.replaceExtras(new Bundle());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }
}
