package il.ac.technion.cs.sd.book.app;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javafx.util.Pair;
import Database.Reader;
import com.google.inject.Inject;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class BookScoreInitializerImpl implements BookScoreInitializer {
    private TreeMap<Pair<String,String>,String> bookMap;
    private TreeMap<Pair<String,String>,String> reviewerMap;
    public static String reviewer_filename = "reviewer_storage";
    public static String book_filename = "book_storage";
    public static String reviewer_data_filename = "reviewer_data_storage";
    public static String book_data_filename = "book_data_storage";

    private Reader bookReader;
    private Reader bookRecordsReader;
    private Reader reviewerReader;
    private Reader reviewerRecordsReader;
    @Inject
    public BookScoreInitializerImpl(LineStorageFactory lsf) {
        bookReader = new Reader(lsf, book_filename);
        reviewerReader = new Reader(lsf, reviewer_filename);
        bookRecordsReader = new Reader(lsf, book_data_filename);
        reviewerRecordsReader = new Reader(lsf, reviewer_data_filename);

    }
    /*@Inject- Maybe redundant? Inject is used when we don't want to be tied up to a single implementation,
    * but here we use the provided LineStorage*/
    public BookScoreInitializerImpl() {
        bookReader = new Reader(book_filename);
        reviewerReader = new Reader(reviewer_filename);
        bookRecordsReader = new Reader(book_data_filename);
        reviewerRecordsReader = new Reader(reviewer_data_filename);
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
            if(!o1.getKey().equals(o2.getKey())){
                return o1.getKey().compareTo(o2.getKey());
            }
            else{
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        reviewerMap = new TreeMap<>((o1, o2) -> {
            if(!o1.getKey().equals(o2.getKey())){
                return o1.getKey().compareTo(o2.getKey());
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
        Double average;
        for (Map.Entry<Pair<String,String>, String> entry : entrySet) {
            if(currentDataID==null){
                currentDataID=entry.getKey().getKey();
            }
            else{
                if (entry.getKey().getKey().equals(currentDataID))
                    DataAndGradeStringBuilder.append(",");
            }
            if(entry.getKey().getKey().equals(currentDataID)){
                countReviews++;
            }
            else{
                average = (double)sumGrades / countReviews;
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
        average = (double)sumGrades / countReviews;
        lines.add(currentDataID + " " + average.toString() + " " + DataAndGradeStringBuilder.toString());
        return lines;
    }

    private void makeDataFiles(List<String> fileLines, Reader linesReader, Reader dataReader) {
        Collections.sort(fileLines);

        List<String> idToLineTmp = fileLines.stream().map((s) -> s.split(" ")[0]).collect(Collectors.toList());
        List<String> data     = fileLines.stream().map((s) -> s.split(" ")[1] + " " + s.split(" ")[2])
                .collect(Collectors.toList());


        List<String> idToLine = new LinkedList<>();
        //Put the line number
        for (int i = 0; i < idToLineTmp.size(); ++i) {
            String st = idToLineTmp.get(i);
            st += " " + String.valueOf(i);
            idToLine.add(i, st);
        }

        linesReader.insertStrings(idToLine, false);
        dataReader.insertStrings(data,false);
    }
    //For Testing
    public List<String> getBookFileLines() {
        return buildLines(bookMap.entrySet());
    }

    public List<String> getReviewerFileLines() {
        return buildLines(reviewerMap.entrySet());
    }
    public void setup(String xmlData){
        buildBookMap(xmlData);
        List<String> bookFileLines = buildLines(bookMap.entrySet());
        makeDataFiles(bookFileLines, bookReader, bookRecordsReader); //write books to library
        List<String> reviewerFileLines = buildLines(reviewerMap.entrySet());
        makeDataFiles(reviewerFileLines, reviewerReader, reviewerRecordsReader); //write reviewers to library
    }
}
