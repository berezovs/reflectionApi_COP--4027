/*
Author: Serghei Berezovschi
Project: Reflection(Project1)
Class: COP--4027
*/

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class VehicleDB {
    private Logger logger = null;
    private Serializer serializer = null;
    private Statement statement = null;
    private Connection conn = null;
    private String dbProperties = null;
    private String dbName = null;
    private final String VEHICLE_DATA_FILENAME = "Vehicle.dat";
    private final String DB_OPERATIONS_LOG_FILE = "dbOperations.log";
    private Class<Vehicle> vehicleCls = null;

    VehicleDB(String dbProperties, String dbName) {
        this.dbProperties = dbProperties;
        this.vehicleCls = Vehicle.class;
        this.logger = new Logger(DB_OPERATIONS_LOG_FILE);
        this.dbName = this.getClassName();
        this.serializer = new Serializer(VEHICLE_DATA_FILENAME);

    }

    // establishes connection to database, serializes and deserializes vehicle data
    public void initializeDatabase() throws Exception {
        Database.init(dbProperties);
        conn = Database.getConnection();
        statement = conn.createStatement();
        VehicleData vData = this.getVehicleData();

        this.dropTable();
        this.createTable();
        this.serializeVehicles(vData);
        VehicleData data = this.deserializeVehicles();
        this.populateDatabase(data);

    }

    // Serializes object passed in as a parameter
    private void serializeVehicles(Serializable data) {
        serializer.serializeObject(data);
    }

    // deserializes and returns vehicle data
    private VehicleData deserializeVehicles(){
        return (VehicleData) serializer.deserializeObject();
    }

    // creates a table with column names corresponding to Vehicle class fieldnames
    private void createTable() {
        String parameters = "";
        String createTableString = "CREATE TABLE " + this.dbName;

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
                logger.log("Error: Unknown datatype " + classFields[i].getType());
                continue;
            }
            if (classFields.length != i + 1) {
                parameters += ", ";
            }
        }

        createTableString += "(" + parameters + ")";

        try {

            statement.execute(createTableString);
            System.out.println("Created table " + this.dbName);
            logger.log("Table " + this.dbName + " created succesfully.");

        } catch (Exception e) {
            logger.log("Error: Could not create table " + this.dbName + ".");
            e.printStackTrace();

        }

    }

    // reads values stored in vehicle objects fields and inserts them into Vehicle
    // table through prepared statements
    public void populateDatabase(VehicleData data) {
        String insertStatement = "INSERT INTO " + this.dbName + " VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertVehicle = null;
        ArrayList<Vehicle> vehicles = data.getVehicleData();

        System.out.println("Inserting data into table " + this.dbName);

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
                insertVehicle.setDouble(5, (int) (double) (weight.get(vehicle)));
                insertVehicle.execute();

            } catch (Exception e) {
                System.out.println("Could not insert data into table.");
                logger.log("Inserting into table failed");
                e.printStackTrace();
            }

        }
        logger.log("Inserted data into table " + this.dbName);

    }

    // builds and executes queries to the database
    public void executeQueries() {
        ResultSet result = null;
        ResultSetMetaData rsm = null;

        try {
            // Retrieve all vehicles

            String showAllVehiclesQuery = "SELECT * FROM " + this.dbName;
            System.out.println("Issuing query: " + showAllVehiclesQuery);
            result = statement.executeQuery(showAllVehiclesQuery);
            rsm = result.getMetaData();
            this.printQueryResults(result, rsm);
            logger.log("Displaying all vehicles");

            // retrieve all Chevy and Toyotas
            String chevysAndToyotasString = "SELECT * FROM " + this.dbName + " WHERE make = ? OR make = ?";
            PreparedStatement allChevysAndToyotasQuery = conn.prepareStatement(chevysAndToyotasString);

            System.out.println("");
            System.out.println("Issuing query: SELECT * FROM " + this.dbName + " WHERE make = Chevy OR make = Toyota");

            allChevysAndToyotasQuery.setString(1, VehicleMakes.Chevy.toString());
            allChevysAndToyotasQuery.setString(2,  VehicleMakes.Toyota.toString());
            result = allChevysAndToyotasQuery.executeQuery();
            rsm = result.getMetaData();
            this.printQueryResults(result, rsm);
            logger.log("Showing all Chevys and Toyotas");

            // Show all vehicles weighing more than 2500lbs
            String fullsizedVehicles = "SELECT * FROM " + this.dbName + " WHERE weight > ?";
            PreparedStatement fullsizedVehiclesQuery = conn.prepareStatement(fullsizedVehicles);

            System.out.println("");
            System.out.println("Issuing query: SELECT * FROM " + this.dbName + " WHERE weight > 2500");
            fullsizedVehiclesQuery.setDouble(1, 2500.0);
            result = fullsizedVehiclesQuery.executeQuery();
            rsm = result.getMetaData();
            this.printQueryResults(result, rsm);
            logger.log("Showing vehicles whose weight is greater than 2500lbs");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // formats and outputs query result to the screen
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
            e.printStackTrace();
        }
    }

    // drops table 
    public void dropTable() {

        String dropTableString = "DROP TABLE " + this.dbName;

        try {

            statement.execute(dropTableString);
            System.out.println("Dropping table " + this.dbName);
            logger.log("Dropping table " + this.dbName);

        } catch (Exception e) {

            logger.log("Could not drop table " + this.dbName);

        }

    }

    // reads declared fields from Vehicle class and return their names in the form of an
    // array
    private Field[] getClassFields() {
        Field[] fields = vehicleCls.getDeclaredFields();
        return fields;
    }

    // retrieves and returns Vehicle class name
    private String getClassName() {
        Class<Vehicle> vehicleCls = Vehicle.class;
        return vehicleCls.getName();
    }

    // returns an instance of the VehicleData class
    private VehicleData getVehicleData() {
        return new VehicleData();
    }

}