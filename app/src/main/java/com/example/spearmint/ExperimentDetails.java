package com.example.spearmint;

/**
 * Fragment with clickable elements that displays the experiment title and allows users to view posts, make a post, and respond to posts
 * Currently has a small bug where the experiment title disappears after responding to a post or after "cancelling" a post
 * Connects to firebase and sends information about posts to ResponseFragment.java
 * @author Daniel
 *
 * firebase implementation is from ...
 * Tanzil Shahriar, "Lab 5 Firestore Integration Instructions", https://eclass.srv.ualberta.ca/pluginfile.php/6714046/mod_resource/content/0/Lab%205%20Firestore%20Integration%20Instructions.pdf
 */

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView displayData;
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

        /**
         * Updates the list stored locally in the app with Firebase data to display the data
         */
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

        /**
         * Directs the user to "ResponseFragment.java" to enter a response to a post/question
         * Sends the appropriate data to the response fragment to track what a user is responding to
         */
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
                transaction.replace(R.id.nav_host_fragment, responseFragment);
                transaction.commit();

            }
        });

        /**
         * Takes the data entered by a user and makes it into a "Post" object
         * the post object is uploaded to firebase and displays post details to users
         */
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

        goBack = view.findViewById(R.id.go_back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExperimentFragment experimentFragment = new ExperimentFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, experimentFragment);
                transaction.commit();
            }
        });
        return view;
    }


}
