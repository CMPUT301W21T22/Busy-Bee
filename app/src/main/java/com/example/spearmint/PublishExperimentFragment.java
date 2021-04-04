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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PublishExperimentFragment extends Fragment {

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
        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        publishExperiment = view.findViewById(R.id.publish_button);
        cancelPublish = view.findViewById(R.id.cancel);
        experimentDescription = view.findViewById(R.id.description);
        experimentRegion = view.findViewById(R.id.region);
        experimentCount = view.findViewById(R.id.count);
        experimentOwner = view.findViewById(R.id.username);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Experiments");
        final CollectionReference collectionReferenceUser = db.collection("User");

        // Accesses the specific user document and gets the username saved to the unique ID
        // TODO: FIX THIS SO IT IS NOT HARD CODED
        String userID = getArguments().getString("dataKey");
        userInfo.add(userID);
        DocumentReference user = collectionReferenceUser.document(userID);
        user.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String experimentOwnerName = (String) value.get("Username");
                userInfo.add(experimentOwnerName);
            }
        });
        experimentOwner.setText(userID);

        /**
         * Takes the data entered by a user and makes it into a "Experiment" object
         * the experiment object is uploaded to firebase and displays experiment details to users
         * directs user back to the experiment fragment "ExperimentFragment.java"
         * does not upload data if any of the edit text fields are empty
         */
        publishExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String exDescription = experimentDescription.getText().toString();
                final String exRegion = experimentRegion.getText().toString();
                final String exCount = experimentCount.getText().toString();

                Experiment uploadData = new Experiment(exDescription, exRegion, exCount, userInfo);

                // Only uploads the experiment if all fields are filled
                if (exDescription.length()>0 && exRegion.length()>0 && exCount.length()>0 && !userInfo.isEmpty()) {

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
                    experimentDescription.setText("");
                    experimentRegion.setText("");
                    experimentCount.setText("");
                }

                ExperimentFragment experimentFragment = new ExperimentFragment();
                /*
                Bundle info = new Bundle();
                info.putStringArrayList("dataKey", userInfo);
                experimentFragment.setArguments(info);
                */
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