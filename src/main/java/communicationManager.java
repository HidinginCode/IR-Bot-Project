import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class communicationManager {
    dataManager dm = new dataManager();

    public communicationManager() throws IOException {

    }

    public String getMessageText(String message) throws IOException, ParseException {
        message = message.toLowerCase();
        List<String> messageList = new ArrayList<>();
        StandardTokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader(message));
        final CharTermAttribute termAttr = tokenizer.addAttribute(CharTermAttribute.class);
        tokenizer.reset();

        while(tokenizer.incrementToken()){
            messageList.add(termAttr.toString());
        }
        System.out.println(messageList.toString());
        return getAnswer(messageList, message);
    }

    public String getAnswer(List<String> messageList, String message) throws IOException, ParseException {
        if(message.equalsIgnoreCase("How many documents are in the database?")){
            String returnString = "Currently I have stored "+dm.getNumberOfDocs()+" documents in my database.";
            return returnString;
        }
        else{
            List<Query> queryList = new ArrayList<>(); //List of querys
            for (int i = 0; i < messageList.size(); i++){
                Query q = new QueryParser("content", new StandardAnalyzer()).parse(messageList.get(i)); //Make a list of querys for each word
                queryList.add(q);
            }
            List<TopDocs> topDocs = new ArrayList(); //List of TopDocs
            IndexReader reader= DirectoryReader.open(dataManager.dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            for(int i = 0; i < queryList.size(); i++){
                TopDocs docs = searcher.search(queryList.get(i), 10);
                topDocs.add(docs);
            }

            List<ScoreDoc[]> hitsList = new ArrayList<>();
            for(int i = 0; i < queryList.size(); i++){
                hitsList.add(topDocs.get(i).scoreDocs);
            }
            System.out.println(hitsList.get(0)[0].doc);
            System.out.println(reader.document(hitsList.get(0)[0].doc).getField("content").toString());
        }
        return "Sorry I didn't understand :(";
    }

}
