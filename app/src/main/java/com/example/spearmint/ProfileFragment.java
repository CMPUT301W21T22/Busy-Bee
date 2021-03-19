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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.example.spearmint.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * ProfileFragment handles user's activities such as editing username, email, and phone number.
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PHONE_NUM = "PhoneNum";

    public static final String SHARED_PREFS = "SharedPrefs";
    public static final String TEXT = "Text";
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextNumber;
    private Button saveProfileBtn;
    private User currentUser = new User();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("User");
//    private DocumentReference userReference = db.document(currentUser.getUUID());

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
        editTextNumber = (EditText) view.findViewById(R.id.edit_text_phone);
        saveProfileBtn = (Button) view.findViewById(R.id.button_save_profile);
        saveProfileBtn.setOnClickListener(this);

        editTextUsername.setText(currentUser.getUsername());
        editTextEmail.setText(currentUser.getEmail());
        editTextNumber.setText(currentUser.getNumber());
//        setTexFromFirebase();

        return view;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    // Creates unique ID
    public String createUniqueID() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    //  Checks if unique ID exists in SharedPreferences, if not create a unique ID
    public String storeUniqueID(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);
        // If unique ID exists and if there is no UUID to a user, do nothing
        if (uniqueID != null && currentUser.getUUID() == null) {
            //
        // If unique ID does not exist in sharedPreferences, create one, store it in sharedPreferences,
        // and attach the ID to currentUser
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String ID = createUniqueID();
            editor.putString(TEXT, ID);
            editor.apply();
            uniqueIDToFirebase(ID);
            currentUser.setUUID(ID);
        }
        return uniqueID;
    }

    // Store currentUser's UUID to Firebase as a document
    public void uniqueIDToFirebase(String uniqueID) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_USERNAME, null);
        user.put(KEY_EMAIL, null);
        user.put(KEY_PHONE_NUM, null);

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
    // Stores user's username, email, and phone number onto Firebase
    public void storeProfileToFirebase() {
        Map<String, Object> user = new HashMap<>();

        currentUser.setUsername(editTextUsername.getText().toString());
        currentUser.setEmail(editTextEmail.getText().toString());
        currentUser.setNumber(editTextNumber.getText().toString());

        if (currentUser.getUsername().length() > 0 && currentUser.getEmail().length() > 0 && currentUser.getNumber().length() > 0) {
            user.put(KEY_USERNAME, currentUser.getUsername());
            user.put(KEY_EMAIL, currentUser.getEmail());
            user.put(KEY_PHONE_NUM, currentUser.getNumber());
            collectionReference
                    .document(storeUniqueID(getContext()))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Username, email, and phone number saved");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Username, email, and phone number not saved" + e.toString());
                        }
                    });
        }
    }

    // Not completed yet, does not display user's input after edit (error)
    // Displays user's username, email, and phone number onto the text box after editing,
    // by retrieving data from Firebase that the user has saved to Firebase
    public void setTextFromFirebase() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_profile:
                storeProfileToFirebase();
                break;
        }
    }
}