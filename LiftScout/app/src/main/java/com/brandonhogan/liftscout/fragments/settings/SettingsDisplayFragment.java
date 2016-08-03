package com.brandonhogan.liftscout.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.foundation.model.UserSetting;
import com.brandonhogan.liftscout.foundation.utils.constants.Themes;
import com.brandonhogan.liftscout.fragments.base.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsDisplayFragment extends BaseFragment {

    // Instance
    //
    public static SettingsDisplayFragment newInstance() {
        return new SettingsDisplayFragment();
    }


    // Private Properties
    //
    private View rootView;
    private String oldThemeValue;
    private ArrayList<String> themes;
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

        setTitle(getResources().getString(R.string.title_frag_settings_display));

        // Sets the original theme value so I can see if it has been changed when we try and save.
        oldThemeValue = getTheme().getValue();

        themes = new ArrayList<>();
        themes.add(Themes.DARK);
        themes.add(Themes.LIGHT);

        themeSpinner.setItems(themes);
        themeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();


            }
        });

        themeSpinner.setSelectedIndex(themes.indexOf(getTheme().getValue()));
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

                // If current selected no longer matches what they came in with,
                // we need to warn them that saving will restart the activity
                if (!oldThemeValue.equals(themes.get(themeSpinner.getSelectedIndex()))) {

                    dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.dialog_restarting_app_theme_title))
                            .setContentText(getString(R.string.dialog_restarting_app_theme_message))
                            .setConfirmText(getString(R.string.restart))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    saveSettings();
                                    restartActivity();
                                }
                            })
                            .setCancelText(getString(R.string.cancel))
                            .showCancelButton(true);

                    dialog.show();
                }
                else {
                    saveSettings();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private UserSetting getTheme() {
        UserSetting theme = getRealm().where(UserSetting.class)
                .equalTo(UserSetting.NAME, UserSetting.THEME).findFirst();

        if (theme == null) {
            theme = new UserSetting();
            theme.setName(UserSetting.THEME);
            theme.setValue(Themes.LIGHT);
        }
         return theme;
    }

    private void restartActivity() {

    }

    private void saveSettings() {

    }
}
