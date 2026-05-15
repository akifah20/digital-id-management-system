package com.digitalid.service;

import java.time.LocalDate;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.Status;
import com.digitalid.exception.IdentityNotFoundException;
import com.digitalid.repository.IdentityRepository;

//Service tht manages the ID life-cycle (creates updates & changes status of IDs)
/**
 * This service coordinates the repo, validation, & authorisation
 * services. It receives its dependencies via the constructor (Dependency
 * Injection), so it can be tested with fake dependencies.
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

    // Updates first & last name of an existing ID.
    // Both fields are optional - when empty it leaves the field unchanged.
    public DigitalID updateName(String digitalIdNumber, String firstName, String lastName,
            Organisation requestor) {
        authorizationService.checkCanUpdate(requestor);

        DigitalID identity = repository.findById(digitalIdNumber)
                .orElseThrow(() -> IdentityNotFoundException.forIdNumber(digitalIdNumber));

        validationService.validateUpdateAllowed(identity);

        if (firstName != null) {
            validationService.validateName(firstName, "First name");
            identity.updateFirstName(firstName);
        }
        if (lastName != null) {
            validationService.validateName(lastName, "Last name");
            identity.updateLastName(lastName);
        }

        repository.save(identity);
        return identity;
    }

    // Changes status of an existing ID.
    public DigitalID changeStatus(String digitalIdNumber, Status newStatus,
            Organisation requestor) {
        authorizationService.checkCanChangeStatus(requestor);

        DigitalID identity = repository.findById(digitalIdNumber)
                .orElseThrow(() -> IdentityNotFoundException.forIdNumber(digitalIdNumber));

        validationService.validateStatusTransition(identity, newStatus);

        identity.changeStatus(newStatus);
        repository.save(identity);
        return identity;
    }
}
