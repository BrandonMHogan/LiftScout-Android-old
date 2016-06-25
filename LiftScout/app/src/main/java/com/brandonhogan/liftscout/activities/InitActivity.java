package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.brandonhogan.liftscout.foundation.model.User;
import com.brandonhogan.liftscout.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class InitActivity extends BaseActivity {

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

        User user = getRealm().where(User.class).findFirst();

        // User has used the app before and should be directed to the main activity
        if (user != null){
            loadHome();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setupName();
        setupAge();
        setupWeight();
    }

    @OnClick(R.id.start_button)
    public void saveUserData() {

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
                nameSet = s.length() > 0;
                showStartButton();
                Log.d(getClassTag(), "Name set : " + nameSet + ". Length :" + s.length());
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
                ageSet = s.length() > 0;
                showStartButton();
                Log.d(getClassTag(), "Age set : " + ageSet + ". Length :" + s.length());
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
                showStartButton();
                Log.d(getClassTag(), "Weight set : " + weightSet + ". Length :" + s.length());
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
