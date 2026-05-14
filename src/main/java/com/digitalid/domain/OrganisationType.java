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
    CENTRAL_AUTHORITY("Central Authority"), // creating, updating + changing status of IDs
    TAX_AUTHORITY("Tax Authority"), // verifies identities for tax purposes.
    DRIVING_LICENCE_AUTHORITY("Driving Licence Authority"), // verifies identities for licensing
    BANK("Bank"), // verifies identity validity for financial services
    EMPLOYER("Employer"), // verifies identity validity for employment
    WELFARE_SERVICE("Welfare Service"); // verifies identities for benefits

    private final String displayName;

    OrganisationType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return easy to read name of the org type
     */
    public String getDisplayName() {
        return displayName;
    }
}
