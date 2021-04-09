package com.example.spearmint;

/**
 * https://www.youtube.com/watch?v=kgJugGyff5o
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class PublishCount extends Fragment {

    Button location;
    TextView latitude, longitude;
    FusedLocationProviderClient client;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button publishCount;
        Button cancelCount;
        final EditText countDescription;
        TextView value;
        Button decrement;
        Button increment;
        final int[] count = {0};

        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.experiment_count, container, false);

        location = view.findViewById(R.id.location);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();

        countDescription = view.findViewById(R.id.countDescription);
        value = view.findViewById(R.id.value);
        decrement = view.findViewById(R.id.decrement);
        increment = view.findViewById(R.id.increment);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Experiments").document(exDescription).collection("Trials");

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]--;
                value.setText("" + count[0]);
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                value.setText("" + count[0]);
            }
        });

        publishCount = view.findViewById(R.id.count_publish);
        publishCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value2 = Integer.toString(count[0]);
                final String description = countDescription.getText().toString();
                String location = "NONE";

                Trial uploadData = new Trial(description, value2, location);

                if (description.length() > 0) {
                    collectionReference
                            .document(description)
                            .set(uploadData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });
                    countDescription.setText("");
                }
                Bundle experimentInfo = new Bundle();
                TrialFragment trialFragment = new TrialFragment();
                experimentInfo.putParcelable("dataKey", experiment);
                trialFragment.setArguments(experimentInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, trialFragment);
                transaction.commit();
            }
        });

        cancelCount = view.findViewById(R.id.count_cancel);
        cancelCount.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle experimentInfo = new Bundle();
                TrialFragment trialFragment = new TrialFragment();
                experimentInfo.putParcelable("dataKey", experiment);
                trialFragment.setArguments(experimentInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, trialFragment);
                transaction.commit();
            }
        }));

        /**
         * https://www.youtube.com/watch?v=VdCQoJtNXAg
         */
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity()
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                else {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }

        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        }
        else {
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                    }
                    else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location = locationResult.getLastLocation();
                                latitude.setText(String.valueOf(location.getLatitude()));
                                longitude.setText(String.valueOf(location.getLongitude()));
                            }
                        };
                        client.requestLocationUpdates(locationRequest
                                , locationCallback, Looper.myLooper());
                    }
                }
            });
        }
        else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
