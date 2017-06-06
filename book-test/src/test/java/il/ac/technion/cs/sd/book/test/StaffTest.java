package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.app.BookScoreReader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static il.ac.technion.cs.sd.book.test.TestUtils.mainTest;
import static il.ac.technion.cs.sd.book.test.TestUtils.mapFrom;
import static il.ac.technion.cs.sd.book.test.TestUtils.setupAndGetReader;
import static il.ac.technion.cs.sd.book.test.TestUtils.toEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StaffTest {
  @Test
  public void gaveReview() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      assertTrue(reader.gaveReview("rVFp6", "b08wUI"));
      //assertFalse(reader.gaveReview("rvfP6", "b08wUI"));
      //assertFalse(reader.gaveReview("rVFp6", "b08Wui"));
    });
  }

  @Test
  public void getScore() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      assertEquals(9, (int) reader.getScore("rYDEh", "bHRKG2ToyI").getAsDouble());
      assertFalse(reader.getScore("rYDEh", "bhrkg2tOYi").isPresent());
      assertFalse(reader.getScore("rydeH", "bAwySIk").isPresent());
    });
  }

  @Test
  public void getScoreAverageForReviewer() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      assertEquals((6 + 8 + 7 + 1) / 4.0, reader.getScoreAverageForReviewer("rbhmWpZZ").getAsDouble(), 0.1);
      assertFalse("Non-existing reviewer should be empty", reader.getScoreAverageForReviewer("rBHMwPzz").isPresent());
      assertFalse("Reviewer with no reviews should be empty", reader.getScoreAverageForReviewer("rEXaN7WChk").isPresent());
    });
  }

  @Test
  public void getAverageReviewScoreForBook() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      assertEquals((1 + 3 + 8 + 1) / 4.0, reader.getAverageReviewScoreForBook("bwihl1P6oT").getAsDouble(), 0.1);
      assertFalse(reader.getScoreAverageForReviewer("bWIHL1p6Ot").isPresent());
    });
  }

  @Test
  public void getReviewedBooks() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      List<String> expectedReviewedBooks =
          Arrays.asList("bqEszCh", "becMA", "b0fEraiUde", "bcjHKK");
      Collections.sort(expectedReviewedBooks);
      assertEquals(expectedReviewedBooks, reader.getReviewedBooks("rp7NJuLz"));
      assertEquals("Non-existing reviewer should be empty", Collections.emptyList(), reader.getReviewedBooks("rP7njUlZ"));
      assertEquals("Reviewer with no reviews should be empty", Collections.emptyList(), reader.getReviewedBooks("r75e"));
    });
  }

  @Test
  public void getAllReviewsByReviewer() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      assertEquals(mapFrom(
          toEntry("bUDxlr", 4),
          toEntry("bMMlMiKo", 2),
          toEntry("bI5HJWmgtI", 2),
          toEntry("bJzZXvdCf", 3)),
          reader.getAllReviewsByReviewer("rg1p9YHd"));
      assertEquals("Non-existing reviewer should be empty", Collections.emptyMap(), reader.getAllReviewsByReviewer("rG1P9yhD"));
      assertEquals("Reviewer with no reviews should be empty", Collections.emptyMap(), reader.getAllReviewsByReviewer("rIaL"));
    });
  }

  @Test
  public void getReviewers() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      List<String> expectedReviewers = Arrays.asList("rMpe", "rCuvDDOmHL", "rZKLuJs", "rfSHK");
      Collections.sort(expectedReviewers);
      assertEquals(expectedReviewers, reader.getReviewers("bcT1QzyFfT"));
      assertEquals(Collections.emptyList(), reader.getReviewers("bCt1qZYfFt"));
    });
  }

  @Test
  public void getReviewsForBook() throws Exception {
    BookScoreReader reader = setupAndGetReader("medium.xml");
    mainTest(() -> {
      assertEquals(mapFrom(
          toEntry("r3GhgOOhf", 10),
          toEntry("r4mx2d6b5", 7),
          toEntry("rbNQTVL2", 9),
          toEntry("rySptBBfNE", 2)), reader.getReviewsForBook("bpx3s0E"));
      assertEquals(Collections.emptyMap(), reader.getReviewsForBook("bPX3S0e"));
    });
  }
  
  @Test
  public void largeMixTest1() throws Exception {
    BookScoreReader reader = setupAndGetReader("large.xml");
    mainTest(() -> {
      assertEquals(10, (int) reader.getAllReviewsByReviewer("rf58NNF").get("bId5qRz"));
      assertEquals(8, (int) reader.getReviewsForBook("bRuB").get("rCp8gTW"));
      assertFalse(reader.getScoreAverageForReviewer("rmbBwDDIwE").isPresent());
      assertEquals(6.5, reader.getScoreAverageForReviewer(reader.getReviewers("b34OWu3r").get(1)).getAsDouble(), 0.1);
    });
  }


  @Test
  public void largeMixTest2() throws Exception {
    BookScoreReader reader = setupAndGetReader("large.xml");
    mainTest(() -> {
      assertEquals((8 + 10 + 3 + 3) / 4.0,
          reader.getAverageReviewScoreForBook(reader.getReviewedBooks(reader.getReviewers("bmB8JLX").get(1)).get(0)).getAsDouble(),
          0.1);
      assertFalse(reader.getScore("r1swQnW7Qw", "bm9kBHMAAr").isPresent());
      assertFalse(reader.getAverageReviewScoreForBook("b3eJK3m").isPresent());
    });
  }
}
