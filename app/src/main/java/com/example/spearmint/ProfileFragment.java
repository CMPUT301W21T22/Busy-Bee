package com.example.spearmint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private final String SAMPLE = "Sample";

    private EditText editTextUsername;
    private EditText editTextEmail;
    private Button saveProfileBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("User");


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editTextUsername = (EditText) view.findViewById(R.id.edit_text_username);
        editTextEmail = (EditText) view.findViewById(R.id.edit_text_email);
        saveProfileBtn = (Button) view.findViewById(R.id.button_save_profile);
        saveProfileBtn.setOnClickListener(this);

        editTextUsername.setHint("Username");
        editTextEmail.setHint("Email");

        return view;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    // Create unique ID
    public String createUniqueID() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    //  Checks if unique ID exists in SharedPreferences, if not create a unique ID
    public String storeUniqueID(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);
        // if unique ID already exists in sharedPreferences, print statement
        if (uniqueID != null) {
            // do nothing
        // if unique ID does not exists in sharedPreferences, create one and store it in sharedPreferences
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String ID = createUniqueID();
            editor.putString(TEXT, ID);
            editor.apply();
            uniqueIDToFirebase(ID);
        }
        return uniqueID;
    }

    public void uniqueIDToFirebase(String uniqueID) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_USERNAME, null);
        user.put(KEY_EMAIL, null);

        db.collection("User").document(uniqueID).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Profile saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error!");
                    }
                });
    }

    // Saves user's username and email to Firebase
    @Override
    public void onClick(View v) {
        Map<String, Object> user = new HashMap<>();
        final String username = editTextUsername.getText().toString();
        final String email = editTextEmail.getText().toString();

        if (username.length() > 0 && email.length() > 0) {
            user.put(KEY_USERNAME, username);
            user.put(KEY_EMAIL, email);
            collectionReference
                    .document(storeUniqueID(getContext()))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Username and email saved");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Username and email not saved" + e.toString());
                        }
                    });
        }
    }
}