package com.digitalid.strategy;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;

public class LicensingVerificationStrategy implements IdentityVerificationStrategy {

    @Override
    public VerificationResponse verify(DigitalID identity) {
        if (identity == null) {
            throw new IllegalArgumentException("Identity is required");
        }
        boolean hasRestriction = identity.getStatus() == Status.SUSPENDED;
        boolean eligibleForLicence = identity.getStatus() == Status.ACTIVE && !hasRestriction;

        String message;
        if (eligibleForLicence) {
            message = "Identity is eligible for a licence";
        } else if (hasRestriction) {
            message = "Identity has an active restriction and is not eligible";
        } else {
            message = "Identity is not eligible (status: " + identity.getStatus() + ")";
        }

        return VerificationResponse.builder()
                .valid(eligibleForLicence)
                .digitalIdNumber(identity.getDigitalIdNumber())
                .status(identity.getStatus())
                .message(message)
                .addInfo("hasRestriction", hasRestriction)
                .addInfo("eligibleForLicence", eligibleForLicence)
                .build();
    }
}
