package com.brandonhogan.liftscout.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.BhDatePicker;
import com.brandonhogan.liftscout.core.model.Category;
import com.brandonhogan.liftscout.core.model.User;
import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InitActivity extends BaseActivity {

    private boolean nameSet, weightSet, ageSet;
    private boolean showWeight, showAge;

    @Bind(R.id.name) TextView name;
    @Bind(R.id.age) BhDatePicker age;
    @Bind(R.id.weight) TextView weight;
    @Bind(R.id.unit_spinner) Spinner unitSpinner;
    @Bind(R.id.start_button)
    CircularProgressButton startButton;

    @Bind(R.id.weight_layout)
    LinearLayout weightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);

        setTitle(R.string.title_activity_init);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupUI(findViewById(android.R.id.content));
        initControl();
        setupName();
        setupWeight();
        setupAge();
    }

    @OnClick(R.id.start_button)
    public void saveUserData() {

        startButton.setIndeterminateProgressMode(true);
        startButton.setProgress(50);

        String errorLog = "";
        boolean focusSet = false;

        String nameValue = name.getText().toString();
        String unitValue = unitSpinner.getSelectedItem().toString();
        Date birthDateValue = age.getDate();
        double weightValue = 0;

        if (nameValue.trim().isEmpty()) {
            errorLog += getResources().getString(R.string.error_name) + "\n";
            name.setError(getResources().getString(R.string.error_name));
            focusSet = true;
            name.requestFocus();
        }

        try {
            weightValue = Double.parseDouble(weight.getText().toString());
        } catch(NumberFormatException nfe) {
            errorLog += getResources().getString(R.string.error_weight);
            weight.setError(getResources().getString(R.string.error_weight));

            if (!focusSet) {
                focusSet = true;
                weight.requestFocus();
            }
        }

        if (age.getDate() == null) {
            errorLog += getResources().getString(R.string.error_age) + "\n";
        }

        if (!errorLog.isEmpty()) {

            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(getResources().getString(R.string.error_title))
            .setContentText(errorLog);

            pDialog.show();

            startButton.setProgress(-1);

            return;
        }

        User user = new User(nameValue, birthDateValue, weightValue, unitValue);
        userManager.setUser(user);

        setupDefaultExercises();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startButton.setProgress(100);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadHome();
                    }
                }, 50);
            }
        }, 2000);
    }

    private void loadHome() {
        Intent intent = new Intent(InitActivity.this, MainActivity.class);
        // Flags will remove this activity from back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initControl() {
        weightLayout.setVisibility(View.GONE);
        weightLayout.setAlpha(0);
        age.setVisibility(View.GONE);
        age.setAlpha(0);
        age.setHint(getResources().getString(R.string.frag_init_age_hint));

        // Setup the scale spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayList<String> unitTypes = new ArrayList<>();
        unitTypes.add(getResources().getString(R.string.lbs));
        unitTypes.add(getResources().getString(R.string.kgs));

        unitSpinner.setAdapter(adapter);
        adapter.addAll(unitTypes);
        adapter.notifyDataSetChanged();
        unitSpinner.setSelection(0);
    }

    private void setupName() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameSet = s.length() > 0;

                if (!showWeight) {
                    showWeight = true;
                    fadeIn(weightLayout);
                }

                showStartButton();
                Log.d(getTAG(), "Name set: " + nameSet + ". Length :" + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupWeight() {

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                weightSet = s.length() > 0;

                if (!showAge) {
                    showAge = true;
                    fadeIn(age);
                }

                showStartButton();
                Log.d(getTAG(), "Weight set: " + weightSet + ". Length :" + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupAge() {
        age.setCallback(new BhDatePicker.DatePickerCallback() {
            @Override
            public void onBhDatePickerDismiss() {
                if (age.getDate() != null) {
                    ageSet = true;
                    showStartButton();
                }
            }
        });
    }

    private void setupDefaultExercises() {
        //TODO : fix when DI is ready
//        getRealm().beginTransaction();
//
//        getRealm().delete(Category.class);
//
//        ArrayList categories = new ArrayList();
//
//        categories.add(createCategory("Abs", R.color.category_red));
//        categories.add(createCategory("Chest", R.color.category_orange));
//        categories.add(createCategory("Back", R.color.category_blue));
//        categories.add(createCategory("Legs", R.color.category_green));
//
//        getRealm().copyToRealmOrUpdate(categories);
//
//        getRealm().commitTransaction();

    }

    private Category createCategory(String name, int color) {
        Category category = new Category();
        category.setName(name);
        category.setColor(color);

        return category;
    }


    private void showStartButton() {
        if (nameSet && weightSet && ageSet)
            startButton.setVisibility(View.VISIBLE);

        startButton.setProgress(0);
    }

    // Fades in the views
    private void fadeIn(final View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        objectAnimator.setDuration(450L);

        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        objectAnimator.start();
    }

    // Will make add a touch listener to everything but EditText which
    // will close the keyboard when touched
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText || view instanceof Button || view instanceof Spinner)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}
