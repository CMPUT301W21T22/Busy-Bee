package com.example.spearmint;

/**
 * Custom adapter that processes and stores ExperimentItem objects in a custom list
 * @author Andrew, Jiho, Daniel
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> implements Filterable {
    private ArrayList<Experiment> aArrayList;
    private ArrayList<Experiment> copyArrayList;

    private static ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView name;
        public TextView aTitle;
        public TextView username;
        public TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            name = (TextView) itemView.findViewById(R.id.recycle_view);
            aTitle = itemView.findViewById(R.id.experiment_title);
            username = itemView.findViewById(R.id.owner_username);
            status = itemView.findViewById(R.id.status);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecycleAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public RecycleAdapter(ArrayList<Experiment> arrayList){
        aArrayList = arrayList;
        copyArrayList = new ArrayList<>(arrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.experiment_cardholder,parent,false);
        ViewHolder aViewHolder = new ViewHolder(view);
        return aViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Experiment currentItem = aArrayList.get(position);

        holder.aTitle.setText(currentItem.getExperimentDescription());
        holder.username.setText("Owner: " + currentItem.getExperimentOwner().get(1));
        holder.status.setText("Status: " + currentItem.getStatus());
    }

    @Override
    public int getItemCount() {
        return aArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return aFilter;
    }
    private Filter aFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Experiment>  filteredList = new ArrayList<>();
            if(constraint == null || constraint.length()== 0){
                filteredList.addAll(copyArrayList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Experiment item: copyArrayList){
                    if(item.getExperimentDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            aArrayList.clear();
            aArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}