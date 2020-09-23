
/*
Author: Serghei Berezovschi
Project: Reflection(Project1)
Class: COP--4027
*/
import java.io.Serializable;
import java.util.ArrayList;

public class VehicleData implements Serializable {

    
    private static final long serialVersionUID = 1L;
    private final int MIN = 1500;
    private final int MAX = 4000;
    public ArrayList<Vehicle> vehicles = new ArrayList<>();

    //C-TOR, intantiates 10 Vehicle objects
    VehicleData(){
        Vehicle v1 = new Vehicle(VehicleMakes.Toyota.toString(), 2.4, true, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v1);
        Vehicle v2 = new Vehicle(VehicleMakes.Hyundai.toString(), 3.6, true, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v2);
        Vehicle v3 = new Vehicle(VehicleMakes.Chevy.toString(), 2.0, false, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v3);
        Vehicle v4 = new Vehicle(VehicleMakes.Ford.toString(), 2.4, false, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v4);
        Vehicle v5 = new Vehicle(VehicleMakes.Nissan.toString(), 2.5, true, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v5);
        Vehicle v6 = new Vehicle(VehicleMakes.Toyota.toString(), 2.0, true, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v6);
        Vehicle v7 = new Vehicle(VehicleMakes.Nissan.toString(), 1.8, true, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v7);
        Vehicle v8 = new Vehicle(VehicleMakes.Chevy.toString(), 3.6, false, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v8);
        Vehicle v9 = new Vehicle(VehicleMakes.Ford.toString(), 2.4, false, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v9);
        Vehicle v10 = new Vehicle(VehicleMakes.Hyundai.toString(), 2.0, true, RandomGenerator.generateRandomNumber(MIN, MAX));
        vehicles.add(v10);
    }


    //returns an array of Vehicle objects
    public ArrayList<Vehicle> getVehicleData(){
        return this.vehicles;
    }
}
