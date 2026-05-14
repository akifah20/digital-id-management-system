package com.digitalid.domain;

/**
 * For types of organisations tht exist in the Digital ID ecosystem.
 * 
 * Each type interacts with the platform in different ways:
 * The central authority manages identities (create, update, change
 * status).
 * Other organisations consume identity info(verification+lookup).
 */
public enum OrganisationType {

    /**
     * The central authority - home
     * creates,updates & changes status of IDs.
     */
    CENTRAL_AUTHORITY("Central Authority", true), // creating, updating + changing status of IDs
    TAX_AUTHORITY("Tax Authority", false), // verifies identities for tax purposes.
    DRIVING_LICENCE_AUTHORITY("Driving Licence Authority", false), // verifies identities for licensing
    BANK("Bank", false), // verifies identity validity for financial services
    EMPLOYER("Employer", false), // verifies identity validity for employment
    WELFARE_SERVICE("Welfare Service", false); // verifies identities for benefits

    private final String displayName;
    private final boolean canManageIdentities;

    OrganisationType(String displayName, boolean canManageIdentities) {
        this.displayName = displayName;
        this.canManageIdentities = canManageIdentities;
    }

    /**
     * @return easy to read name of the org type
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if this org type can create, update, or change status of identities.
     * Only the CA is permitted to manage identities.
     * 
     * @return true if this type has identity management permissions
     */
    public boolean canManageIdentities() {
        return canManageIdentities;
    }
}
