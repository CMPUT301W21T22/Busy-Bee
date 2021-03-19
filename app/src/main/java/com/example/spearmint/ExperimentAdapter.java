package com.example.spearmint;

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