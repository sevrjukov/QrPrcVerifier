package cz.sevrjukov.qrpcr.verifier.service.signature;

import java.security.Signature;

public class SignatureVerifier {

    private SignCertificateProvider certificateProvider;

    public SignatureVerifier(SignCertificateProvider certificateProvider) {
        this.certificateProvider = certificateProvider;
    }

    public boolean verifySignature(byte[] signature, byte[] contentToVerify, int keyId) throws Exception {
        Signature s = Signature.getInstance("SHA256withECDSA");
        s.initVerify(certificateProvider.getPublicKey(keyId));
        s.update(contentToVerify);
        return s.verify(signature);
    }

}
