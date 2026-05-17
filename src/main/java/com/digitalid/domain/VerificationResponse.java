package com.digitalid.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VerificationResponse {

    private final boolean valid;
    private final String digitalIdNumber;
    private final Status status;
    private final String message;
    private final LocalDateTime verificationTime;
    private final Map<String, Object> additionalInfo;

    private VerificationResponse(Builder builder) {
        this.valid = builder.valid;
        this.digitalIdNumber = builder.digitalIdNumber;
        this.status = builder.status;
        this.message = builder.message;
        this.verificationTime = LocalDateTime.now();
        this.additionalInfo = Collections.unmodifiableMap(new HashMap<>(builder.additionalInfo));
    }

    public boolean isValid() {
        return valid;
    }

    public String getDigitalIdNumber() {
        return digitalIdNumber;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getVerificationTime() {
        return verificationTime;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static VerificationResponse valid(String digitalIdNumber, Status status) {
        return builder()
                .valid(true)
                .digitalIdNumber(digitalIdNumber)
                .status(status)
                .message("Identity is valid")
                .build();
    }

    public static VerificationResponse invalid(String digitalIdNumber, String reason) {
        return builder()
                .valid(false)
                .digitalIdNumber(digitalIdNumber)
                .message(reason)
                .build();
    }

    @Override
    public String toString() {
        return String.format("VerificationResponse{valid=%s, id='%s', status=%s, message='%s'}",
                valid, digitalIdNumber, status, message);
    }

    public static class Builder {
        private boolean valid;
        private String digitalIdNumber;
        private Status status;
        private String message;
        private final Map<String, Object> additionalInfo = new HashMap<>();

        public Builder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        public Builder digitalIdNumber(String digitalIdNumber) {
            this.digitalIdNumber = digitalIdNumber;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder addInfo(String key, Object value) {
            this.additionalInfo.put(key, value);
            return this;
        }

        public VerificationResponse build() {
            return new VerificationResponse(this);
        }
    }
}
