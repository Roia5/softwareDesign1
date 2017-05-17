package il.ac.technion.cs.sd.book.app;

import Database.Reader;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.*;
import java.util.stream.Collectors;

public class BookScoreReaderImpl implements BookScoreReader{
  private Reader bookReader;
  private Reader reviewerReader;

  @Inject
  public BookScoreReaderImpl(@Named("reviewer_filename") Reader reviewerReaderNew,
                             @Named("book_filename") Reader bookReaderNew) {
    bookReader = bookReaderNew;
    reviewerReader = reviewerReaderNew;
  }
  //This method finds book info from a given reviewer and returns it.
  private String getBookInfoFromReviewer(String reviewerId, String bookId) throws InterruptedException {
      String booksReviewedByReader;

      booksReviewedByReader = reviewerReader.find(reviewerId," ",2);
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

  private List<String> getPairFirstList(Reader reader, String key){
    String data;
    try {
      data = reader.find(key," ",2);
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
      return getPairFirstList(reviewerReader,reviewerId);
  }

  private Map<String, Integer> getPairMap(Reader reader, String key){
    String data;
    try {
      data = reader.find(key," ",2);
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
