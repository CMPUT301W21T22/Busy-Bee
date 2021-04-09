package com.example.spearmint;

/**
 * Expected behavior: calculate the quartiles, mean, median, and standard deviations of the results of a specific experiment.
 * Histogram and plots of results is also available.
 * @author Gavriel and Sowon
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.jjoe64.graphview.GraphView;

import static android.content.Context.MODE_PRIVATE;

public class StatisticsTrialFragment extends Fragment {
    private static final String SHARED_PREFS = "SharedPrefs";
    private static final String TEXT = "Text";

    private Button goBack;
    private EditText quartiles;
    private EditText median;
    private EditText mean;
    private EditText stdev;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Expected behavior: goBack button will go back to the previous fragment which is ExperimentDetailsFragment.
     * Each EditText should display the necessary statistics.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @see ExperimentDetailsFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);

        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();

        final CollectionReference collectionReferenceExperiments = db.collection("Experiments");
        final CollectionReference collectionReferenceTrials = collectionReferenceExperiments.document(exDescription).collection("Trials");
        final CollectionReference collectionReferenceUser = db.collection("User");

//       final GraphView graph = view.findViewById(R.id.plot_graph);
       Button button = view.findViewById(R.id.plot_button);

        goBack = view.findViewById(R.id.stats_go_back);
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

        quartiles = view.findViewById(R.id.edit_text_quartiles);
        median = view.findViewById(R.id.edit_text_median);
        mean = view.findViewById(R.id.edit_text_mean);
        stdev = view.findViewById(R.id.edit_text_stdev);

        int[] trialResultValue;

//        collectionReferenceTrials.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (QueryDocumentSnapshot queryDocumentSnapshot) {
//                    String trialResult = (String)
//                }
//            }
//        })

//        graph.setVisibility(View.VISIBLE);

        /**
         * Gathers all of the trials from an experiment and plots the points onto a graph
         * !! Does not work yet !!
         * https://tjah.medium.com/how-to-create-a-simple-graph-in-android-6c484324a4c1
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return view;
    }
}
