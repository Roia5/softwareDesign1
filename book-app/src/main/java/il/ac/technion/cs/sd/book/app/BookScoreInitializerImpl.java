package il.ac.technion.cs.sd.book.app;
import java.io.StringReader;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Database.Reader;
import com.google.inject.Inject;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class BookScoreInitializerImpl implements BookScoreInitializer{
    private TreeMap<Pair<String,String>,String> bookMap;
    private TreeMap<Pair<String,String>,String> reviewerMap;
    public static String reviewer_filename = "reviewer_filename";
    public static String book_filename = "book_filename";
    private Reader bookReader;
    private Reader reviewerReader;
    /*public BookScoreInitializerImpl(Reader userDatabaseLibrary) {
        reader = userDatabaseLibrary;
    }*/
    @Inject
    public BookScoreInitializerImpl() {
        bookReader = new Reader("book_storage");
        reviewerReader = new Reader("reviewer_storage");
    }
    /*
    parseXML- uses java DOM to parse the xml file.
    @returns the new document.
     */
    private Document parseXML(String filename){
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new InputSource(new StringReader(filename)));
            doc.getDocumentElement().normalize();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return doc;
    }
    private void buildPairsAndInsert(Element reviewerElement){
        String reviewerID = reviewerElement.getAttribute("Id");
        NodeList reviewList = reviewerElement.getElementsByTagName("Review");
        for (int reviewIndex = 0; reviewIndex < reviewList.getLength(); reviewIndex++) {
            Element reviewElement = (Element) reviewList.item(reviewIndex);
            String bookID = reviewElement.getElementsByTagName("Id").item(0).getTextContent();
            String bookGrade = reviewElement.getElementsByTagName("Score").item(0).getTextContent();
            bookMap.put(new Pair<>(bookID,reviewerID),bookGrade);
            reviewerMap.put(new Pair<>(reviewerID,bookID),bookGrade);
        }
    }
    private void buildBookMap(String filename){
        Document doc = parseXML(filename);
        bookMap = new TreeMap<>((o1, o2) -> {
            Integer value1 = Integer.parseInt(o1.getValue());
            Integer value2 = Integer.parseInt(o2.getValue());
            if(!o1.getKey().equals(o2.getKey())){
                return o1.getKey().compareTo(o2.getKey());
            }
            else{
                return value1-value2;
            }
        });
        reviewerMap = new TreeMap<>((o1, o2) -> {
            Integer key1 = Integer.parseInt(o1.getKey());
            Integer key2 = Integer.parseInt(o2.getKey());
            if(!key1.equals(key2)){
                return key1-key2;
            }
            else{
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        NodeList reviewerList = doc.getElementsByTagName("Reviewer");
        for (int reviewerIndex = 0; reviewerIndex < reviewerList.getLength(); reviewerIndex++) {
            Element reviewerElement = (Element) reviewerList.item(reviewerIndex);
            buildPairsAndInsert(reviewerElement);
        }
    }
    private List<String> buildLines(Set<Map.Entry<Pair<String,String>, String>> entrySet){
        String currentDataID = null;
        List<String> lines = new ArrayList<>();
        StringBuilder DataAndGradeStringBuilder = new StringBuilder("");
        int countReviews = 0, sumGrades = 0;
        Integer average;
        for (Map.Entry<Pair<String,String>, String> entry : entrySet) {
            if(currentDataID==null){
                currentDataID=entry.getKey().getKey();
            }
            else{
                DataAndGradeStringBuilder.append(",");
            }
            if(entry.getKey().getKey().equals(currentDataID)){
                countReviews++;
            }
            else{
                average = sumGrades / countReviews;
                lines.add(currentDataID + " " + average.toString() + " " + DataAndGradeStringBuilder.toString());
                currentDataID = entry.getKey().getKey();
                DataAndGradeStringBuilder.setLength(0);
                countReviews = 1;
                sumGrades = 0;
            }
            sumGrades+= Integer.parseInt(entry.getValue());
            DataAndGradeStringBuilder.append(entry.getKey().getValue());
            DataAndGradeStringBuilder.append("-");
            DataAndGradeStringBuilder.append(entry.getValue());

        }
        average = sumGrades / countReviews;
        lines.add(currentDataID + " " + average.toString() + " " + DataAndGradeStringBuilder.toString());
        return lines;
    }
    public void setup(String xmlData){
        buildBookMap(xmlData);
        List<String> bookFileLines = buildLines(bookMap.entrySet());        //write books to library
        bookReader.insertStrings(bookFileLines);
        List<String> reviewerFileLines = buildLines(reviewerMap.entrySet());
        reviewerReader.insertStrings(reviewerFileLines);        //write reviewers to library
    }
}
