package com.digitalid.domain;

/**
 * shows lifecycle states of a Digital ID.
 *
 * 
 *
 */
public enum Status {
    ACTIVE, // Digital ID is valid & can be used for verification.
    SUSPENDED, // Digital ID is temporarily suspended & cannot be used. Can be reactivated by the central authority.
    REVOKED, // Digital ID has been permanently revoked. terminated state - can't change
    EXPIRED; // Digital ID has expired & is no longer valid. terminated state - can't change
             
    /**
     * Checks if status allows identity to be used for verification.
     * 
     * @return true if the identity is usable (only ACTIVE), false otherwise
     */
    public boolean isUsable() {
        return this == ACTIVE;
    }

    /**
     * Checks if status is terminal.
     * 
     * @return true if the status is terminal (REVOKED/EXPIRED)
     */
    public boolean isTerminal() {
        return this == REVOKED || this == EXPIRED;
    }
}
