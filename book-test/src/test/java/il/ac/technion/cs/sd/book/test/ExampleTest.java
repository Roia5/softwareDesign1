package il.ac.technion.cs.sd.book.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.File;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class ExampleTest {
  @Rule public Timeout globalTimeout = Timeout.seconds(30);

  private static BookScoreReader setupAndGetReader(String fileName) throws Exception {
    String fileContents =
        new Scanner(new File(ExampleTest.class.getResource(fileName).getFile())).useDelimiter("\\Z").next();
    Injector injector = Guice.createInjector(new BookScoreModule(), new TestingLineStorageModule());
    injector.getInstance(BookScoreInitializer.class).setup(fileContents);
    return injector.getInstance(BookScoreReader.class);
  }

  @Test
  public void testSimple() throws Exception {
    BookScoreReader reader = setupAndGetReader("small.xml");
    assertEquals(Arrays.asList("Boobar", "Foobar", "Moobar"), reader.getReviewedBooks("123"));
    assertEquals(OptionalDouble.of(6.0), reader.getScoreAverageForReviewer("123"));
    assertEquals(OptionalDouble.of(10.0), reader.getAverageReviewScoreForBook("Foobar"));
  }
}
