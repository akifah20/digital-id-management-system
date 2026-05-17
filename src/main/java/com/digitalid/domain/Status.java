package com.digitalid.domain;

public enum Status {
    ACTIVE,
    SUSPENDED,
    REVOKED,
    EXPIRED;

    public boolean isUsable() {
        return this == ACTIVE;
    }

    public boolean isTerminal() {
        return this == REVOKED || this == EXPIRED;
    }

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
