package com.example.spearmint;

/**
 * Custom adapter that processes and stores Question objects in a list
 * connects to question_content.xml file for the visual formatting of objects stored in the adapter
 * @author Andrew
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class QuestionAdapter extends ArrayAdapter<Question> {
    private int resourceLayout;
    private ArrayList<Question> questions;
    private Context context;

    public QuestionAdapter(Context context, int resource, ArrayList<Question> questions) {
        super(context, resource, questions);
        this.resourceLayout = resource;
        this.questions = questions;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.post_content, parent, false);
        }
        Question questionAnswers = questions.get(position);

        TextView question = view.findViewById(R.id.experiment_question);
        TextView answer = view.findViewById(R.id.experiment_answer);

        question.setText(questionAnswers.getQuestion());
        answer.setText(questionAnswers.getAnswer());

        return view;
    }
}