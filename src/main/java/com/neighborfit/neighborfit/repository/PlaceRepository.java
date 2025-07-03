package com.neighborfit.neighborfit.repository;

import com.neighborfit.neighborfit.model.Place;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PlaceRepository extends MongoRepository<Place, String> {
    Optional<Place> findByNameIgnoreCase(String name);
}
