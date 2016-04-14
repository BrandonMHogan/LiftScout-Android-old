package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class InitActivity extends AppCompatActivity {

    private static final String TAG = "InitActivity";
    private boolean nameSet, ageSet, weightSet;

    @Bind(R.id.name) TextView name;
    @Bind(R.id.age) TextView age;
    @Bind(R.id.weight) TextView weight;
    @Bind(R.id.start_button) Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);

        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();

        // User has never used the app before
        if (user != null){
            loadHome();
        }
        realm.close();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });


        setupName();
        setupAge();
        setupWeight();
    }

    private void saveUserData() {

        String nameValue = name.getText().toString();
        int ageValue = 0;
        double weightValue = 0;

        try {
            ageValue = Integer.parseInt(age.getText().toString());
            weightValue = Double.parseDouble(weight.getText().toString());
        } catch(NumberFormatException nfe) {
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        User user = new User(nameValue, ageValue, weightValue);
        realm.copyToRealmOrUpdate(user);

        realm.commitTransaction();
        realm.close();

        loadHome();
    }

    private void loadHome() {
        Intent intent = new Intent(InitActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void setupName() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameSet = count > 0;
                showStartButton();
                Log.d(TAG, "Name set : " + nameSet);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupAge() {

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ageSet = count > 0;
                showStartButton();
                Log.d(TAG, "Age set : " + ageSet);
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
                weightSet = count > 0;
                showStartButton();
                Log.d(TAG, "Weight set : " + weightSet);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showStartButton() {
        if (nameSet && ageSet && weightSet)
            startButton.setVisibility(View.VISIBLE);
        else
            startButton.setVisibility(View.INVISIBLE);
    }
}
