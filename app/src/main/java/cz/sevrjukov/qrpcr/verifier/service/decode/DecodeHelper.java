package cz.sevrjukov.qrpcr.verifier.service.decode;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Date;

import cz.sevrjukov.qrpcr.verifier.dto.TestResult;
import cz.sevrjukov.qrpcr.verifier.exception.QrCodeFormatException;

import static cz.sevrjukov.qrpcr.verifier.service.decode.QrCodeFormat.SEPARATOR;

public final class DecodeHelper {

    private DecodeHelper() {
        // prevent instantiation
    }

    public static void validateParsedTokens(final String[] tokens) throws QrCodeFormatException {
        if (tokens == null || tokens.length == 0) {
            throw new QrCodeFormatException("Zero tokens found");
        }
        if (tokens.length != QrCodeFormat.NUM_FIELDS) {
            throw new QrCodeFormatException("Incorrect fields count. Expected " + QrCodeFormat.NUM_FIELDS + " but found " + tokens.length);
        }
        int version = decodeInt(tokens, QrCodeFormat.INDEX_VERSION);
        if (version != QrCodeFormat.VERSION) {
            throw new QrCodeFormatException("Wrong QR code version " + version);
        }

    }

    public static String decodeStr(final String[] tokens, int index) throws QrCodeFormatException {
        try {
            String result = tokens[index];
            if (result == null) {
                throw new QrCodeFormatException("Null string at index " + index);
            }
            result = result.trim();
            if (result.isEmpty()) {
                throw new QrCodeFormatException("Empty string at index " + index);
            }
            return result;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new QrCodeFormatException("Invalid token index " + index);
        }
    }

    public static int decodeInt(final String[] tokens, int index) throws QrCodeFormatException {
        try {
            return Integer.parseInt(decodeStr(tokens, index));
        } catch (NumberFormatException nex) {
            throw new QrCodeFormatException("Invalid integer on position index " + index);
        }
    }

    public static long decodeLong(final String[] tokens, int index) throws QrCodeFormatException {
        try {
            return Long.parseLong(decodeStr(tokens, index));
        } catch (NumberFormatException nex) {
            throw new QrCodeFormatException("Invalid integer on position index " + index);
        }
    }

    public static Date decodeDate(final String[] tokens, int index) throws QrCodeFormatException {
        return new Date(decodeLong(tokens, index));
    }

    public static TestResult decodeTestResult(final String[] tokens, int index) throws QrCodeFormatException {
        int code = decodeInt(tokens, index);
        try {
            return TestResult.getByCode(code);
        } catch (IllegalArgumentException ex) {
            throw new QrCodeFormatException("Unknown test result at position " + index);
        }
    }

    public static byte[] decodeByteArray(final String[] tokens, int index) throws QrCodeFormatException {
        try {
            String str = decodeStr(tokens, index);
            return Hex.decodeHex(str);
        } catch (DecoderException dex) {
            throw new QrCodeFormatException("Invalid byte array at position " + index);
        }
    }

    public static String extractSignedContent(String qrCodeContent) {
        int pos = qrCodeContent.lastIndexOf(SEPARATOR);
        return qrCodeContent.substring(0, pos + 1);
    }
}
