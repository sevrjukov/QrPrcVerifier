package cz.sevrjukov.qrpcr.verifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import cz.sevrjukov.qrpcr.verifier.dto.DecodeResultDto;
import cz.sevrjukov.qrpcr.verifier.service.QrCodeDecoder;

public class DisplayQrCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr_code);

        Intent intent = getIntent();
        String qrCodeData = intent.getStringExtra("qr_content");
        try {
            decodeAndDisplayData(qrCodeData);

        } catch (Exception ex) {
            Log.e("tag", "decoding failed", ex);
            //TODO fix this
            ((TextView) findViewById(R.id.decodedFirstname)).setText("Chyba");
        }

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