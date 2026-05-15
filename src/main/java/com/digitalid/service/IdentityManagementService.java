package com.digitalid.service;

import java.time.LocalDate;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.repository.IdentityRepository;

//Service tht manages the ID life-cycle (creates updates and changes status of IDs)
/**
 * This service coordinates the repository, validation, and authorisation
 * services. It receives its dependencies via the constructor (Dependency
 * Injection), so it can be tested with fake dependencies and the storage
 * layer can be swapped without changing this service.
 */
public class IdentityManagementService {

    private final IdentityRepository repository;
    private final ValidationService validationService;
    private final AuthorizationService authorizationService;

    /**
     * Constructs an IdentityManagementService with its dependencies.
     *
     * @param repository           where IDs are stored
     * @param validationService    imposes business validation rules
     * @param authorizationService imposes org permissions
     */
    public IdentityManagementService(IdentityRepository repository,
            ValidationService validationService,
            AuthorizationService authorizationService) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required");
        }
        if (validationService == null) {
            throw new IllegalArgumentException("ValidationService is required");
        }
        if (authorizationService == null) {
            throw new IllegalArgumentException("AuthorizationService is required");
        }
        this.repository = repository;
        this.validationService = validationService;
        this.authorizationService = authorizationService;
    }

    // Creates a new ID. starts with ACTIVE status + a generated ID n.o.
    public DigitalID createIdentity(String firstName, String lastName,
            LocalDate dateOfBirth, Organisation requestor) {
        authorizationService.checkCanCreate(requestor); // Check that requestor is allowed to create

        validationService.validateIdentityData(firstName, lastName, dateOfBirth);

        DigitalID identity = new DigitalID(firstName, lastName, dateOfBirth); // Create the ID
        repository.save(identity); // saves itt

        return identity;
    }
}
