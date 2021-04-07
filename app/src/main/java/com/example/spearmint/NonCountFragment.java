package com.example.spearmint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

public class NonCountFragment extends Fragment {
    Button add_noncount;
    Button end_noncount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("NonCount");

        View view = inflater.inflate(R.layout.experiment_noncount, container, false);

        ListView listView = (ListView) view.findViewById(R.id.noncount_list);

        /**
         * Samantha Squires. (2016, March 1). 1.5: Display a ListView in a Fragment [Video]. YouTube. https://www.youtube.com/watch?v=edZwD54xfbk
         * Abram Hindle, "Lab 3 instructions - CustomList", Public Domain, 2021-02-12, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
         *  https://stackoverflow.com/users/788677/rakhita. (2011, Nov 17). Custom Adapter for List View. https://stackoverflow.com/. https://stackoverflow.com/questions/8166497/custom-adapter-for-list-view/8166802#8166802
         */

        ArrayList<NonCount> noncountList = new ArrayList<>();

        NonCountAdapter customAdapter = new NonCountAdapter(getActivity(), R.layout.noncount_content, noncountList);

        listView.setAdapter(customAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                noncountList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String noncount_description = doc.getId();
                    String noncount_result = ("Count: " + (String) doc.get("noncountResult"));

                    noncountList.add(new NonCount(noncount_description, noncount_result));
                }
                customAdapter.notifyDataSetChanged();
            }
        });

        add_noncount = view.findViewById(R.id.add_noncount);
        add_noncount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishNonCountFragment publishNonCountFragment = new PublishNonCountFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, publishNonCountFragment);
                transaction.commit();
            }
        });

        end_noncount = view.findViewById(R.id.end_noncount);
        end_noncount.setOnClickListener(new View.OnClickListener() {
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
