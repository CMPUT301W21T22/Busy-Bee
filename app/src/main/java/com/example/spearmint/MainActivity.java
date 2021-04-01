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

/**
 * MainActivity initializes a bottom navigation bar to navigate between different fragments of the
 * app. It contains an experiment fragment, search fragment, notifications fragment, and profile
 * fragment.
 * @author Gavriel
 * @see ProfileFragment
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Briana Nzivu, "Bottom Navigation Bar in Android Applications", October 28, 2020, Creative Commons CC ,
         * https://www.section.io/engineering-education/bottom-navigation-bar-in-android/
         *
         * Stevdza-San, "BottomNavigationView with Navigation Component - Android Studio Tutorial",
         * 2020-4-17, Creative Commons CC, https://www.youtube.com/watch?v=Chso6xrJ6aU
         */
        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // New profile that will be stored onto Firebase
        ProfileFragment userInfo = new ProfileFragment();
        userInfo.storeUniqueID(this);
    }

}