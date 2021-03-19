package com.example.spearmint;

/**
 * Custom adapter that processes and stores Post objects in a list
 * connects to post_content.xml file for the visual formatting of objects stored in the adapter
 * @author Daniel
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

public class PostAdapter extends ArrayAdapter<Post> {
    private int resourceLayout;
    private ArrayList<Post> trials;
    private Context context;

    public PostAdapter(Context context, int resource, ArrayList<Post> trials) {
        super(context, resource, trials);
        this.resourceLayout = resource;
        this.trials = trials;
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
        Post post = trials.get(position);

        TextView postContent = view.findViewById(R.id.post_text);
        TextView experimentTitle = view.findViewById(R.id.post_experiment);

        postContent.setText(post.getText());
        experimentTitle.setText(post.getExperimentTitle());

        return view;
    }
}