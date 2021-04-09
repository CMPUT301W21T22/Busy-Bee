package com.example.spearmint;

/**
 * Fragment that allows a user to publish an binomial trial with a description, result either pass or fail
 * Fragment also gets the user location and displays the latitude and longitude
 * Fragment uploads user entered information to firebase when "publish" is pressed
 * has two clickable buttons called "publish" or "cancel", once either is pressed, the user is redirected to experimental_binomial.java
 * Hana Fakhouri. (2020, March 21). Increment Decrement App | Android Studio Tutorial. YouTube. https://www.youtube.com/watch?v=kgJugGyff5o
 * Android Coding. (2020, November 1). How to Get Current Location in Fragment in Android Studio | CurrentLocation | Android Coding. https://www.youtube.com/watch?v=VdCQoJtNXAg
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;

import androidx.annotation.Nullable;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class PublishBinomial extends Fragment {


    Button location;
    TextView latitude, longitude;
    FusedLocationProviderClient client;

    private static final String SHARED_PREFS = "SharedPrefs";
    private static final String TEXT = "Text";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button binomialPublish;
        Button binomialCancel;
        final EditText binomialDescription;
        final String[] result = new String[1];
        final Spinner binomial;
        ArrayAdapter<CharSequence> adapter;
        ArrayList<String> coordinates = new ArrayList<>();
        ArrayList<String> experimenter = new ArrayList<>();

        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();

        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.experiment_binomial, container, false);


        location = view.findViewById(R.id.location);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);

        client = LocationServices.getFusedLocationProviderClient(getActivity());


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);

        final CollectionReference collectionReference = db.collection("Experiments").document(exDescription).collection("Trials");
        final CollectionReference collectionReferenceUser = db.collection("User");
        DocumentReference documentReference = collectionReferenceUser.document(uniqueID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String username = value.getString("Username");
                experimenter.add(username);
            }
        });


        binomialDescription = view.findViewById(R.id.binomialDescription);

        binomial = (Spinner) view.findViewById(R.id.binomial_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.binomial, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binomial.setAdapter(adapter);

        binomial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result[0] = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /** Used the video to implement the get current location
         * Android Coding. (2020, November 1). How to Get Current Location in Fragment in Android Studio | CurrentLocation | Android Coding. https://www.youtube.com/watch?v=VdCQoJtNXAg
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

        binomialPublish = view.findViewById(R.id.binomial_publish);
        binomialPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = binomialDescription.getText().toString();
                coordinates.add((String) latitude.getText());
                coordinates.add((String) longitude.getText());

                Trial uploadData = new Trial(description, result[0], experimenter.get(0), coordinates);

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
                    binomialDescription.setText("");
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

        binomialCancel = view.findViewById(R.id.binomial_cancel);
        binomialCancel.setOnClickListener((new View.OnClickListener() {
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


        return view;
    }

    /** Used the video to implement the get current location fragment and current location latitude/longitude
     * Android Coding. (2020, November 1). How to Get Current Location in Fragment in Android Studio | CurrentLocation | Android Coding. https://www.youtube.com/watch?v=VdCQoJtNXAg
     */

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
