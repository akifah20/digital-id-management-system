package com.digitalid.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.digitalid.domain.DigitalID;

public class InMemoryIdentityRepository implements IdentityRepository {

    private final Map<String, DigitalID> storage = new HashMap<>();

    @Override
    public void save(DigitalID identity) {
        if (identity == null) {
            throw new IllegalArgumentException("Cannot save null identity");
        }
        storage.put(identity.getDigitalIdNumber(), identity);
    }

    @Override
    public Optional<DigitalID> findById(String digitalIdNumber) {
        if (digitalIdNumber == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(digitalIdNumber));
    }

    @Override
    public List<DigitalID> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean exists(String digitalIdNumber) {
        if (digitalIdNumber == null) {
            return false;
        }
        return storage.containsKey(digitalIdNumber);
    }

    @Override
    public void delete(String digitalIdNumber) {
        if (digitalIdNumber == null) {
            return;
        }
        storage.remove(digitalIdNumber);
    }

    @Override
    public int count() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }
}
