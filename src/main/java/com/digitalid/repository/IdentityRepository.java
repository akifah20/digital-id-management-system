package com.digitalid.repository;

import java.util.List;
import java.util.Optional;

import com.digitalid.domain.DigitalID;

public interface IdentityRepository {
    void save(DigitalID identity);

    Optional<DigitalID> findById(String digitalIdNumber);

    List<DigitalID> findAll();

    boolean exists(String digitalIdNumber);

    void delete(String digitalIdNumber);

    int count();
}
