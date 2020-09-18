import java.io.Serializable;

public class Vehicle implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String size;
    private String make;
    private double engineSize;
    private boolean isImport;
    private double weight;

public Vehicle(){
    
}

private Vehicle(String make, double engineSize, boolean isImport, double weight){
    this.make = make;
    this.engineSize = engineSize;
    this.isImport = isImport;
    this.weight = weight;
}

public String getSize(){
    if(this.weight>=1500 && this.weight<2000){
        this.size = Sizes.compact.toString();
    }
    if(this.weight>=2000&&this.weight<2500){
        this.size = Sizes.intermediate.toString();
    }
    if(this.weight>=2500 && this.weight<4000){
        this.size = Sizes.fullSized.toString();
    }
    return this.size;
}

public String getMake(){
    return this.make;
}
public double getEngineSize(){
    return this.engineSize;
}

public boolean isImport(){
    return this.isImport;
}

public double getWeight(){
    return this.weight;
}
public Vehicle getVehicleInstance(String carMake, double engineSz, boolean imported, double carWeight){
    return new Vehicle(carMake, engineSz, imported, carWeight);
}


}