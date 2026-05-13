package com.digitalid.domain;

/**
 * For lifecycle states of a Digital ID
 */
public enum Status {

    /**
     * Digital ID is valid & can be used for verification.
     */
    ACTIVE,

    /**
     * Digital ID is temporarily suspended & can't be used.
     */
    SUSPENDED,

    /**
     * Digital ID has been permanently revoked.
     */
    REVOKED,

    /**
     * Digital ID has expired & is no longer valid.
     */
    EXPIRED
}
