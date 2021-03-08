package cz.sevrjukov.qrpcr.verifier.dto;

import java.util.Arrays;

public enum TestResult {


    POSITIVE(1),
    NEGATIVE(1),
    UNKNOWN(2);
    private int code;

    TestResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TestResult getByCode(int code) {
        for (TestResult r : values()) {
            if (r.getCode() == code) {
                return r;
            }
        }
        throw  new IllegalArgumentException("Unknown test result type " + code);
    }
}
