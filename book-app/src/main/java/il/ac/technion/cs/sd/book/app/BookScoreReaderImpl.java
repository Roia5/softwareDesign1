package il.ac.technion.cs.sd.book.app;

import Database.Reader;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BookScoreReaderImpl implements BookScoreReader{
  private Reader bookReader;
  private Reader bookRecordsReader;
  private Reader reviewerReader;
  private Reader reviewerRecordsReader;

  @Inject
  public BookScoreReaderImpl(LineStorageFactory lsf) {
    bookReader = new Reader(lsf, BookScoreInitializerImpl.book_filename);
    bookRecordsReader = new Reader(lsf, BookScoreInitializerImpl.book_data_filename);
    reviewerReader =new Reader(lsf, BookScoreInitializerImpl.reviewer_filename);;
    reviewerRecordsReader = new Reader(lsf, BookScoreInitializerImpl.reviewer_data_filename);;
  }
  //This method finds book info from a given reviewer and returns it.
  private String getBookInfoFromReviewer(String reviewerId, String bookId) throws InterruptedException {
      String booksReviewedByReaderLineNum;

      booksReviewedByReaderLineNum = reviewerReader.find(reviewerId," ",1);
      int lineNum = Integer.valueOf(booksReviewedByReaderLineNum);

      String booksReviewedByReader = reviewerRecordsReader.find(lineNum," ", 1);
      String[] booksAndGrades = booksReviewedByReader.split(",");

      int place = Arrays.binarySearch(booksAndGrades,bookId, Comparator.comparing(o -> o.split("-")[0]));

      if (place > -1)
          return booksAndGrades[place];

      throw  new InterruptedException();
  }
  @Override
  public boolean gaveReview(String reviewerId, String bookId) {
    String bookInfo;
    try {
      System.out.print("size 1is " + bookReader.numberOfLines());
      System.out.print("size 2is " + bookRecordsReader.numberOfLines());
      System.out.print("size 3is " + reviewerReader.numberOfLines());
      System.out.print("size 4is " + reviewerRecordsReader.numberOfLines());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      bookInfo = getBookInfoFromReviewer(reviewerId,bookId);
    } catch (InterruptedException e) {
      return false;
    }

    return bookInfo.split("-")[0].equals(bookId);
  }

  @Override
  public OptionalDouble getScore(String reviewerId, String bookId) {
    String bookInfo;
    try {
      bookInfo = getBookInfoFromReviewer(reviewerId,bookId);
    } catch (InterruptedException e) {
      return OptionalDouble.empty();
    }

    return OptionalDouble.of(Integer.parseInt(bookInfo.split("-")[1]));
  }

  private List<String> getPairFirstList(Reader reader,Reader dataReader, String key){
    String data,dataLine;
    try {
      dataLine = reader.find(key," ",1);
      data = dataReader.find(Integer.valueOf(dataLine), " ", 1);
    } catch (InterruptedException e) {
      return new LinkedList<>();
    }
    List<String> pairList;
    String[] pairsArray = data.split(",");
    pairList = Arrays.stream(pairsArray).map((s)->s.split("-")[0]).collect(Collectors.toList());

    return pairList;
  }
  @Override
  public List<String> getReviewedBooks(String reviewerId) {
      return getPairFirstList(reviewerReader,reviewerRecordsReader,reviewerId);
  }

  private Map<String, Integer> getPairMap(Reader reader,Reader dataReader, String key){
    String data, dataLine;
    try {
        dataLine = reader.find(key, " ", 1);
      data = dataReader.find(Integer.valueOf(dataLine)," ",1);
    } catch (InterruptedException e) {
      return new HashMap<>();
    }
    Map<String, Integer> pairMap = new HashMap<>();
    String[] pairArray = data.split(",");
    Arrays.stream(pairArray).forEach((s)-> {
        String[] split = s.split("-");
        pairMap.put(split[0],Integer.parseInt(split[1]));
    });
    return pairMap;
  }
  @Override
  public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
    return getPairMap(reviewerReader,reviewerRecordsReader,reviewerId);
  }

  private OptionalDouble getAverage(Reader reader,Reader dataReader, String key){
    String average, averageLine;
    try {
      averageLine = reader.find(key, " ", 1);
      average = dataReader.find(Integer.valueOf(averageLine)," ",0);
    } catch (InterruptedException e) {
      return OptionalDouble.empty();
    }
    return OptionalDouble.of(Integer.parseInt(average));
  }
  @Override
  public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
    return getAverage(reviewerReader,reviewerRecordsReader,reviewerId);
  }

  @Override
  public List<String> getReviewers(String bookId) {
    return getPairFirstList(bookReader,bookRecordsReader,bookId);
  }

  @Override
  public Map<String, Integer> getReviewsForBook(String bookId) {
    return getPairMap(bookReader,bookRecordsReader,bookId);
  }

  @Override
  public OptionalDouble getAverageReviewScoreForBook(String bookId) {
    return getAverage(bookReader,bookRecordsReader,bookId);
  }
}
