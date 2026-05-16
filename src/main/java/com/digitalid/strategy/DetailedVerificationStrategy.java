package com.digitalid.strategy;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;

/**
 * returns thorough response.
 *
 * for organs tht need > a simple validity check -
 * e.g tax authorities tht check the identity has been valid
 * over a reporting period, not just at the moment of request.
 */
public class DetailedVerificationStrategy implements IdentityVerificationStrategy {

    @Override
    public VerificationResponse verify(DigitalID identity) {
        if (identity == null) {
            throw new IllegalArgumentException("Identity is required");
        }

        boolean currentlyActive = identity.getStatus() == Status.ACTIVE; // true only if status is ACTIVE rn
        boolean currentlySuspended = identity.getStatus() == Status.SUSPENDED; // true only if status is SUSPENDED rn

        String message;
        if (currentlyActive) {
            message = "Identity is active";
        } else if (currentlySuspended) {
            message = "X - Identity is currently suspended - X";
        } else {
            message = "XX - Identity is NOT currently valid - XX";
        }

        return VerificationResponse.builder()
                .valid(currentlyActive)
                .digitalIdNumber(identity.getDigitalIdNumber())
                .status(identity.getStatus())
                .message(message)
                .addInfo("currentlyActive", currentlyActive)
                .addInfo("currentlySuspended", currentlySuspended)
                .addInfo("createdDate", identity.getCreatedDate()) // when identity was created
                .addInfo("lastModifiedDate", identity.getLastModifiedDate()) // when identity was last changed
                .build();
    }
}
