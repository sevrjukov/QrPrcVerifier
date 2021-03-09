package cz.sevrjukov.qrpcr.verifier.service.decode;


import java.nio.charset.StandardCharsets;
import java.util.Date;

import cz.sevrjukov.qrpcr.verifier.dto.DecodeResultDto;
import cz.sevrjukov.qrpcr.verifier.dto.TestResult;
import cz.sevrjukov.qrpcr.verifier.exception.QrCodeFormatException;

import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.decodeByteArray;
import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.decodeDate;
import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.decodeInt;
import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.decodeStr;
import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.decodeTestResult;
import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.extractSignedContent;
import static cz.sevrjukov.qrpcr.verifier.service.decode.DecodeHelper.validateParsedTokens;
import static cz.sevrjukov.qrpcr.verifier.service.decode.QrCodeFormat.SEPARATOR;

public class QrCodeDecoder {


    public DecodeResultDto decodeQrContent(String qrContent) throws QrCodeFormatException {

        final String[] parts = qrContent.split(SEPARATOR);

        validateParsedTokens(parts);

        final String firstName = decodeStr(parts, QrCodeFormat.INDEX_FIRSNAME);
        final String lastName = decodeStr(parts, QrCodeFormat.INDEX_LASTNAME);
        final String birthNum = decodeStr(parts, QrCodeFormat.INDEX_BIRTHNUM);
        final String labId = decodeStr(parts, QrCodeFormat.INDEX_LAB_ID);
        final String testId = decodeStr(parts, QrCodeFormat.INDEX_TEST_ID);
        final Date testTime = decodeDate(parts, QrCodeFormat.INDEX_TEST_TIME);
        final TestResult testResult = decodeTestResult(parts, QrCodeFormat.INDEX_TEST_RESULT);
        final int keyId = decodeInt(parts, QrCodeFormat.INDEX_KEY_ID);
        final byte[] signature = decodeByteArray(parts, QrCodeFormat.INDEX_SIGNATURE);
        final byte[] signedContent = extractSignedContent(qrContent).getBytes(StandardCharsets.UTF_8);

        final DecodeResultDto result = DecodeResultDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthNumber(birthNum)
                .testFacilityId(labId)
                .testId(testId)
                .testTime(testTime)
                .result(testResult)
                .keyId(keyId)
                .signedContent(signedContent)
                .signature(signature)
                .build();
        return result;
    }

}
