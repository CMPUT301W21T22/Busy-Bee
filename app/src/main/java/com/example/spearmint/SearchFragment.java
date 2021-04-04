package com.example.spearmint;

/**
 * Displays each experiment in a particular format to display the experiment owner, experiment title and experiment status.
 * A keyword search function is implemented.
 * Allows owner to browse questions/replies about an experiment when clicked.
 * Magnifying glass button must be pressed twice to enter search text.
 * https://stackoverflow.com/users/1703376/marurban, "RecyclerView onClick", 2015-07-28, Creative Commons Attribution-ShareAlike, https://stackoverflow.com/questions/24471109/recyclerview-onclick
 * 3.0license (CC BY-SA 3.0)
 * @author Daniel, Sandy, JiHo, Andrew
 */

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    Button count;

    // RecycleView(Added)
    RecyclerView aRecyclerView;
    RecycleAdapter aAdapter;
    RecyclerView.LayoutManager aLayoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //ArrayList(Added)
        ArrayList<ExperimentItem> experimentArrayList = new ArrayList<>();
        experimentArrayList.add(new ExperimentItem("Experiment D"));

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Experiments");

        /**
         * Updates the list stored locally in the app with Firebase data to display the data
         */
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                experimentArrayList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    String description = doc.getId();

                    experimentArrayList.add(new ExperimentItem(description));
                };

        aRecyclerView = rootView.findViewById(R.id.recycle_view);
        aLayoutManager = new LinearLayoutManager(getActivity());
        aRecyclerView.setHasFixedSize(true);
        aRecyclerView.setLayoutManager(aLayoutManager);
        aAdapter = new RecycleAdapter(experimentArrayList);
        aRecyclerView.setAdapter(aAdapter);

            }
        });

        /** https://stackoverflow.com/users/1703376/marurban, "RecyclerView onClick", 2015-07-28, Creative Commons Attribution-ShareAlike, https://stackoverflow.com/questions/24471109/recyclerview-onclick
         * 3.0license (CC BY-SA 3.0)
         * When clicked, redirects user to "ExperimentDetails.java"
         * To browse questions and replies
         */
        aAdapter = new RecycleAdapter(experimentArrayList);
        aAdapter.setOnItemClickListener(new RecycleAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Bundle experimentInfo = new Bundle();
                QuestionsAnswers detailsFragment = new QuestionsAnswers();
                String experimentName = experimentArrayList.get(position).getaTitle();

                experimentInfo.putString("dataKey", experimentName);
                detailsFragment.setArguments(experimentInfo);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);
                transaction.commit();
            }
            // Will implement at a later date
            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    /**
     * Allows user to use search bar to find experiments based on keywords
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (aAdapter != null) {
                    aAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

    }


}
