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

public class CountAdapter extends ArrayAdapter<Count> {
    private int resourceLayout;
    private ArrayList<Count> countList;
    private Context context;

    public CountAdapter(Context context, int resource, ArrayList<Count> countList) {
        super(context, resource, countList);
        this.resourceLayout = resource;
        this.countList = countList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.count_content, parent, false);
        }
        Count count = countList.get(position);

        TextView count_description = view.findViewById(R.id.count_description);
        TextView count_result = view.findViewById(R.id.count_result);

        count_description.setText(count.getCountDescription());
        count_result.setText(count.getCountResult());

        return view;
    }
}
