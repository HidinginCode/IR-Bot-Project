import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
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

public class communicationManager {
    dataManager dm = new dataManager();

    public communicationManager() throws IOException {

    }

    public String getMessageText(String message) throws IOException, ParseException {
        message = message.toLowerCase();
        List<String> messageList = new ArrayList<>();
        CharArraySet stopSet = StopFilter.makeStopSet("what", "is", "can", "you", "tell", "me");
        StandardTokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader(message));
        StopFilter stopTokenizer = new StopFilter(tokenizer, stopSet);
        final CharTermAttribute termAttr = stopTokenizer.addAttribute(CharTermAttribute.class);
        stopTokenizer.reset();

        while(stopTokenizer.incrementToken()){
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
        else {
            //Make a query for every word that has not been filtered
            List<Query> queryList = new ArrayList<>(); //List of querys
            for (int i = 0; i < messageList.size(); i++) {
                Query q = new QueryParser("content", new StandardAnalyzer()).parse(messageList.get(i)); //Make a list of querys for each word
                queryList.add(q);
            }

            //Get the Top Docs for every query and save it to list
            List<TopDocs> topDocs = new ArrayList(); //List of TopDocs
            IndexReader reader = DirectoryReader.open(dataManager.dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            for (int i = 0; i < queryList.size(); i++) {
                TopDocs docs = searcher.search(queryList.get(i), 10);
                topDocs.add(docs);
            }

            //Convert topdocs to arrays and save arrays to list
            List<ScoreDoc[]> hitsList = new ArrayList<>();
            for (int i = 0; i < queryList.size(); i++) {
                hitsList.add(topDocs.get(i).scoreDocs);
            }
            if (hitsList.size() != 0) {

                float highestScore = 0;
                int docId = 0;

                for (int i = 0; i < hitsList.size(); i++) {
                    for (int j = 0; j < hitsList.get(i).length; j++) {
                        if (hitsList.get(i)[j].score > highestScore) {
                            highestScore = hitsList.get(i)[j].score;
                            docId = hitsList.get(i)[j].doc;
                        }
                    }
                }
                String answer = reader.document(docId).getField("content").stringValue();
                answer = answerPreprocessing(answer);
                return answer;
            }
            else{
                return "Sorry I didn't understand :(";
            }
        }
    }

    public String answerPreprocessing(String answer){
        if(answer.startsWith(".") || answer.startsWith("!") || answer.startsWith(" ") || answer.startsWith(",")){
            answer = answer.substring(1);
            return answerPreprocessing(answer);
        }
        return answer;
    }

}
