/*
Author: Serghei Berezovschi
Project: Reflection(Project1)
Class: COP--4027
*/

import java.lang.reflect.InvocationTargetException;


public class Main {
    public static void main(String[] args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
       String filename = args[0];
       VehicleDB vehicleDB = new VehicleDB(filename, "Vehicles");
       try{
       vehicleDB.initializeDatabase();
       vehicleDB.executeQueries();
       vehicleDB.dropTable();
       System.out.println("Exiting program");
       }
       catch(Exception e){
           e.printStackTrace();
       }

  

}
}