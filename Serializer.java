import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {
    private String fileName = null;

    Serializer(String fileName){
        this.fileName = fileName;
    }

    public void serializeObject(Serializable object) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(object);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Serializable deserializeObject() {
       Serializable object = null;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fin);
            object = (Serializable) in.readObject();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
