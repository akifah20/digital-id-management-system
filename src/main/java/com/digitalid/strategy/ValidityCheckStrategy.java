package com.digitalid.strategy;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.VerificationResponse;

public class ValidityCheckStrategy implements IdentityVerificationStrategy {

    @Override
    public VerificationResponse verify(DigitalID identity) {
        if (identity == null) {
            throw new IllegalArgumentException("Identity is required");
        }

        if (identity.isValid()) {
            return VerificationResponse.builder()
                    .valid(true)
                    .digitalIdNumber(identity.getDigitalIdNumber())
                    .status(identity.getStatus())
                    .message("Identity is currently valid")
                    .build();
        }

        return VerificationResponse.builder()
                .valid(false)
                .digitalIdNumber(identity.getDigitalIdNumber())
                .status(identity.getStatus())
                .message("Identity is not currently valid")
                .build();
    }
}
