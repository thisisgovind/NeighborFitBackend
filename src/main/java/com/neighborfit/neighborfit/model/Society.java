package com.neighborfit.neighborfit.model;

public class Society {
    private String name;
    private double safetyRating;
    private double costOfLiving;
    private double greenSpaces;
    private double nightlife;
    private double publicTransport;
    private String description;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getSafetyRating() { return safetyRating; }
    public void setSafetyRating(double safetyRating) { this.safetyRating = safetyRating; }
    public double getCostOfLiving() { return costOfLiving; }
    public void setCostOfLiving(double costOfLiving) { this.costOfLiving = costOfLiving; }
    public double getGreenSpaces() { return greenSpaces; }
    public void setGreenSpaces(double greenSpaces) { this.greenSpaces = greenSpaces; }
    public double getNightlife() { return nightlife; }
    public void setNightlife(double nightlife) { this.nightlife = nightlife; }
    public double getPublicTransport() { return publicTransport; }
    public void setPublicTransport(double publicTransport) { this.publicTransport = publicTransport; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 