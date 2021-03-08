package cz.sevrjukov.qrpcr.verifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayQrCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qr_code);

        Intent intent = getIntent();
        String qrCodeData  = intent.getStringExtra("qr_content");

        TextView qrCodeTextView =  (TextView) findViewById(R.id.textViewQr);
        qrCodeTextView.setText(qrCodeData);

    }


}