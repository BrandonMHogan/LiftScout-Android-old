package com.brandonhogan.liftscout.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.brandonhogan.liftscout.foundation.controls.BhDatePicker;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.R;
import java.util.Date;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InitActivity extends BaseActivity {

    private boolean nameSet, weightSet;
    private boolean showWeight, showAge;

    @Bind(R.id.name) TextView name;
    @Bind(R.id.age) BhDatePicker age;
    @Bind(R.id.weight) TextView weight;
    @Bind(R.id.start_button) Button startButton;

    @Bind(R.id.weight_inputlayout)
    TextInputLayout weightInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);

        setTitle(R.string.title_activity_init);

        User user = getRealm().where(User.class).findFirst();

        // User has used the app before and should be directed to the main activity
        if (user != null){
            loadHome();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initControl();
        setupName();
        setupWeight();
    }

    @OnClick(R.id.start_button)
    public void saveUserData() {

        String nameValue = name.getText().toString();
        Date birthDate;
        double weightValue = 0;

        try {
            birthDate = age.getDate();
            weightValue = Double.parseDouble(weight.getText().toString());
        } catch(NumberFormatException nfe) {
            Log.e(getClassTag(), "Failed to save user data. Likely conversion failure");
            return;
        }

        getRealm().beginTransaction();

        User user = new User(nameValue, birthDate, weightValue);
        getRealm().copyToRealmOrUpdate(user);

        getRealm().commitTransaction();

        loadHome();
    }

    private void loadHome() {
        Intent intent = new Intent(InitActivity.this, MainActivity.class);
        // Flags will remove this activity from back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initControl() {
        weightInputLayout.setAlpha(0);
        age.setAlpha(0);
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
                    fadeIn(weightInputLayout);
                }

                showStartButton();
                Log.d(getClassTag(), "Name set: " + nameSet + ". Length :" + s.length());
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
                Log.d(getClassTag(), "Weight set: " + weightSet + ". Length :" + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showStartButton() {
        if (nameSet && weightSet)
            startButton.setVisibility(View.VISIBLE);
        else
            startButton.setVisibility(View.INVISIBLE);
    }

    private void fadeIn(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        objectAnimator.setDuration(500L);
        objectAnimator.start();
    }


    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

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
