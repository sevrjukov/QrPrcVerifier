package cz.sevrjukov.qrpcr.verifier.service;

import android.content.Context;
import android.content.res.Resources;

import org.apache.commons.codec.binary.Hex;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

import cz.sevrjukov.qrpcr.verifier.R;
import cz.sevrjukov.qrpcr.verifier.dto.DecodeResultDto;
import cz.sevrjukov.qrpcr.verifier.dto.TestResult;

public class QrCodeDecoder {

    private Resources resources;

    public QrCodeDecoder(Resources resources) {
        this.resources = resources;
    }


    public DecodeResultDto decodeQrContent(String qrContent) throws Exception {

        String[] parts = qrContent.split(":");


        /* 0 VACLAV:
         1  CERVENOKOSTELECKY:
         2 7256890245: rodne cislo
        3 85050: laborator
        4  100062565: cislo testu
        5 1615227023: datum a cas
        6 1: vysledek
       7 signature   */

        //TODO udelat decoder vic smart, pocitat kolik je tokenu, vyhazovat ruzne exceptiony podle toho co se nepovedlo

        byte[] signature = Hex.decodeHex(parts[7]);
        byte[] contentToVerify = extractSignedContent(qrContent).getBytes(StandardCharsets.UTF_8);
        boolean signatureOk = verifySignature(signature, contentToVerify);

        final DecodeResultDto result = DecodeResultDto
                .builder()
                .firstName(parts[0])
                .lastName(parts[1])
                .birthNumber(parts[2])
                .testFacilityId(parts[3])
                .testId(parts[4])
                .testTime(parts[5])
                .result(TestResult.getByCode(Integer.parseInt(parts[6])))
                .signature(parts[7])
                .signatureOk(signatureOk)
                .build();
        return result;
    }


    public static String extractSignedContent(String qrCodeContent) {
        int pos = qrCodeContent.lastIndexOf(":");
        return qrCodeContent.substring(0, pos + 1);
    }

    private boolean verifySignature(byte[] signature, byte[] contentToVerify) throws Exception {
        Signature s = Signature.getInstance("SHA256withECDSA");
        s.initVerify(loadPublicKey(0));
        s.update(contentToVerify);
        return s.verify(signature);
    }


    private PublicKey loadPublicKey(int keyId) throws Exception {
        InputStream is = resources.openRawResource(R.raw.qr_test_keystore_ec);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(is, "password".toCharArray());
        Certificate cert = keyStore.getCertificate("signerKeyPair");
        return cert.getPublicKey();
    }


}
