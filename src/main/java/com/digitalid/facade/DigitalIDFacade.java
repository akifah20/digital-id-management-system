package com.digitalid.facade;

import java.time.LocalDate;
import java.util.List;

import com.digitalid.domain.DigitalID;
import com.digitalid.domain.Organisation;
import com.digitalid.domain.Status;
import com.digitalid.domain.VerificationResponse;
import com.digitalid.repository.IdentityRepository;
import com.digitalid.repository.InMemoryIdentityRepository;
import com.digitalid.service.AuthorizationService;
import com.digitalid.service.IdentityManagementService;
import com.digitalid.service.IdentityVerificationService;
import com.digitalid.service.ValidationService;

public class DigitalIDFacade {

    private final IdentityRepository repository;
    private final IdentityManagementService managementService;
    private final IdentityVerificationService verificationService;

    public DigitalIDFacade() {
        this.repository = new InMemoryIdentityRepository();
        ValidationService validationService = new ValidationService();
        AuthorizationService authorizationService = new AuthorizationService();
        this.managementService = new IdentityManagementService(
                repository, validationService, authorizationService);
        this.verificationService = new IdentityVerificationService(
                repository, authorizationService);
    }

    public DigitalIDFacade(IdentityRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required");
        }
        this.repository = repository;
        ValidationService validationService = new ValidationService();
        AuthorizationService authorizationService = new AuthorizationService();
        this.managementService = new IdentityManagementService(
                repository, validationService, authorizationService);
        this.verificationService = new IdentityVerificationService(
                repository, authorizationService);
    }

    public DigitalID createIdentity(String firstName, String lastName,
            LocalDate dateOfBirth, Organisation requestor) {
        return managementService.createIdentity(firstName, lastName, dateOfBirth, requestor);
    }

    public DigitalID updateName(String digitalIdNumber, String firstName, String lastName,
            Organisation requestor) {
        return managementService.updateName(digitalIdNumber, firstName, lastName, requestor);
    }

    public DigitalID changeStatus(String digitalIdNumber, Status newStatus, Organisation requestor) {
        return managementService.changeStatus(digitalIdNumber, newStatus, requestor);
    }

    public VerificationResponse verifyIdentity(String digitalIdNumber, Organisation requestor) {
        return verificationService.verify(digitalIdNumber, requestor);
    }

    public List<DigitalID> listAllIdentities() {
        return repository.findAll();
    }

    public int identityCount() {
        return repository.count();
    }
}
