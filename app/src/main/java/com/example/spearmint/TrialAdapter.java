package com.example.spearmint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;

public class TrialAdapter extends ArrayAdapter<Trial> {
    private int resourceLayout;
    private ArrayList<Trial> trialList;
    private Context context;

    public TrialAdapter(Context context, int resource, ArrayList<Trial> trialList) {
        super(context, resource, trialList);
        this.resourceLayout = resource;
        this.trialList = trialList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.trial_content, parent, false);
        }
        Trial trial = trialList.get(position);

        TextView trial_description = view.findViewById(R.id.trial_description);
        TextView trial_result = view.findViewById(R.id.trial_result);

        trial_description.setText(trial.getTrialDescription());
        trial_result.setText(trial.getTrialResult());

        return view;
    }
}



