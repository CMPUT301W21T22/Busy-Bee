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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class PostFragment extends Fragment {

    private static final String SHARED_PREFS = "SharedPrefs";
    private static final String TEXT = "Text";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button goBack;
        Button comment;
        FirebaseFirestore db;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);
        ArrayList<String> posterInfo = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();

        goBack = view.findViewById(R.id.back_to_details);
        comment = view.findViewById(R.id.comment);

        ListView listView = (ListView) view.findViewById(R.id.post_list);
        ArrayList<Post> postList = new ArrayList<>();
        PostAdapter customAdapter = new PostAdapter(getActivity(), R.layout.experiment_content, postList);
        listView.setAdapter(customAdapter);

        final CollectionReference collectionReferencePosts = db.collection("Experiments").document(exDescription).collection("Posts");
        final CollectionReference collectionReferenceUser = db.collection("User");

        DocumentReference documentReferenceUser = collectionReferenceUser.document(uniqueID);
        documentReferenceUser.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String user = value.getString("Username");
                posterInfo.add(user);
            }
        });

        /**
         * Updates the list stored locally in the app with Firebase data to display the data
         */
        collectionReferencePosts.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                postList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String postOwner = doc.getString("postOwner");
                    String postText = doc.getString("postText");

                    postList.add(new Post(postOwner, postText));
                }
                customAdapter.notifyDataSetChanged();
            }
        });

        /**
         * Redirects the user to experiment details fragment "ExperimentDetails.java" through "cancel" button
         * sends the title of the question to track what the response is linked to
         */
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle experimentInfo = new Bundle();
                ExperimentDetailsFragment detailsFragment = new ExperimentDetailsFragment();

                experimentInfo.putParcelable("dataKey", experiment);
                detailsFragment.setArguments(experimentInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText content;

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.post_comment, null);
                content = view.findViewById(R.id.comment_text);

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(view);
                alert.setPositiveButton("POST", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String postText = content.getText().toString();
                        final String postOwner = posterInfo.get(0);

                        Post content = new Post(postOwner, postText);

                        if (postText.length() > 0) {
                            collectionReferencePosts
                                    .document(postOwner + ": " + postText)
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
                                    });
                        }
                        else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setMessage("You have not entered anything!");
                            alert.show();
                        }
                    }
                });
                alert.show();
            }
        });

        /**
         * Takes the data entered by a user and makes it into a "Post" object
         * the post object is uploaded to firebase and displays post details to users
         * directs user back to the experiment details fragment "ExperimentDetails.java"
         * does not upload data if any of the edit text fields are empty

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
                ExperimentDetailsFragment detailsFragment = new ExperimentDetailsFragment();
                String questionExperiment = questionData;

                questionInfo.putString("dataKey", questionExperiment);
                detailsFragment.setArguments(questionInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
        });
         */

        return view;
    }


}
