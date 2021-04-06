package com.example.spearmint;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class PublishTrialFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button trialPublish;
        Button trialCancel;
        final EditText trialDescription;
        final String[] result = new String[1];
        final Spinner trial;
        ArrayAdapter<CharSequence> adapter;

        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.trial_publish, container, false);

        trialDescription = view.findViewById(R.id.trialDescription);

        trial = (Spinner) view.findViewById(R.id.trial_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.binomial, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trial.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Binomial");

        trial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result[0] = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        trialPublish = view.findViewById(R.id.trial_publish);

        trialPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = trialDescription.getText().toString();

                Trial uploadData = new Trial(description,result[0]);

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
                    trialDescription.setText("");
                }
                SearchFragment searchFragment = new SearchFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, searchFragment);
                transaction.commit();
            }
        });

        trialCancel = view.findViewById(R.id.trial_cancel);
        trialCancel.setOnClickListener((new View.OnClickListener() {
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
