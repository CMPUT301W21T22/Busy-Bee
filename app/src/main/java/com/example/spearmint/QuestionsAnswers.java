package com.example.spearmint;

/**
 * Fragment with clickable elements that displays the experiment title and allows users to view posts, make a post, and respond to posts
 * Currently has a small bug where the experiment title disappears after responding to a post or after "cancelling" a post
 * Connects to firebase and sends information about posts to ResponseFragment.java
 * Unused class, but will be implemented at a later date
 * @author Andrew
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

public class QuestionsAnswers extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button goBack;
        TextView displayData;
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Questions and Answers");

        View view = inflater.inflate(R.layout.experiment_questions, container, false);

        String experimentData = getArguments().getString("dataKey");

        // Setting the EditText to the experiment title from ExperimentFragment.java
        displayData = view.findViewById(R.id.experiment_name);
        displayData.setText(experimentData);

        ListView listView = (ListView) view.findViewById(R.id.questions_answers);

        ArrayList<Question> questionList = new ArrayList<>();

        QuestionAdapter customAdapter = new QuestionAdapter(getActivity(), R.layout.question_content, questionList);

        listView.setAdapter(customAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                questionList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String question = doc.getId();
                    String answer = (String) doc.get("Answer");

                    questionList.add(new Question(question, answer));
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
                String questionTitle = questionList.get(position).getQuestion();

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

        // Go back to the experiment fragment
        goBack = view.findViewById(R.id.back_button);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                transaction.replace(R.id.navHostfragment, searchFragment);
                transaction.commit();
            }
        });

        return view;
    }
}
