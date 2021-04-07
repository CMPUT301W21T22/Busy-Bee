package com.example.spearmint;

/**
 * Fragment that allows a user to publish an experiment with a title, region, and number of trials
 * fragment uploads user entered information to firebase when "publish" is pressed
 * has two clickable buttons called "publish" or "cancel", once either is pressed, the user is redirected to ExperimentFragment.java
 * @author Daniel
 *
 * firebase implementation is from ...
 * Tanzil Shahriar, "Lab 5 Firestore Integration Instructions", https://eclass.srv.ualberta.ca/pluginfile.php/6714046/mod_resource/content/0/Lab%205%20Firestore%20Integration%20Instructions.pdf
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class PublishExperimentFragment extends Fragment {

    private static final String SHARED_PREFS = "SharedPrefs";
    private static final String TEXT = "Text";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button publishExperiment;
        Button cancelPublish;
        final EditText experimentDescription;
        final EditText experimentRegion;
        final EditText experimentCount;
        final TextView experimentOwner;
        ArrayList<String> userInfo = new ArrayList<>();
        final String[] geo = new String[1];
        final String[] type = new String[1];
        final Spinner trialType;
        final Spinner geoLocation;
        ArrayAdapter<CharSequence> adapter;
        ArrayAdapter<CharSequence> adapter2;
        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        publishExperiment = view.findViewById(R.id.publish_button);
        cancelPublish = view.findViewById(R.id.cancel);
        experimentDescription = view.findViewById(R.id.description);
        experimentRegion = view.findViewById(R.id.region);
        experimentCount = view.findViewById(R.id.count);
        experimentOwner = view.findViewById(R.id.username);

        geoLocation = (Spinner) view.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.names, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        geoLocation.setAdapter(adapter);
        
        trialType = (Spinner) view.findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.options, R.layout.support_simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trialType.setAdapter(adapter2);
        
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Experiments");
        final CollectionReference collectionReferenceUser = db.collection("User");

        /**
         * I don't think we need this block of code anymore? - Gave
         */
        // Accesses the specific user document and gets the username saved to the unique ID
//        String userID = getArguments().getString("dataKey");
//        userInfo.add(userID);
//        DocumentReference user = collectionReferenceUser.document(userID);
//        user.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                String experimentOwnerName = (String) value.get("Username");
//                userInfo.add(experimentOwnerName);
//            }
//        });
//        experimentOwner.setText(userID);
        /**
         * Takes the data entered by a user and makes it into a "Experiment" object
         * the experiment object is uploaded to firebase and displays experiment details to users
         * directs user back to the experiment fragment "ExperimentFragment.java"
         * does not upload data if any of the edit text fields are empty
         */
        
        geoLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                geo[0] = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        trialType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type[0] = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        publishExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String exDescription = experimentDescription.getText().toString();
                final String exRegion = experimentRegion.getText().toString();
                final String exCount = experimentCount.getText().toString();
                final String exOwner = experimentOwner.getText().toString();

                Experiment uploadData = new Experiment(exDescription, exRegion, exCount, userInfo, geo[0], type[0]);

                if (exDescription.length()>0 && exRegion.length()>0 && exCount.length()>0) {

                    collectionReference
                            .document(exDescription)
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

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    String uniqueID = sharedPreferences.getString(TEXT, null);


                    collectionReferenceUser
                            .document(uniqueID).collection("myExperiment").document(exDescription)
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


                    experimentDescription.setText("");
                    experimentRegion.setText("");
                    experimentCount.setText("");
                    experimentOwner.setText("");
                }

                ExperimentFragment experimentFragment = new ExperimentFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, experimentFragment);
                transaction.commit();

            }
        });

        /**
         * Redirects the user back to experiment fragment "ExperimentFragment"
         */
        cancelPublish.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExperimentFragment experimentFragment = new ExperimentFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, experimentFragment);
                transaction.commit();
            }
        }));

        // Inflate the layout for this fragment
        return view;
    }

}
