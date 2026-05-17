package com.digitalid.strategy;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.VerificationResponse;

public interface IdentityVerificationStrategy {

    VerificationResponse verify(DigitalID identity);
}
