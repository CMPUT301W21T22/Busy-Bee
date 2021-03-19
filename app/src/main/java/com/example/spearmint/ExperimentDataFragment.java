package com.example.spearmint;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ExperimentDataFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
