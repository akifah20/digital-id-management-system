package com.digitalid.service;

import com.digitalid.domain.Organisation;
import com.digitalid.exception.AuthorizationException;

public class AuthorizationService {

    public void checkCanCreate(Organisation organisation) {
        requireOrganisation(organisation);
        if (!organisation.canManageIdentities()) {
            throw new AuthorizationException(
                    organisation.getName() + " is not authorised to create Digital IDs");
        }
    }

    public void checkCanUpdate(Organisation organisation) {
        requireOrganisation(organisation);
        if (!organisation.canManageIdentities()) {
            throw new AuthorizationException(
                    organisation.getName() + " is not authorised to update Digital IDs");
        }
    }

    public void checkCanChangeStatus(Organisation organisation) {
        requireOrganisation(organisation);
        if (!organisation.canManageIdentities()) {
            throw new AuthorizationException(
                    organisation.getName() + " is not authorised to change Digital ID status");
        }
    }

    public void checkCanVerify(Organisation organisation) {
        requireOrganisation(organisation);
    }

    private void requireOrganisation(Organisation organisation) {
        if (organisation == null) {
            throw new IllegalArgumentException("Organisation is required");
        }
    }
}
