import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
       String filename = args[0];
       VehicleDB vehicleDB = new VehicleDB(filename, "Vehicles");
       try{
       vehicleDB.initializeDatabase();
       }
       catch(Exception e){
           
       }

        // Class<? extends Vehicle> cls = v.getClass();
        // Method[] methods = cls.getDeclaredMethods();
        // Field[] fields  = cls.getDeclaredFields();
      
        // System.out.println("Fields:");
        // for(Field field: fields){
        //     field.setAccessible(true);
        //     System.out.println(field.get(v));
        // }

        // System.out.println("Methods:");
        // for(Method method: methods){
        //     System.out.println(method.invoke(v));
        // }
           


}
}