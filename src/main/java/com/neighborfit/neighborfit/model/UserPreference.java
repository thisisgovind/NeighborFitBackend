package com.neighborfit.neighborfit.model;

public class UserPreference {
    private double safetyWeight;
    private double costWeight;
    private double greenWeight;
    private double nightlifeWeight;
    private double transportWeight;

    // Getters and Setters

    public double getSafetyWeight() {
        return safetyWeight;
    }

    public void setSafetyWeight(double safetyWeight) {
        this.safetyWeight = safetyWeight;
    }

    public double getCostWeight() {
        return costWeight;
    }

    public void setCostWeight(double costWeight) {
        this.costWeight = costWeight;
    }

    public double getGreenWeight() {
        return greenWeight;
    }

    public void setGreenWeight(double greenWeight) {
        this.greenWeight = greenWeight;
    }

    public double getNightlifeWeight() {
        return nightlifeWeight;
    }

    public void setNightlifeWeight(double nightlifeWeight) {
        this.nightlifeWeight = nightlifeWeight;
    }

    public double getTransportWeight() {
        return transportWeight;
    }

    public void setTransportWeight(double transportWeight) {
        this.transportWeight = transportWeight;
    }
}
