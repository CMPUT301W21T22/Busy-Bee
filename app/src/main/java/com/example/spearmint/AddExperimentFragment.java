package com.example.spearmint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AddExperimentFragment extends DialogFragment {
    private EditText experimentDescription;
    private EditText experimentRegion;
    private EditText experimentNum;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment experiment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_experiment_fragment, null);

        experimentDescription = view.findViewById(R.id.experiment_description_editText);
        experimentRegion = view.findViewById(R.id.experiment_region_editText);
        experimentNum = view.findViewById(R.id.experiment_num_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String description = experimentDescription.getText().toString();
                        String region = experimentRegion.getText().toString();
                        String num = experimentNum.getText().toString();

                        listener.onOkPressed(new Experiment(description, region, num));
                    }

                }).create();
    }
}