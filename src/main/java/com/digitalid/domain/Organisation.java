package com.digitalid.domain;

import java.util.UUID;

public class Organisation {
    // every org has a unique id, name, & a type(determines what it can do - so
    // manage identities/only consumes them).

    private final String id;
    private final String name;
    private final OrganisationType type;

    /**
     * Creates a new Organisation with a generated id.
     *
     * @param name the name of the organisation
     * @param type the type of organisation
     */
    public Organisation(String name, OrganisationType type) {
        this(UUID.randomUUID().toString(), name, type);
    }

    /**
     * Creates an Organisation with a specific id
     * 
     * @param id   the unique identifier
     * @param name the name of the organisation
     * @param type the type of organisation
     * @throws IllegalArgumentException if id/name is null/empty/type is null
     */
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
}
