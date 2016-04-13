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

import io.realm.Realm;

public class InitActivity extends AppCompatActivity {

    private static final String TAG = "InitActivity";
    private boolean nameSet, ageSet, weightSet;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();

        // User has never used the app before
        if (user != null){
            loadHome();
        }
        realm.close();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome(v);
            }
        });


        setupName();
        setupAge();
        setupWeight();
    }

    /** Called when the user clicks the Send button */
    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadHome() {
        Intent intent = new Intent(InitActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void setupName() {
        TextView name = (TextView) findViewById(R.id.username);

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
        TextView age = (TextView) findViewById(R.id.age);

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
        TextView weight = (TextView) findViewById(R.id.weight);

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
