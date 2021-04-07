package com.example.spearmint;

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

public class PublishNonCountFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button noncount_publish;
        Button noncount_cancel;
        final EditText noncountDescription;
        final EditText noncountResult;

        FirebaseFirestore db;

        View view = inflater.inflate(R.layout.noncount_publish, container, false);

        noncountDescription = view.findViewById(R.id.noncountDescription);
        noncountResult = view.findViewById(R.id.noncountResult);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("NonCount");

        noncount_publish = view.findViewById(R.id.noncount_publish);
        noncount_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = noncountDescription.getText().toString();
                final String result = noncountResult.getText().toString();

                NonCount uploadData = new NonCount(description, result);

                if (description.length() > 0 && result.length() > 0) {
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
                    noncountDescription.setText("");
                    noncountResult.setText("");
                }
                SearchFragment searchFragment = new SearchFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, searchFragment);
                transaction.commit();
            }
        });

        noncount_cancel = view.findViewById(R.id.noncount_cancel);
        noncount_cancel.setOnClickListener((new View.OnClickListener() {
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
