import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

public class VehicleDB implements Initializable {
    private String dbProperties = null;
    private String dbName = null;
    private Statement statement = null;
    private Connection conn = null;
    private final String VEHICLE_DATA_FILENAME = "Vehicle.dat";

    private Class<Vehicle> vehicleCls = null;

    VehicleDB(String dbProperties, String dbName) {
        this.dbProperties = dbProperties;
        this.dbName = dbName;

        vehicleCls = Vehicle.class;

    }

    @Override
    public void initializeDatabase() throws Exception {
        Database.init(dbProperties);
        conn = Database.getConnection();
        statement = conn.createStatement();

        this.dropTable();
        this.createTable();

        Serializer serializer = new Serializer(VEHICLE_DATA_FILENAME);
        serializer.serializeObject(this.getVehicleData());

        VehicleData data = (VehicleData) serializer.deserializeObject();
        this.populateDatabase(data);

    }

    public void createTable() {
        String parameters = "";
        String createTableString = "CREATE TABLE " + this.getClassName();

        Field[] classFields = this.getClassFields();

        for (int i = 0; i < classFields.length; i++) {
            if (classFields[i].getType().toString().equals("double")) {
                parameters += classFields[i].getName() + " DOUBLE";
            } else if (classFields[i].getType().toString().equals("boolean")) {
                parameters += classFields[i].getName() + " BOOLEAN";
            } else if (classFields[i].getType().toString().equals("int")) {
                parameters += classFields[i].getName() + " INTEGER";
            } else if (classFields[i].getType().toString().equals("class java.lang.String")) {
                parameters += classFields[i].getName() + " VARCHAR(20)";
            } else {
                System.out.println("Logging: Unknown datatype " + classFields[i].getType());
                continue;
            }
            if (classFields.length != i + 1) {
                parameters += ", ";
            }
        }

        createTableString += "(" + parameters + ")";
        System.out.println(createTableString);

        try {
            statement.execute(createTableString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not create table");
        }

    }

    public void populateDatabase(VehicleData data) {
        String insertStatement = "INSERT INTO " +this.getClassName()+" VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertVehicle = null;
        ArrayList<Vehicle> vehicles = data.getVehicleData();

        for (Vehicle vehicle : vehicles) {
            try{
            Field size= vehicle.getClass().getDeclaredField("size");
            size.setAccessible(true);

            Field make= vehicle.getClass().getDeclaredField("make");
            make.setAccessible(true);

            Field engineSize= vehicle.getClass().getDeclaredField("engineSize");
            engineSize.setAccessible(true);

            Field isImport= vehicle.getClass().getDeclaredField("isImport");
            isImport.setAccessible(true);

            Field weight= vehicle.getClass().getDeclaredField("weight");
            weight.setAccessible(true);


            
           insertVehicle = conn.prepareStatement(insertStatement);
            
            //insertVehicle.setString(1, this.getClassName());
            insertVehicle.setString(1, (String)(size.get(vehicle)));
            insertVehicle.setString(2, (String)(make.get(vehicle)));
            insertVehicle.setDouble(3, (double)(engineSize.get(vehicle)));
            insertVehicle.setBoolean(4, (boolean)(isImport.get(vehicle)));
            insertVehicle.setDouble(5, (double) (weight.get(vehicle)));
            insertVehicle.execute();

        //     conn.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }

          
    }
}

    public void dropTable() {

        String dropTableString = "DROP TABLE " + this.getClassName();

        try {
            System.out.println("Dropping Table " + this.getClassName());
            statement.execute(dropTableString);

        } catch (Exception e) {

            System.out.println("Drop failed");
            e.printStackTrace();

        }

    }

    public Field[] getClassFields() {
        Field[] fields = vehicleCls.getDeclaredFields();
        return fields;
    }

    public String getClassName() {
        Class<Vehicle> vehicleCls = Vehicle.class;

        return vehicleCls.getName();
    }

    public VehicleData getVehicleData() {
        return new VehicleData();

    }

}