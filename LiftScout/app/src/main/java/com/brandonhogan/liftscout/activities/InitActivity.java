package com.brandonhogan.liftscout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.model.User;

import io.realm.Realm;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();

        // User has never used the app before
        if (user != null){
            loadHome();
        }
        realm.close();
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
