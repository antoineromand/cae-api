package com.pickandeat.api.shared;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ErrorApiResponse {
    private final String code;
    private final String message;
    private final Instant timestamp;
    private final int statusCode;
    private final Map<String, List<String>> multipleErrors;

    public ErrorApiResponse(String code, String message, int statusCode, Map<String, List<String>> multipleErrors) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
        this.multipleErrors = multipleErrors;
        this.timestamp = Instant.now();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, List<String>> getMultipleErrors() {
        return multipleErrors;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
