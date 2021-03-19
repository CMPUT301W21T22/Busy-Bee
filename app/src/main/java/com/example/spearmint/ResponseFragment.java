package com.example.spearmint;

/**
 * Fragment opened after a post in "ExperimentDetails" is clicked that allows users to respond to a post
 * Takes in user entered text and uploads to firebase after "post" is clicked
 * Has two clickable buttons called "post" or "cancel", once either is pressed, the user is redirected to the "ExperimentDetails.java" fragment
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

public class ResponseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button cancelResponse;
        Button confirmResponse;
        EditText responseText;
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Posts");

        View view = inflater.inflate(R.layout.responses, container, false);

        String questionData = getArguments().getString("dataKey");
        String questionTitle = getArguments().getString("questionKey");

        confirmResponse = view.findViewById(R.id.confirm_response);
        cancelResponse = view.findViewById(R.id.cancel_response);
        responseText = view.findViewById(R.id.response_text);

        /**
         * Redirects the user to experiment details fragment "ExperimentDetails.java" through "cancel" button
         * sends the title of the question to track what the response is linked to
         */
        cancelResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle questionInfo = new Bundle();
                ExperimentDetails detailsFragment = new ExperimentDetails();
                String questionExperiment = questionData;

                questionInfo.putString("dataKey", questionExperiment);
                detailsFragment.setArguments(questionInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        });

        /**
         * Takes the data entered by a user and makes it into a "Post" object
         * the post object is uploaded to firebase and displays post details to users
         * directs user back to the experiment details fragment "ExperimentDetails.java"
         * does not upload data if any of the edit text fields are empty
         */
        confirmResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String responseContent = responseText.getText().toString();
                final String responseQuestion = questionTitle;

                Post content = new Post(responseQuestion, responseContent);

                if (responseContent.length()>0) {
                    collectionReference
                            .document(responseContent)
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
                responseText.setText("");

                Bundle questionInfo = new Bundle();
                ExperimentDetails detailsFragment = new ExperimentDetails();
                String questionExperiment = questionData;

                questionInfo.putString("dataKey", questionExperiment);
                detailsFragment.setArguments(questionInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        });

        return view;
    };


}
