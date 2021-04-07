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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.example.spearmint.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * ProfileFragment handles user's activities such as editing username, email, and phone number.
 * ProFileFragment also generates a unique ID for the user.
 * @author Gavriel and Michael
 * @see User
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PHONE_NUM = "PhoneNum";

    private static final String KEY_EXPERIMENT = null;
    private static final String KEY_COUNT = null;
    private static final String KEY_REGION = null;

    private static final String SHARED_PREFS = "SharedPrefs";
    private static final String TEXT = "Text";
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextNumber;
    private Button saveProfileBtn;
    private User currentUser = new User();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("User");

    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Expected behavior: ProfileFragment creates the profile "page" for the user
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
    /**
     * Expected behavior: onCreateView sets the text of the user's input onto their profile to view
     * their profile. This method will store a unique ID into sharedPreferences and the user is able
     * to set their username, email, and phone number on their profile which will eventually be saved
     * onto Firebase.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view of this fragment
     *
     * advantej, "findViewById in Fragment", 2011-06-27, Creative Commons Attribution-ShareAlike
     * 3.0license (CC BY-SA 3.0), https://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
     *
     * Oum Saokosal, "How to Use findViewById in Fragment in Android - Navigation Drawer",
     * 2017-3-29, Creative Commons CC, https://www.youtube.com/watch?v=fF8f3BDDudo
     *
     * Jignesh mayani, "How to use documentReference in fragment on android studio", 2020-02-24,
     * Creative Common Attribution-ShareAlike 3.0license (CC BY-SA 3.0),
     * https://stackoverflow.com/questions/60372975/how-to-use-documentreference-in-fragment-on-android-studio
     */
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);
        DocumentReference documentReference = collectionReference.document(uniqueID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                editTextUsername.setText(doc.getString("Username"));
                editTextEmail.setText(doc.getString("Email"));
                editTextNumber.setText(doc.getString("PhoneNum"));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /**
     * Expected behavior: createUniqueID creates a random unique ID that will be later assigned to
     * the user
     * This method does not take any arguments.
     * @return uniqueID
     *
     * Fahim, "how to create random UUID in Android when button click event happens?", 2015-02-27,
     * Creative Common Attribution-ShareAlike 3.0license (CC BY-SA 3.0),
     * https://stackoverflow.com/questions/28770408/how-to-create-random-uuid-in-android-when-button-click-event-happens
     */
    public String createUniqueID() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    /**
     * Expected behavior: storeUniqueID checks whether a uniqueID exists in sharedPreferences or not.
     * SharedPreferences will store the string uniqueID. If a uniqueID exists in sharedPreferences,
     * then continue. If a uniqueID does not exist in sharedPreferences, then create a uniqueID for
     * the user and store that ID onto Firebase as a document and set the currentUser's UUID to that
     * ID.
     * This method takes a Context as an argument, it cannot take null as an argument.
     * @param activity
     * @return uniqueID
     *
     * Coding in Flow, "How to Save Variables in SharedPreferences - Android Studio Tutorial",
     * 2017-10-7, Creative Commons CC, https://www.youtube.com/watch?v=fJEFZ6EOM9o
     *
     * Jug6ernaut, "Android SharedPreferences in Fragment", 2012-07-31, Creative Commons
     * Attribution-ShareAlike 3.0license (CC BY-SA 3.0),
     * https://stackoverflow.com/questions/11741270/android-sharedpreferences-in-fragment
     */
    public String storeUniqueID(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String uniqueID = sharedPreferences.getString(TEXT, null);
        if (uniqueID != null) {


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

    /**
     * Expected behavior: uniqueIDToFirebase takes in a string that will be stored as a document in
     * the collection of "User" onto Firebase.
     * This method only takes in a string as a valid argument and nothing else.
     * @param uniqueID
     * @return void
     *
     * Aaron Liu, "Lab 5", 2021-02-24, Public Domain,
     * https://drive.google.com/file/d/1e8W1sG8RCOcEOsV01nFJp3vaMqxQf7og/view?usp=sharing
     */
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

    /**
     * Expected behavior: this method stores the user's username, email, and phone number onto
     * Firebase. Users can only save their information onto Firebase once they have their username,
     * email, and phone number filled into the textboxes.
     * This method takes no arguments.
     * @return void
     *
     * Aaron Liu, "Lab 5", 2021-02-24, Public Domain,
     * https://drive.google.com/file/d/1e8W1sG8RCOcEOsV01nFJp3vaMqxQf7og/view?usp=sharing
     */
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




    /**
     * Expected behavior: this method keeps track of what a button's ID will behave when selected.
     * @param v
     * @return void
     *
     * Francesc, "multiple buttons in fragment, how to redirect to a different layout", 2016-03-04,
     * Creative Common Attribution-ShareAlike 3.0license (CC BY-SA 3.0),
     * https://stackoverflow.com/questions/35788127/multiple-buttons-in-fragment-how-to-redirect-to-a-different-layout
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_profile:
                storeProfileToFirebase();
                break;
        }
    }
}