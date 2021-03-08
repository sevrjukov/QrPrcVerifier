package cz.sevrjukov.qrpcr.verifier.service;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;

public class QrCodeDecoderTest {


    @Test
    public void testDecode() {


        String res = QrCodeDecoder.extractSignedContent("A:B:C:123");
        Assert.assertEquals("not equal", "A:B:C:", res);
    }

}
