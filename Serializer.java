import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    private final String fileName = "Vehicles.dat";

    public void serializeObject(Vehicle vehicle) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(vehicle);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vehicle deserializeObject() {
        Vehicle v = null;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fin);
            v = (Vehicle) in.readObject();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
}
