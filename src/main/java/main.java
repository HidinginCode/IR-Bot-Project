import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Constants;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class main {
    static dataManager dm = new dataManager();
    static Directory dir;

    static {
        try {
            dir = FSDirectory.open(Paths.get("C:\\Users\\speid\\Documents\\GitHub\\IR-Bot-Project\\src\\main\\resources\\LuceneDir"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //new gui(); //Create GUI
        Path dbPath = Paths.get("C:\\Users\\speid\\Documents\\GitHub\\IR-Bot-Project\\src\\main\\resources\\Database");
        //System.out.println(Files.exists(dbPath));

        //Begin indexing the database
        File db = new File(String.valueOf(dbPath)); //open database as file
        File[] directoryListing = db.listFiles(); //get all subFiles

        for(File child: directoryListing){  //for all directories in database
            File[] subDir = child.listFiles(); //get files of subdirectory
            for(File doc : subDir){ //for each file in subdirectory
                //System.out.println(doc.getAbsolutePath());
                String docPath = doc.getAbsolutePath();
                if (docPath.endsWith(".txt")){
                    String content = dm.getTextFromFile(docPath);
                    if(!content.isEmpty()){
                        index(content);
                    }
                }
            }
        }
        //If this is done the Database is indexed
        for(int i=0; i < dir.listAll().length; i++){
            IndexReader reader = DirectoryReader.open(dir);
            System.out.println(reader.document(i).getField("content"));
        }
    }

    public static void index(String text) throws IOException {
        StandardAnalyzer standard = new StandardAnalyzer(CharArraySet.EMPTY_SET);
        Document doc = new Document();
        doc.add(new TextField("content", text, Field.Store.YES));
        IndexWriterConfig config = new IndexWriterConfig(standard);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.addDocument(doc);
        indexWriter.close();
    }

}
