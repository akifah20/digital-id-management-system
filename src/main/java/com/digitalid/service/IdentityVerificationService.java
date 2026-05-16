package com.digitalid.service;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.OrganisationType;
import com.digitalid.domain.VerificationResponse;
import com.digitalid.exception.IdentityNotFoundException;
import com.digitalid.repository.IdentityRepository;
import com.digitalid.strategy.DetailedVerificationStrategy;
import com.digitalid.strategy.IdentityVerificationStrategy;
import com.digitalid.strategy.LicensingVerificationStrategy;
import com.digitalid.strategy.ValidityCheckStrategy;

public class IdentityVerificationService {

    private final IdentityRepository repository;
    private final AuthorizationService authorizationService;

    private final IdentityVerificationStrategy validityStrategy;
    private final IdentityVerificationStrategy detailedStrategy;
    private final IdentityVerificationStrategy licensingStrategy;

    /**
     * 
     * Constructs the IdentityVerificationService with required dependencies
     * + initialises default verification strategies.
     *
     * @param repository           stores + retrieves ID records
     * @param authorizationService handles verification permissions
     */
    public IdentityVerificationService(IdentityRepository repository,
            AuthorizationService authorizationService) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required!!");
        }
        if (authorizationService == null) {
            throw new IllegalArgumentException("AuthorizationService is required!!!");
        }
        this.repository = repository;
        this.authorizationService = authorizationService;

        this.validityStrategy = new ValidityCheckStrategy();
        this.detailedStrategy = new DetailedVerificationStrategy();
        this.licensingStrategy = new LicensingVerificationStrategy();
    }

    /**
     * Verifies an ID for a requesting organisation.
     * 
     * @param digitalIdNumber the unique ID n.o to verify
     * @param requestor       the organisation requesting verification
     * @return a verification response tailored to the organisation type
     * @throws com.digitalid.exception.AuthorizationException if the requestor is
     *                                                        not permitted to
     *                                                        verify
     * @throws IdentityNotFoundException                      if no ID exists for
     *                                                        the given n.o
     */
    public VerificationResponse verify(String digitalIdNumber, Organisation requestor) {
        authorizationService.checkCanVerify(requestor); // Authorise request
        DigitalID identity = repository.findById(digitalIdNumber) // Find ID
                .orElseThrow(() -> IdentityNotFoundException.forIdNumber(digitalIdNumber));

        IdentityVerificationStrategy strategy = selectStrategy(requestor.getType()); // Pick right strategy + run it
        return strategy.verify(identity);
    }

    // selects correct start for given org type
    private IdentityVerificationStrategy selectStrategy(OrganisationType type) {
        switch (type) {
            case TAX_AUTHORITY:
                return detailedStrategy;
            case DRIVING_LICENCE_AUTHORITY:
                return licensingStrategy;
            default:
                return validityStrategy;
        }
    }
}
