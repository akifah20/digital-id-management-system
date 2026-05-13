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
     * Solely responsible for creating, updating, and changing the status of Digital
     * IDs.
     */
    CENTRAL_AUTHORITY, // creating, updating + changing status of IDs
    TAX_AUTHORITY, // verifies identities for tax purposes.
    DRIVING_LICENCE_AUTHORITY, // verifies identities for licensing
    BANK, // verifies identity validity for financial services
    EMPLOYER, // verifies identity validity for employment
    WELFARE_SERVICE // verifies identities for benefits
}
