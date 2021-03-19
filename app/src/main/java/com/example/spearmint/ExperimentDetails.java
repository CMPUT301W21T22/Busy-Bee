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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ExperimentDetails extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button goBack;
        Button post;
        EditText displayData;
        EditText question;
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Posts");

        View view = inflater.inflate(R.layout.experiment_details, container, false);

        String experimentData = getArguments().getString("dataKey");

        // Setting the EditText to the experiment title from ExperimentFragment.java
        displayData = view.findViewById(R.id.experiment_title);
        displayData.setText(experimentData);

        question = view.findViewById(R.id.post_question);
        post = view.findViewById(R.id.post_question_button);

        ListView listView = (ListView) view.findViewById(R.id.post_list);

        ArrayList<Post> postList = new ArrayList<>();

        PostAdapter customAdapter = new PostAdapter(getActivity(), R.layout.content, postList);

        listView.setAdapter(customAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                postList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String questionText = doc.getId();
                    String title = (String) doc.get("experimentTitle");

                    postList.add(new Post(questionText, title));
                }
                customAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle questionInfo = new Bundle();
                Bundle parentQuestion = new Bundle();
                ResponseFragment responseFragment = new ResponseFragment();
                String questionExperiment = experimentData;
                String questionTitle = postList.get(position).getExperimentTitle();

                questionInfo.putString("dataKey", questionExperiment);
                parentQuestion.putString("questionKey", questionTitle);

                Log.d(TAG, questionTitle);

                responseFragment.setArguments(questionInfo);
                responseFragment.setArguments(parentQuestion);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.navHostfragment, responseFragment);
                transaction.commit();

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = displayData.getText().toString();
                final String questionText = question.getText().toString();

                Post content = new Post(title, questionText);

                if (questionText.length()>0) {
                    collectionReference
                            .document(questionText)
                            .set(content)
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
                            });}
                question.setText("");
            }
        });




        // Go back to the experiment fragment
        goBack = view.findViewById(R.id.go_back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExperimentFragment experimentFragment = new ExperimentFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.navHostfragment, experimentFragment);
                transaction.commit();
            }
        });

        return view;
    }


}
