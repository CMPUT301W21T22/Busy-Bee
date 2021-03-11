package com.example.spearmint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.navHostfragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        ExperimentFragment experimentFragment = new ExperimentFragment();
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.navHostfragment, experimentFragment).commit();

    }

}