package com.digitalid.domain;

import java.util.Objects;
import java.util.UUID;

public class Organisation {

    private final String id;
    private final String name;
    private final OrganisationType type;

    public Organisation(String name, OrganisationType type) {
        this(UUID.randomUUID().toString(), name, type);
    }

    public Organisation(String id, String name, OrganisationType type) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Organisation id cannot be null or empty! :(");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Organisation name cannot be null or empty! :(");
        }
        if (type == null) {
            throw new IllegalArgumentException("Organisation type cannot be null! :(");
        }

        this.id = id;
        this.name = name.trim();
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OrganisationType getType() {
        return type;
    }

    public boolean canManageIdentities() {
        return type.canManageIdentities();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        Organisation that = (Organisation) other;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Organisation{id='%s', name='%s', type=%s}", id, name, type);
    }
}
