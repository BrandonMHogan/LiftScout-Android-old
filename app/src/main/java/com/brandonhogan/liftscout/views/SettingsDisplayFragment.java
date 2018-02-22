package com.brandonhogan.liftscout.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.injection.components.Injector;
import com.brandonhogan.liftscout.interfaces.contracts.SettingsDisplayContract;
import com.brandonhogan.liftscout.views.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class SettingsDisplayFragment extends BaseFragment implements SettingsDisplayContract.View {

    // Injections
    //
    @Inject
    SettingsDisplayContract.Presenter presenter;

    // Bindings
    //
    @BindView(R.id.themeSpinner)
    MaterialSpinner themeSpinner;

    // Instance
    //
    public static SettingsDisplayFragment newInstance() {
        return new SettingsDisplayFragment();
    }

    // Private Properties
    //
    private MaterialDialog dialog;

    //Overrides
    //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_settings_display, container, false);
        Injector.getFragmentComponent().inject(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setView(this);
        setTitle(getResources().getString(R.string.title_frag_settings_display));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
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

        dialog = new com.afollestad.materialdialogs.MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_restarting_app_theme_title)
                .content(R.string.dialog_restarting_app_theme_message)
                .positiveText(R.string.restart)
                .autoDismiss(false)
                .onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogAccepted();
                    }
                })
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogCancel();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogCancel();
                    }
                })
                .build();

        dialog.show();
    }

    private void dialogAccepted() {
        dialog.dismiss();
        presenter.changeTheme();
    }

    private void dialogCancel() {
        dialog.dismiss();
        themeSpinner.setSelectedIndex(presenter.getOriginalThemeIndex());
    }

    private void restartActivity() {
        getActivity().finish();
        final Intent intent = getActivity().getIntent();
        intent.replaceExtras(new Bundle());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }
}
