package com.example.spearmint;

/**
 * https://www.youtube.com/watch?v=kgJugGyff5o
 */

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class PublishNonNegative extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button publishNonnegative;
        Button cancelNonnegative;
        final EditText nonnegativeDescription;
        TextView value;
        Button decrement;
        Button increment;
        final int[] count = {0};

        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.experiment_nonnegative, container, false);

        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();

        nonnegativeDescription = view.findViewById(R.id.nonnegativeDescription);
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

        publishNonnegative = view.findViewById(R.id.nonnegative_publish);

        publishNonnegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value2 = Integer.toString(count[0]);
                final String description = nonnegativeDescription.getText().toString();
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
                    nonnegativeDescription.setText("");
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

        cancelNonnegative = view.findViewById(R.id.nonnegative_cancel);
        cancelNonnegative.setOnClickListener((new View.OnClickListener() {
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
}
