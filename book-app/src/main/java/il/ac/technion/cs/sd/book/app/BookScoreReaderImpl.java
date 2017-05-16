package il.ac.technion.cs.sd.book.app;

import Database.Reader;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.*;

public class BookScoreReaderImpl implements BookScoreReader{
  private Reader bookReader;
  private Reader reviewerReader;

  @Inject
  public BookScoreReaderImpl(@Named("reviewer_filename") Reader reviewerReaderNew,
                             @Named("book_filename") Reader bookReaderNew) {
    bookReader = bookReaderNew;
    reviewerReader = reviewerReaderNew;
  }
  @Override
  public boolean gaveReview(String reviewerId, String bookId) {
    String BooksReviewedByReader;
    try {
      BooksReviewedByReader = reviewerReader.find(reviewerId," ",2);
    } catch (InterruptedException e) {
      return false;
    }
    return BooksReviewedByReader.contains(bookId);
  }

  @Override
  public OptionalDouble getScore(String reviewerId, String bookId) {
    String BooksReviewedByReader;
    try {
      BooksReviewedByReader = reviewerReader.find(reviewerId," ",2);
    } catch (InterruptedException e) {
      return OptionalDouble.empty();
    }
    String[] booksAndGrades = BooksReviewedByReader.split(",");
    for(String bookAndGrade : booksAndGrades){
      String[] split = bookAndGrade.split("-");
      if(split[0].equals(bookId)){
        return OptionalDouble.of(Integer.parseInt(split[1]));
      }
    }
    return OptionalDouble.empty();
  }

  private List<String> getPairFirstList(Reader reader, String key){
    List<String> pairList = new LinkedList<>();
    String data;
    try {
      data = reader.find(key," ",2);
    } catch (InterruptedException e) {
      return pairList;
    }
    String[] pairsArray = data.split(",");
    for(String pair : pairsArray){
      String[] split = pair.split("-");
      pairList.add(split[0]);
    }
    return pairList;
  }
  @Override
  public List<String> getReviewedBooks(String reviewerId) {
      return getPairFirstList(reviewerReader,reviewerId);
  }

  private Map<String, Integer> getPairMap(Reader reader, String key){
    Map<String, Integer> pairMap = new HashMap<>();
    String data;
    try {
      data = reader.find(key," ",2);
    } catch (InterruptedException e) {
      return pairMap;
    }
    String[] pairArray = data.split(",");
    for(String pair : pairArray){
      String[] split = pair.split("-");
      pairMap.put(split[0],Integer.parseInt(split[1]));
    }
    return pairMap;
  }
  @Override
  public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
    return getPairMap(reviewerReader,reviewerId);
  }

  private OptionalDouble getAverage(Reader reader, String key){
    String average;
    try {
      average = reader.find(key," ",1);
    } catch (InterruptedException e) {
      return OptionalDouble.empty();
    }
    return OptionalDouble.of(Integer.parseInt(average));
  }
  @Override
  public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
    return getAverage(reviewerReader,reviewerId);
  }

  @Override
  public List<String> getReviewers(String bookId) {
    return getPairFirstList(bookReader,bookId);
  }

  @Override
  public Map<String, Integer> getReviewsForBook(String bookId) {
    return getPairMap(bookReader,bookId);
  }

  @Override
  public OptionalDouble getAverageReviewScoreForBook(String bookId) {
    return getAverage(bookReader,bookId);
  }
}
