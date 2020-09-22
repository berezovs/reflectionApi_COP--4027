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
       }
       catch(Exception e){
           e.printStackTrace();
       }

  

}
}