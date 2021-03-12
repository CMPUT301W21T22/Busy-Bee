package com.example.spearmint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ProfileFragment userInfo = new ProfileFragment();
//        userInfo.storeUniqueID();
        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.navHostfragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        ExperimentFragment experimentFragment = new ExperimentFragment();
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.navHostfragment, experimentFragment).commit();

        ProfileFragment userInfo = new ProfileFragment();
        userInfo.storeUniqueID(this);
    }

}