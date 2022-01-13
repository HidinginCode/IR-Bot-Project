import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
public class dataManager {
    static Directory dir;


    public dataManager() throws IOException {
        try {
            dir = FSDirectory.open(Paths.get("C:\\Users\\speid\\Documents\\GitHub\\IR-Bot-Project\\src\\main\\resources\\LuceneDir"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path pathToIndex = Paths.get("C:\\Users\\speid\\Documents\\GitHub\\IR-Bot-Project\\src\\main\\resources\\LuceneDir");
        if(!Files.exists(pathToIndex)){
            indexDatabase();
        }
    }

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

    public void indexDatabase() throws IOException {
        Path dbPath = Paths.get("C:\\Users\\speid\\Documents\\GitHub\\IR-Bot-Project\\src\\main\\resources\\Database");

        //Begin indexing the database
        File db = new File(String.valueOf(dbPath)); //open database as file
        File[] directoryListing = db.listFiles(); //get all subFiles

        int j = 0;
        for(File child: directoryListing){  //for all directories in database
            File[] subDir = child.listFiles(); //get files of subdirectory
            for(File doc : subDir){ //for each file in subdirectory
                String docPath = doc.getAbsolutePath();
                if (docPath.endsWith(".txt")){
                    String content = getTextFromFile(docPath);
                    index(content);
                    j++;
                    System.out.println(j);
                }
            }
        }
        //If this is done the Database is indexed
        for(int i=0; i < 2000; i++){
            IndexReader reader = DirectoryReader.open(dir);
            System.out.println(reader.document(i).getField("content"));
        }
    }

    public static void index(String text) throws IOException {
        if(!text.equals("")) {
            StandardAnalyzer standard = new StandardAnalyzer(CharArraySet.EMPTY_SET);
            Document doc = new Document();
            doc.add(new TextField("content", text, Field.Store.YES));
            IndexWriterConfig config = new IndexWriterConfig(standard);
            IndexWriter indexWriter = new IndexWriter(dir, config);
            indexWriter.addDocument(doc);
            indexWriter.close();
        }
    }

    public int getNumberOfDocs() throws IOException {
        IndexReader reader = DirectoryReader.open(dir);
        return reader.maxDoc();
    }
}


