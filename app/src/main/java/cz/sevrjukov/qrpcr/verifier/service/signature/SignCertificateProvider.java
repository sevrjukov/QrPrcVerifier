package cz.sevrjukov.qrpcr.verifier.service.signature;

import android.content.res.Resources;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;

import cz.sevrjukov.qrpcr.verifier.R;

public class SignCertificateProvider {


    private Resources resources;

    public SignCertificateProvider(Resources resources) {
        this.resources = resources;
    }

    public PublicKey getPublicKey(int keyId) throws Exception {
        InputStream is = resources.openRawResource(R.raw.qr_test_keystore_ec);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(is, "password".toCharArray());
        Certificate cert = keyStore.getCertificate("signerKeyPair");
        return cert.getPublicKey();
    }
}
