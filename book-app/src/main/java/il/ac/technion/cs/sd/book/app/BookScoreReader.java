package il.ac.technion.cs.sd.book.app;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * This class will only be instantiated by Guice after
 * {@link BookScoreInitializer#setup(java.lang.String) has been called}.
 */
public interface BookScoreReader {
  /** Returns true iff reviewer has reviewed the book. If the reviewer or book do not exist, returns false. */
  boolean gaveReview(String reviewerId, String bookId);

  /**
   * Returns the reviewer's review score for the book, if the reviewer reviewed the book.
   * If the reviewer or book do not exist, or the reviewer didn't review the book, returns empty.
   */
  OptionalDouble getScore(String reviewerId, String bookId);

  /**
   * Returns a <b>sorted</b> list of all the books the reviewer reviewed.
   * If the reviewer does not exist returns an empty list.
   */
  List<String> getReviewedBooks(String reviewerId);

  /**
   * Returns the grades of all the books the reviewer reviewed, as a map from book ID to its score.
   * If the reviewer does not exist returns an empty map.
   */
  Map<String, Integer> getAllReviewsByReviewer(String reviewerId);

  /**
   * Returns the average grade of the reviewer, across all reviewed books.
   * If the reviewer does not exist or has no reviews, returns empty.
   */
  OptionalDouble getScoreAverageForReviewer(String reviewerId);

  /**
   * Returns a <b>sorted</b> list of all the reviewers that reviewed a book.
   * If the book does not exist, returns an empty list.
   */
  List<String> getReviewers(String bookId);

  /**
   * Returns the grades of all the reviewers who reviewed the book, as a map from reviewer ID to their review score.
   * If the book does not exist, return an empty map.
   */
  Map<String, Integer> getReviewsForBook(String bookId);

  /** Returns the average review score of the book. If the book does not exist, returns empty. */
  OptionalDouble getAverageReviewScoreForBook(String bookId);
}
