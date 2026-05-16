package com.digitalid.strategy;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.VerificationResponse;

/**
 * Contract - a verification response from an ID.
 * links to:
 * ValiditycheckStrategy - yes/no
 * DetailedVerificationStrategy - suspension info for tax
 * authorities
 * LicensingVerificationStrategy - restriction info for
 * licensing
 */
public interface IdentityVerificationStrategy {

    VerificationResponse verify(DigitalID identity); // ver response for given ID.
}
