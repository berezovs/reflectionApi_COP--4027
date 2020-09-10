import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Vehicle vehicle = new Vehicle("Honda", "midsize", 2.4, true, 3500);
        Serializer s = new Serializer();
        s.serializeObject(vehicle);
        Vehicle v = s.deserializeObject();


        Class<? extends Vehicle> cls = v.getClass();
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields  = cls.getDeclaredFields();
      
        System.out.println("Fields:");
        for(Field field: fields){
            field.setAccessible(true);
            System.out.println(field.get(v));
        }

        System.out.println("Methods:");
        for(Method method: methods){
            System.out.println(method.invoke(v));
        }
}
}