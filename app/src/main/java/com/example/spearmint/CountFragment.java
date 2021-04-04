package com.example.spearmint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class CountFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.experiment_count, container, false);

        TextView value;
        Button decrement;
        Button increment;
        final int[] count = {0};

        value = view.findViewById(R.id.value);
        decrement = view.findViewById(R.id.decrement);
        increment = view.findViewById(R.id.increment);

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]--;
                value.setText("" + count[0]);
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                value.setText("" + count[0]);
            }
        });
        
        return view;
    }
}
