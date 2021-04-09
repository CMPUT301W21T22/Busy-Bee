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

public class TrialFragment extends Fragment {

    Button addTrial;
    Button goBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_trials, container, false);
        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();
        String exType = experiment.getTrialType();

        ListView listView = (ListView) view.findViewById(R.id.trial_list);

        final CollectionReference collectionReference = db.collection("Experiments").document(exDescription).collection("Trials");

        /**
         * Samantha Squires. (2016, March 1). 1.5: Display a ListView in a Fragment [Video]. YouTube. https://www.youtube.com/watch?v=edZwD54xfbk
         * Abram Hindle, "Lab 3 instructions - CustomList", Public Domain, 2021-02-12, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
         *  https://stackoverflow.com/users/788677/rakhita. (2011, Nov 17). Custom Adapter for List View. https://stackoverflow.com/. https://stackoverflow.com/questions/8166497/custom-adapter-for-list-view/8166802#8166802
         */

        ArrayList<Trial> trialList = new ArrayList<>();

        TrialAdapter customAdapter = new TrialAdapter(getActivity(), R.layout.trial_content, trialList);

        listView.setAdapter(customAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                trialList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String trialDescription = doc.getId();
                    String trialResult = (String) doc.get("trialResult");

                    trialList.add(new Trial(trialDescription, trialResult));
                }
                customAdapter.notifyDataSetChanged();
            }
        });

        addTrial = view.findViewById(R.id.add_trial);
        addTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle experimentInfo = new Bundle();
                experimentInfo.putParcelable("dataKey", experiment);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                switch (exType) {
                    case "Counts":
                        PublishCount publishCount = new PublishCount();
                        publishCount.setArguments(experimentInfo);
                        transaction.replace(R.id.nav_host_fragment, publishCount);
                        transaction.commit();
                        break;
                    case "Binomial Trials":
                        PublishBinomial publishBinomial = new PublishBinomial();
                        publishBinomial.setArguments(experimentInfo);
                        transaction.replace(R.id.nav_host_fragment, publishBinomial);
                        transaction.commit();
                        break;
                    case "Non-negative Integer Counts":
                        PublishNonNegative publishNonNegative = new PublishNonNegative();
                        publishNonNegative.setArguments(experimentInfo);
                        transaction.replace(R.id.nav_host_fragment, publishNonNegative);
                        transaction.commit();
                        break;
                    case "Measurement Trials":
                        PublishMeasurement publishMeasurement = new PublishMeasurement();
                        publishMeasurement.setArguments(experimentInfo);
                        transaction.replace(R.id.nav_host_fragment, publishMeasurement);
                        transaction.commit();
                        break;
                }

            }
        });

        goBack = view.findViewById(R.id.end_trial);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle experimentInfo = new Bundle();
                ExperimentDetailsFragment experimentDetailsFragment = new ExperimentDetailsFragment();
                experimentInfo.putParcelable("dataKey", experiment);
                experimentDetailsFragment.setArguments(experimentInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, experimentDetailsFragment);
                transaction.commit();
            }
        });

        return view;
    }
}
