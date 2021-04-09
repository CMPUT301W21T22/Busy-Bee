package com.example.spearmint;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeFragment extends Fragment {

    private EditText barcodeNum;
    private ImageView qrcodeImage;
    private Button qrcodeButton;
    private Button barcodeButton;
    private ImageView barcodeImage;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    RelativeLayout relativeLayout;

    private static final String SHARED_PREFS = "SharedPrefs";
    private static final String TEXT = "Text";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.qrcode_generator, container, false);

        barcodeNum = view.findViewById(R.id.barcode_number);
        barcodeButton = view.findViewById(R.id.barcode_button);
        qrcodeImage = view.findViewById(R.id.qrcode_image);
        qrcodeButton = view.findViewById(R.id.qrcode_button);
        barcodeImage = view.findViewById(R.id.barcode_image);
        relativeLayout = view.findViewById(R.id.qrcode_layout);

        Trial trial = getArguments().getParcelable("dataKey");
        String result = trial.getTrialResult();

        /**
         * Creates a QR Code when the "Generate QR Code" button is clicked
         * https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
         */

        qrcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Point point = new Point();
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3/4;

                qrgEncoder = new QRGEncoder(result, null, QRGContents.Type.TEXT, smallerDimension);
                bitmap = qrgEncoder.getBitmap();
                qrcodeImage.setImageBitmap(bitmap);

            }
        });


        /**
         * Creates a Barcode when the User inputs numbers in to the EditText and clicks on the "Generate Barcode" button
         * http://learningprogramming.net/mobile/android/create-and-scan-barcode-in-android/
         */
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String numId = barcodeNum.getText().toString();
                    Writer codeWriter;
                    codeWriter = new Code128Writer();
                    BitMatrix byteMatrix = codeWriter.encode(numId, BarcodeFormat.CODE_128, 270, 100);
                    int width = byteMatrix.getWidth();
                    int height = byteMatrix.getHeight();
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    barcodeImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return relativeLayout;
    }
}
