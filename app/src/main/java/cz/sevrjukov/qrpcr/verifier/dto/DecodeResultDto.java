package cz.sevrjukov.qrpcr.verifier.dto;

import java.time.LocalDateTime;

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

    private String testTime;
    private TestResult result;
    private String signature;
    private boolean signatureOk;

}
