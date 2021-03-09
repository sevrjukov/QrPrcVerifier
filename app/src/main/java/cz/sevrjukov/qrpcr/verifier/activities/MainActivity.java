package cz.sevrjukov.qrpcr.verifier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cz.sevrjukov.qrpcr.verifier.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpButtonListeners();
    }

    private void setUpButtonListeners() {
        Button scanBtn = (Button) findViewById(R.id.btnMainScanQr);
        scanBtn.setOnClickListener(v -> launchScanActivity());
        Button manInputBtn = (Button) findViewById(R.id.btnMainManInput);
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


}