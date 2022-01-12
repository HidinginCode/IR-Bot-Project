import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.Assert;
public class dataManager {

    public dataManager(){}

    public String getTextFromFile(String path) throws FileNotFoundException {
        //Gets the content of a textFile
        String content = "";
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            content += scanner.nextLine();
        }
        return content;
    }

}
