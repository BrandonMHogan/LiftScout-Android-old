package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.model.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class InitActivity extends AppCompatActivity {

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


        Spinner ageSpinner = (Spinner)findViewById(R.id.age_spinner);


        List age = new ArrayList<Integer>();
        for (int i = 10; i <= 99; i++) {
            age.add(Integer.toString(i));
        }

        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, R.layout.item_spinner_default, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        ageSpinner.setAdapter(spinnerArrayAdapter);
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

}
