package com.digitalid.repository;

import java.util.List;
import java.util.Optional;

import com.digitalid.domain.DigitalID;

//  Repo contract for storing & retrieving Digital IDs.

public interface IdentityRepository {
    void save(DigitalID identity); // Saves a Digital ID. If an ID with the same n.o already exists, it is
                                   // replaced(used for updates).

    Optional<DigitalID> findById(String digitalIdNumber);// Finds ID by its ID n.o.

    List<DigitalID> findAll(); // Returns all IDs currently stored.

    boolean exists(String digitalIdNumber); // Checks whether an ID with the given n.o exists - returns true if it does

    void delete(String digitalIdNumber);

    int count(); // total n.o of IDs stored
}
