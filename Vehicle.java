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


 Vehicle(String make, String size, double engineSize, boolean isImport, double weight){
    this.size = size;
    this.make = make;
    this.engineSize = engineSize;
    this.isImport = isImport;
    this.weight = weight;
}

public String getSize(){
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



}