package com.example.spearmint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class PublishMeasurementsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button measurementsPublish;
        Button measurementsCancel;
        final EditText measurementDescription;
        final EditText measurementResult;
        final EditText measurementUnit;

        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.measurements_publish, container, false);

        measurementDescription = view.findViewById(R.id.measurementDescription);
        measurementResult = view.findViewById(R.id.measurementResult);
        measurementUnit = view.findViewById(R.id.measurementUnit);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Measurements");

        measurementsPublish = view.findViewById(R.id.measurement_publish);
        measurementsPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = measurementDescription.getText().toString();
                final String result = measurementResult.getText().toString();
                final String unit = measurementUnit.getText().toString();

                Measurements uploadData = new Measurements(description, result, unit);

                if (description.length() > 0 && result.length() > 0 && unit.length() > 0) {
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
                    measurementDescription.setText("");
                    measurementResult.setText("");
                    measurementUnit.setText("");
                }
                Bundle experimentInfo = new Bundle();
                QuestionsAnswers detailsFragment = new QuestionsAnswers();
                detailsFragment.setArguments(experimentInfo);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        });


        measurementsCancel = view.findViewById(R.id.measurement_cancel);
        measurementsCancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle experimentInfo = new Bundle();
                QuestionsAnswers detailsFragment = new QuestionsAnswers();
                detailsFragment.setArguments(experimentInfo);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        }));

            return view;
        }
}
