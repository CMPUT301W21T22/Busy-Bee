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

public class NonCountAdapter extends ArrayAdapter<NonCount> {
    private int resourceLayout;
    private ArrayList<NonCount> noncountList;
    private Context context;

    public NonCountAdapter(Context context, int resource, ArrayList<NonCount> noncountList) {
        super(context, resource, noncountList);
        this.resourceLayout = resource;
        this.noncountList = noncountList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.noncount_content, parent, false);
        }
        NonCount nonCount = noncountList.get(position);

        TextView noncount_description = view.findViewById(R.id.noncount_description);
        TextView noncount_result = view.findViewById(R.id.noncount_result);

        noncount_description.setText(nonCount.getNoncountDescription());
        noncount_result.setText(nonCount.getNoncountResult());

        return view;
    }
}


