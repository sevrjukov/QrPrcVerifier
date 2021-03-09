package cz.sevrjukov.qrpcr.verifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cz.sevrjukov.qrpcr.verifier.R;
import cz.sevrjukov.qrpcr.verifier.dto.DecodeResultDto;
import cz.sevrjukov.qrpcr.verifier.exception.QrCodeFormatException;
import cz.sevrjukov.qrpcr.verifier.service.decode.QrCodeDecoder;
import cz.sevrjukov.qrpcr.verifier.service.signature.SignCertificateProvider;
import cz.sevrjukov.qrpcr.verifier.service.signature.SignatureVerifier;

public class DisplayScanResultActivity extends AppCompatActivity {

    final ExecutorService executorService;

    public DisplayScanResultActivity() {
        executorService = Executors.newFixedThreadPool(4);
    }

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


    private void decodeAndDisplayData(String qrCodeData) {


        final QrCodeDecoder decoder = new QrCodeDecoder();
        try {
            final DecodeResultDto decodedData = decoder.decodeQrContent(qrCodeData);
            ((TextView) findViewById(R.id.decodedFirstname)).setText(decodedData.getFirstName());
            ((TextView) findViewById(R.id.decodedLastname)).setText(decodedData.getLastName());
            ((TextView) findViewById(R.id.decodedBirthnumber)).setText(decodedData.getBirthNumber());


            verifySignatureAsynch(decodedData);

        } catch (QrCodeFormatException ex) {
            //TODO display good message that QR code is invalid
            Log.e("decode", "bad QR code", ex);
        }


    }

    private void verifySignatureAsynch(final DecodeResultDto decodedData) {

        final SignCertificateProvider certificateProvider = new SignCertificateProvider(getResources());
        final SignatureVerifier signatureVerifier = new SignatureVerifier(certificateProvider);


        //TODO thread timeout
        executorService.submit(
                //TODO diffetentiate cases "bad signature" vs "error verifying"
                () -> {
                    try {
                        boolean result = signatureVerifier.verifySignature(decodedData.getSignature(),
                                decodedData.getSignedContent(),
                                decodedData.getKeyId());
                        setSignatureVerificationResult(result);
                    } catch (Exception ex) {
                        //TODO show reasonable exception
                        Log.e("decode", "failed to verify signature", ex);
                    }
                }
        );
    }

    private void setSignatureVerificationResult(boolean isSignatureOk) {
        CheckBox offlineVerifCheckbox = (CheckBox) findViewById(R.id.checkBoxOfflineVerify);
        if (isSignatureOk) {
            offlineVerifCheckbox.setText("offline overeni ok");
            offlineVerifCheckbox.setChecked(true);
        } else {
            offlineVerifCheckbox.setText("offline overeni selhalo");
            offlineVerifCheckbox.setChecked(false);
        }
    }


}