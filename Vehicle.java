/*
Author: Serghei Berezovschi
Project: Reflection(Project1)
Class: COP--4027
*/
import java.io.Serializable;

public class Vehicle implements Serializable { 
    private String make;
    private String size;
    private double engineSize;
    private boolean isImport;
    private double weight;

    
    public Vehicle(String make, double engineSize, boolean isImport, double weight) {
        this.size=null;
        this.make = make;
        this.engineSize = engineSize;
        this.isImport = isImport;
        this.weight = weight;
        this.assignSize();
    }

    //assigns Vehicle size based on its weight
    public void assignSize() {
        if (this.weight >= 1500 && this.weight < 2000) {
            this.size = VehicleSizes.compact.toString();
        }
        if (this.weight >= 2000 && this.weight < 2500) {
            this.size = VehicleSizes.intermediate.toString();
        }
        if (this.weight >= 2500 && this.weight < 4000) {
            this.size = VehicleSizes.fullSized.toString();
        }
    }

    public String getSize() {
        return this.size;
    }

    public String getMake() {
        return this.make;
    }

    public double getEngineSize() {
        return this.engineSize;
    }

    public boolean isImport() {
        return this.isImport;
    }

    public double getWeight() {
        return this.weight;
    }


}