package com.neighborfit.neighborfit.controller;

import com.neighborfit.neighborfit.model.Place;
import com.neighborfit.neighborfit.model.Society;
import com.neighborfit.neighborfit.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000","https://neighbor-fit-frontend-git-main-thisisgovinds-projects.vercel.app"})
public class PlaceController {
    private final PlaceRepository placeRepository;
    private static final Logger logger = Logger.getLogger(PlaceController.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    // GET all places
    @GetMapping("/places")
    public ResponseEntity<List<Place>> getAllPlaces() {
        try {
            List<Place> places = placeRepository.findAll();
            logger.info("Retrieved " + places.size() + " places");
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            logger.severe("Error retrieving places: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST add new place
    @PostMapping("/places")
    public ResponseEntity<?> addPlace(@RequestBody Place place) {
        try {
            // Validate input
            if (place.getName() == null || place.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Place name is required");
            }
            if (place.getSocieties() == null || place.getSocieties().isEmpty()) {
                return ResponseEntity.badRequest().body("At least one society is required");
            }

            // Validate each society
            for (int i = 0; i < place.getSocieties().size(); i++) {
                var society = place.getSocieties().get(i);
                if (society.getName() == null || society.getName().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Society name is required for society " + (i + 1));
                }
                if (society.getSafetyRating() < 1 || society.getSafetyRating() > 5) {
                    return ResponseEntity.badRequest().body("Safety rating must be between 1 and 5 for society " + society.getName());
                }
                if (society.getCostOfLiving() < 1) {
                    return ResponseEntity.badRequest().body("Cost of living must be a positive amount (>= 1) for society " + society.getName());
                }
                if (society.getGreenSpaces() < 1 || society.getGreenSpaces() > 5) {
                    return ResponseEntity.badRequest().body("Green spaces rating must be between 1 and 5 for society " + society.getName());
                }
                if (society.getNightlife() < 1 || society.getNightlife() > 5) {
                    return ResponseEntity.badRequest().body("Nightlife rating must be between 1 and 5 for society " + society.getName());
                }
                if (society.getPublicTransport() < 1 || society.getPublicTransport() > 5) {
                    return ResponseEntity.badRequest().body("Public transport rating must be between 1 and 5 for society " + society.getName());
                }
            }

            Place savedPlace = placeRepository.save(place);
            logger.info("Added new place: " + savedPlace.getName() + " with " + savedPlace.getSocieties().size() + " societies");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
        } catch (Exception e) {
            logger.severe("Error adding place: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding place");
        }
    }

    // Get societies by place name
    @GetMapping("/places/{name}")
    public ResponseEntity<?> getPlaceByName(@PathVariable String name) {
        return placeRepository.findByNameIgnoreCase(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backend is running!");
    }

    // Add a new society to an existing place
    @PostMapping("/places/{name}/societies")
    public ResponseEntity<?> addSocietyToPlace(@PathVariable String name, @RequestBody Society newSociety) {
        try {
            // Validate input
            if (newSociety.getName() == null || newSociety.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Society name is required");
            }
            if (newSociety.getSafetyRating() < 1 || newSociety.getSafetyRating() > 5) {
                return ResponseEntity.badRequest().body("Safety rating must be between 1 and 5");
            }
            if (newSociety.getCostOfLiving() < 1) {
                return ResponseEntity.badRequest().body("Cost of living must be a positive amount (>= 1)");
            }
            if (newSociety.getGreenSpaces() < 1 || newSociety.getGreenSpaces() > 5) {
                return ResponseEntity.badRequest().body("Green spaces rating must be between 1 and 5");
            }
            if (newSociety.getNightlife() < 1 || newSociety.getNightlife() > 5) {
                return ResponseEntity.badRequest().body("Nightlife rating must be between 1 and 5");
            }
            if (newSociety.getPublicTransport() < 1 || newSociety.getPublicTransport() > 5) {
                return ResponseEntity.badRequest().body("Public transport rating must be between 1 and 5");
            }

            Query query = new Query(Criteria.where("name").is(name));
            Update update = new Update().push("societies", newSociety);
            var result = mongoTemplate.updateFirst(query, update, Place.class);

            if (result.getMatchedCount() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found");
            }
            return ResponseEntity.ok("Society added successfully");
        } catch (Exception e) {
            logger.severe("Error adding society: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding society");
        }
    }

    @PutMapping("/places/{placeName}/societies/{societyName}")
    public ResponseEntity<?> updateSociety(
        @PathVariable String placeName,
        @PathVariable String societyName,
        @RequestBody Society updatedSociety
    ) {
        try {
            Query query = new Query(Criteria.where("name").is(placeName).and("societies.name").is(societyName));
            Update update = new Update()
                .set("societies.$.name", updatedSociety.getName())
                .set("societies.$.safetyRating", updatedSociety.getSafetyRating())
                .set("societies.$.costOfLiving", updatedSociety.getCostOfLiving())
                .set("societies.$.greenSpaces", updatedSociety.getGreenSpaces())
                .set("societies.$.nightlife", updatedSociety.getNightlife())
                .set("societies.$.publicTransport", updatedSociety.getPublicTransport())
                .set("societies.$.description", updatedSociety.getDescription());
            var result = mongoTemplate.updateFirst(query, update, Place.class);
            if (result.getMatchedCount() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Society not found");
            }
            return ResponseEntity.ok("Society updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating society");
        }
    }
}
