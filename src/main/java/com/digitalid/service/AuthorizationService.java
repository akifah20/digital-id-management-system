package com.digitalid.service;

import com.digitalid.domain.Organisation;
import com.digitalid.exception.AuthorizationException;

//org-level authorisation ruling

public class AuthorizationService {

    public void checkCanCreate(Organisation organisation) { // Checks that an org is permitted to create a new ID
        requireOrganisation(organisation);
        if (!organisation.canManageIdentities()) {
            throw new AuthorizationException( // if the organisation is not the CA
                    organisation.getName() + " is not authorised to create Digital IDs");
        }
    }

    public void checkCanUpdate(Organisation organisation) { // Checks IF org is permitted to update an existing ID.
        requireOrganisation(organisation);
        if (!organisation.canManageIdentities()) { // if org is not CA
            throw new AuthorizationException(
                    organisation.getName() + " is not authorised to update Digital IDs");
        }
    }

    // Checks that an org is permitted to change status of an ID.
    public void checkCanChangeStatus(Organisation organisation) {
        requireOrganisation(organisation);
        if (!organisation.canManageIdentities()) { // if org is not CA then..
            throw new AuthorizationException(
                    organisation.getName() + " is not authorised to change Digital ID status");
        }
    }

    // Checks that an org is permitted to verify an ID.
    // All authorised organisations can verify; this method just makes sure
    // tht a non-null organisation is supplied.
    public void checkCanVerify(Organisation organisation) {
        requireOrganisation(organisation);
    }

    private void requireOrganisation(Organisation organisation) {
        if (organisation == null) {
            throw new IllegalArgumentException("Organisation is required");
        }
    }
}
