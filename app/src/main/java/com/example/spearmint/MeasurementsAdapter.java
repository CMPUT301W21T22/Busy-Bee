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

public class MeasurementsAdapter extends ArrayAdapter<Measurements> {
    private int resourceLayout;
    private ArrayList<Measurements> measurementList;
    private Context context;

    public MeasurementsAdapter(Context context, int resource, ArrayList<Measurements> measurementList) {
        super(context, resource, measurementList);
        this.resourceLayout = resource;
        this.measurementList = measurementList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.measurements_content, parent, false);
        }
        Measurements measurements = measurementList.get(position);

        TextView measurement_description = view.findViewById(R.id.measurement_description);
        TextView measurement_result = view.findViewById(R.id.measurement_result);
        TextView measurement_unit = view.findViewById(R.id.measurement_unit);

        measurement_description.setText(measurements.getMeasurementDescription());
        measurement_result.setText(measurements.getMeasurementResult());
        measurement_unit.setText(measurements.getMeasurementUnit());

        return view;
    }
}