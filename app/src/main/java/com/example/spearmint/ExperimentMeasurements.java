package com.example.spearmint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExperimentMeasurements extends Fragment {
    Button add_measurement;
    Button end_measurement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Measurements");

        View view = inflater.inflate(R.layout.experiment_measurements, container, false);

        ListView listView = (ListView) view.findViewById(R.id.measurement_list);

        ArrayList<Measurements> measurementList = new ArrayList<>();

        MeasurementsAdapter customAdapter = new MeasurementsAdapter(getActivity(), R.layout.measurements_content, measurementList);

        listView.setAdapter(customAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                measurementList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String measurement_description = doc.getId();
                    String measurement_result = (String) doc.get("measurementResult");
                    String measurement_unit = (String) doc.get("measurementUnit");

                    measurementList.add(new Measurements(measurement_description, measurement_result, measurement_unit));
                }
                customAdapter.notifyDataSetChanged();
            }
        });

        add_measurement = view.findViewById(R.id.add_measurement);
        add_measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishMeasurementsFragment publishMeasurementsFragment = new PublishMeasurementsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, publishMeasurementsFragment);
                transaction.commit();
            }
        });

        end_measurement = view.findViewById(R.id.end_measurement);
        end_measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle experimentInfo = new Bundle();
                QuestionsAnswers detailsFragment = new QuestionsAnswers();

                detailsFragment.setArguments(experimentInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        });

        return view;

    }
}
