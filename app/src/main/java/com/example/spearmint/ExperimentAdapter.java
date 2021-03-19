package com.example.spearmint;

/**
 * Custom adapter that processes and stores Experiment objects in a list
 * connects to content.xml file for the visual formatting of objects stored in the adapter
 * @author Daniel and Andrew
 */

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;

import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExperimentAdapter extends ArrayAdapter<Experiment> {
    private int resourceLayout;
    private ArrayList<Experiment> trials;
    private Context context;

    public ExperimentAdapter(Context context, int resource, ArrayList<Experiment> trials) {
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
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }
        Experiment experiment = trials.get(position);

        TextView experiment_description = view.findViewById(R.id.experiment_description);
        TextView experiment_region = view.findViewById(R.id.experiment_region);
        TextView experiment_count = view.findViewById(R.id.experiment_count);

        experiment_description.setText(experiment.getExperimentDescription());
        experiment_region.setText(experiment.getExperimentRegion());
        experiment_count.setText(experiment.getExperimentCount());

        return view;
    }
}
