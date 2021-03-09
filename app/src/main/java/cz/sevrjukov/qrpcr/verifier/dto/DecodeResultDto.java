package cz.sevrjukov.qrpcr.verifier.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class DecodeResultDto {

    private String firstName;
    private String lastName;
    private String birthNumber;

    private String testFacilityId;
    private String testId;

    private Date testTime;
    private TestResult result;
    private int keyId;
    private byte [] signedContent;
    private byte[] signature;

}
