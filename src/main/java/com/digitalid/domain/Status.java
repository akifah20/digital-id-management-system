package com.digitalid.domain;

/**
 * shows lifecycle states of a Digital ID.
 *
 * 
 *
 */
public enum Status {
    ACTIVE, // Digital ID is valid & can be used for verification.
    SUSPENDED, // Digital ID is temporarily suspended & cannot be used. Can be reactivated by
               // the central authority.
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

    /**
     * Checks if transition from one status to another/new status is valid.
     *
     * Tenets:
     * - Terminal states (REVOKED, EXPIRED) can't be changed
     * - can't transition to the same state
     * - All other transitions can happen
     *
     * @param newStatus the target status
     * @return true if the transition is allowed, false otherwise
     */
    public boolean canChangeTo(Status newStatus) {
        if (newStatus == null) {
            return false;
        }
        if (this.isTerminal()) {
            return false;
        }
        if (this == newStatus) {
            return false;
        }
        return true;
    }
}
