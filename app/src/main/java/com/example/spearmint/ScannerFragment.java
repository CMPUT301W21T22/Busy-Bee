package com.example.spearmint;

/**
 * Host fragment for fragments dealing scanning QR codes and barcodes.
 * When click on ScannerFragment, app will ask for camera permission on initial startup.
 * Camera will be turned on once user clicks into the camera space
 * @author Gavriel, Daniel
 *
 * Code Palace, "QR & Barcode Scanner App Tutorial in Android Studio", 2020-8-15
 * Creative Commons CC, https://www.youtube.com/watch?v=drH63NpSWyk&t=320s
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.api.Context;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.Code;
import com.google.zxing.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScannerFragment extends Fragment {

    private CodeScanner codeScanner;
    private boolean permissionGranted;
    private TextView textViewScan;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ScannerFragment() {
        // Required empty public constructor
    }

    /**
     * Expected behavior: ScannerFragment creates a fragment to scan for QR codes and bar codes
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScannerFragment.
     */
    public static ScannerFragment newInstance(String param1, String param2) {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Expected behavior: gets the arguments of the bundle that was passed.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Expected behavior: ScannerFragment is given access to Firebase to access experiments and trials that are needed
     * to generate QR codes/bar codes. Once a code is scan, the information will be shown below the camera interface.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view of ScannerFragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        ArrayList<String> dan = new ArrayList<>();
        Trial trial = new Trial("string one", "string two", "string three",dan);
        Experiment experiment = getArguments().getParcelable("dataKey");
        String exDescription = experiment.getExperimentDescription();

        final CollectionReference collectionReference = db.collection("Experiments").document(exDescription).collection("Trials");

        setupPermissions();
        final Activity activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        textViewScan = (TextView) view.findViewById(R.id.scanner_text);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(activity, scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        collectionReference
                                .document(trial.getTrialDescription())
                                .set(trial);
//                                .set(result.getText());

                        textViewScan.setText(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
        return view;
    }

    /**
     * Expected behavior: if permissionGranted is true, start preview of camera
     */
    @Override
    public void onResume() {
        super.onResume();
        if (permissionGranted) {
            codeScanner.startPreview();
        }
    }

    /**
     * Expected behavior: camera stops behaving when this fragment is not active
     */
    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    /**
     * Expected behavior: asks permission from the user to allow camera permissions from this app
     */
    public void setupPermissions() {
        final Activity activity = getActivity();
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    /**
     * Expected behavior: request pop up shows the user of camera permission
     */
    public void makeRequest() {
        final Activity activity = getActivity();
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
    }

    /**
     * Expected behavior: if no permission granted, a message will show; if not, fragment behavior will continue
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final Activity activity = getActivity();
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    codeScanner.startPreview();
                    Toast.makeText(activity, "Camera permission needed to use this app", Toast.LENGTH_SHORT).show();
                } else {
                    permissionGranted = false;
                    // success
                }
        }
    }
}