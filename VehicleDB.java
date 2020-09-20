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
        ArrayList<Vehicle> vehicles = data.getVehicleData();

        for (Vehicle vehicle : vehicles) {
            Field[] fields = vehicle.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    System.out.print(field.getName() + " " + field.get(vehicle) + " ");
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
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