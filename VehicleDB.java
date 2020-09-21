import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

        try {
            statement.execute(createTableString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not create table");
        }

    }

    public void populateDatabase(VehicleData data) {
        String insertStatement = "INSERT INTO " + this.getClassName() + " VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertVehicle = null;
        ArrayList<Vehicle> vehicles = data.getVehicleData();

        for (Vehicle vehicle : vehicles) {
            try {
                Field size = vehicle.getClass().getDeclaredField("size");
                size.setAccessible(true);

                Field make = vehicle.getClass().getDeclaredField("make");
                make.setAccessible(true);

                Field engineSize = vehicle.getClass().getDeclaredField("engineSize");
                engineSize.setAccessible(true);

                Field isImport = vehicle.getClass().getDeclaredField("isImport");
                isImport.setAccessible(true);

                Field weight = vehicle.getClass().getDeclaredField("weight");
                weight.setAccessible(true);

                insertVehicle = conn.prepareStatement(insertStatement);

                
                insertVehicle.setString(1, (String) (make.get(vehicle)));
                insertVehicle.setString(2, (String) (size.get(vehicle)));
                insertVehicle.setDouble(3, (double) (engineSize.get(vehicle)));
                insertVehicle.setBoolean(4, (boolean) (isImport.get(vehicle)));
                insertVehicle.setDouble(5, (int)(double) (weight.get(vehicle)));
                insertVehicle.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void executeQueries() {
        ResultSet result = null;
        ResultSetMetaData rsm = null;

        try {
            // Retrieve all vehicles

            System.out.println("Displaying all vehicles");
            result = statement.executeQuery("SELECT * FROM " + this.getClassName());
            rsm = result.getMetaData();
            this.printQueryResults(result, rsm);

            // retrieve all Chevy and Toyotas
            String chevysAndToyotasString = "SELECT * FROM " + this.getClassName() + " WHERE make = ? OR make = ?";
            PreparedStatement allChevysAndToyotasQuery = conn.prepareStatement(chevysAndToyotasString);

            System.out.println("");
            System.out.println("Displaying all Chevys and Toyotas");
            allChevysAndToyotasQuery.setString(1, "Chevy");
            allChevysAndToyotasQuery.setString(2, "Toyota");
            result = allChevysAndToyotasQuery.executeQuery();
            rsm = result.getMetaData();
            this.printQueryResults(result, rsm);

            String fullsizedVehicles = "SELECT * FROM " + this.getClassName() + " WHERE weight > ?";
            PreparedStatement fullsizedVehiclesQuery = conn.prepareStatement(fullsizedVehicles);

            System.out.println("");
            System.out.println("Displaying vehicles weighing more than 2500lbs");
            fullsizedVehiclesQuery.setDouble(1, 2500.0);
            result = fullsizedVehiclesQuery.executeQuery();
            rsm = result.getMetaData();
            this.printQueryResults(result, rsm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printQueryResults(ResultSet result, ResultSetMetaData rsm) {

        try {
            int cols = rsm.getColumnCount();

            for (int i = 1; i <= cols; i++)
                System.out.printf("%1$-15s", rsm.getColumnName(i));
            System.out.println("");

            while (result.next()) {

                for (int i = 1; i <= cols; i++)
                    System.out.printf("%1$-15s", result.getString(i));
                System.out.println("");

            }

        } catch (Exception e) {

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