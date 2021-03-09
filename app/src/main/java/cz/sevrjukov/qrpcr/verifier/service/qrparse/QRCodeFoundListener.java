package cz.sevrjukov.qrpcr.verifier.service.qrparse;

public interface QRCodeFoundListener {
    void onQRCodeFound(String qrCode);
}