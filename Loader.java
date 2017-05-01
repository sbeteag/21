import java.io.File;
import java.util.ArrayList;

/**
 * Created by Steven on 4/30/2017.
 */
public class Loader {
    private ArrayList<String> names = new ArrayList<>();

    public ArrayList<String> getNames(){
        File folder = new File("Saves/");
        File[] files = folder.listFiles();
        names = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String name = files[i].getName().substring(0, files[i].getName().indexOf('.'));
                names.add(name);
            }
        }
        return names;
    }
}
