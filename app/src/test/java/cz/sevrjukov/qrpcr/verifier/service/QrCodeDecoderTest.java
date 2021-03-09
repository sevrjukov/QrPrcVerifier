package cz.sevrjukov.qrpcr.verifier.service;

import org.junit.Assert;
import org.junit.Test;

import cz.sevrjukov.qrpcr.verifier.service.decode.QrCodeDecoder;

public class QrCodeDecoderTest {


    @Test
    public void testDecode() {


        String res = QrCodeDecoder.extractSignedContent("A:B:C:123");
        Assert.assertEquals("not equal", "A:B:C:", res);
    }

}
