package com.neighborfit.neighborfit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "places")
public class Place {
    @Id
    private String id;
    private String name; // e.g., "Greater Noida"
    private List<Society> societies;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Society> getSocieties() { return societies; }
    public void setSocieties(List<Society> societies) { this.societies = societies; }
}
