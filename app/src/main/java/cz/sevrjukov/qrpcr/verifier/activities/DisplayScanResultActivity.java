package cz.sevrjukov.qrpcr.verifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import cz.sevrjukov.qrpcr.verifier.R;
import cz.sevrjukov.qrpcr.verifier.dto.DecodeResultDto;
import cz.sevrjukov.qrpcr.verifier.service.QrCodeDecoder;

public class DisplayScanResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        setUpButtonListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();

        final Intent intent = getIntent();
        String qrCodeData = intent.getStringExtra("qr_content");
        try {
            decodeAndDisplayData(qrCodeData);
        } catch (Exception ex) {
            Log.e("tag", "decoding failed", ex);
            //TODO fix this
            ((TextView) findViewById(R.id.decodedFirstname)).setText("Chyba");
        }
    }

    private void setUpButtonListeners() {
        Button scanBtn = (Button) findViewById(R.id.btnScanResScanNext);
        scanBtn.setOnClickListener(v -> launchScanActivity());
        Button manInputBtn = (Button) findViewById(R.id.btnScanResManInput);
        manInputBtn.setOnClickListener(v -> launchManualInputActivity());
    }


    private void launchScanActivity() {
        final Intent intent = new Intent(this, ScanQrCodeActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }


    private void launchManualInputActivity() {
        //TODO implement
    }


    private void decodeAndDisplayData(String qrCodeData) throws Exception {


        QrCodeDecoder decoder = new QrCodeDecoder(getResources());
        DecodeResultDto decodedData = decoder.decodeQrContent(qrCodeData);

        ((TextView) findViewById(R.id.decodedFirstname)).setText(decodedData.getFirstName());
        ((TextView) findViewById(R.id.decodedLastname)).setText(decodedData.getLastName());
        ((TextView) findViewById(R.id.decodedBirthnumber)).setText(decodedData.getBirthNumber());

        CheckBox offlineVerifCheckbox =  (CheckBox) findViewById(R.id.checkBoxOfflineVerify);
        if (decodedData.isSignatureOk()) {
            offlineVerifCheckbox.setText("offline overeni ok");
            offlineVerifCheckbox.setChecked(true);
        } else {
            offlineVerifCheckbox.setText("offline overeni selhalo");
            offlineVerifCheckbox.setChecked(false);
        }

    }


}