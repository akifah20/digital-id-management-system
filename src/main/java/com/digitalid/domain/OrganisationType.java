package com.digitalid.domain;

public enum OrganisationType {

    CENTRAL_AUTHORITY("Central Authority", true),
    TAX_AUTHORITY("Tax Authority", false),
    DRIVING_LICENCE_AUTHORITY("Driving Licence Authority", false),
    BANK("Bank", false),
    EMPLOYER("Employer", false),
    WELFARE_SERVICE("Welfare Service", false);

    private final String displayName;
    private final boolean canManageIdentities;

    OrganisationType(String displayName, boolean canManageIdentities) {
        this.displayName = displayName;
        this.canManageIdentities = canManageIdentities;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canManageIdentities() {
        return canManageIdentities;
    }
}
