package il.ac.technion.cs.sd.book.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.*;

public class BookScoreReaderTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);
    private BookScoreReader setup(boolean forPerformance) throws FileNotFoundException {
        Injector injector = Guice.createInjector(new TestBookScoreModule(forPerformance ? new PerformanceTestFactory() :new MockFactory()),
                forPerformance ? new PerformanceFactoryModule() : new MockFactoryModule());
        injector.getInstance(BookScoreInitializer.class).setup(new Scanner(new File(BookScoreReaderTest.class.
                getResource("small.xml").getFile())).useDelimiter("\\Z").next());
        return injector.getInstance(BookScoreReader.class);
    }

    @Test
    public void checkReviewerGaveReview()  throws Exception{
        BookScoreReader bookScoreReader = setup(false);
        assertTrue(bookScoreReader.gaveReview("123","1"));
        assertFalse(bookScoreReader.gaveReview("123", "100"));
        assertFalse(bookScoreReader.gaveReview("1222","1"));
    }

    @Test
    public void checkScoreCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        assertEquals(bookScoreReader.getScore("123","1"), OptionalDouble.of(4));
        assertEquals(bookScoreReader.getScore("123","5"), OptionalDouble.empty());
        assertEquals(bookScoreReader.getScore("121","5"), OptionalDouble.empty());
        assertEquals(bookScoreReader.getScore("123","3"), OptionalDouble.of(5));
    }

    @Test
    public void checkReviewedListCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        assertEquals(Arrays.asList("1", "2", "3"), bookScoreReader.getReviewedBooks("123"));
        assertEquals(new LinkedList<String>(), bookScoreReader.getReviewedBooks("12"));
    }
    @Test
    public void checkReviewsMapCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        Map<String,Integer> testMap = new HashMap<>();
        testMap.put("1", 4);
        testMap.put("2", 6);
        testMap.put("3", 5);
        assertEquals(testMap,bookScoreReader.getAllReviewsByReviewer("123"));
        assertEquals(new HashMap<String, Integer>(), bookScoreReader.getAllReviewsByReviewer("124"));
    }
    @Test
    public void checkReviewerAverageIsCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        Map<String, Integer> idGradeMap = bookScoreReader.getAllReviewsByReviewer("123");
        int sum = 0, avg;
        for(int integer : idGradeMap.values())
            sum += integer;
        avg = sum / idGradeMap.values().size();
        assertEquals(OptionalDouble.of(avg), bookScoreReader.getScoreAverageForReviewer("123"));
        assertEquals(OptionalDouble.empty(), bookScoreReader.getScoreAverageForReviewer("213"));
    }
    @Test
    public void checkReviewersListCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        assertEquals(Arrays.asList("123"), bookScoreReader.getReviewers("1"));
        assertEquals(Arrays.asList("123"), bookScoreReader.getReviewers("2"));
        assertEquals(Arrays.asList("123"), bookScoreReader.getReviewers("3"));
        assertEquals(new LinkedList<String>(), bookScoreReader.getReviewers("4"));
    }

    @Test
    public void checkBooksMapCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("123", 4);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("123", 6);
        Map<String, Integer> map3 = new HashMap<>();
        map3.put("123", 5);

        assertEquals(map1, bookScoreReader.getReviewsForBook("1"));
        assertEquals(map2, bookScoreReader.getReviewsForBook("2"));
        assertEquals(map3, bookScoreReader.getReviewsForBook("3"));
        assertEquals(new HashMap<String, Integer>(), bookScoreReader.getReviewsForBook("4"));
    }

    @Test
    public void checkBookAverageCorrect() throws Exception {
        BookScoreReader bookScoreReader = setup(false);
        assertEquals(OptionalDouble.of(4), bookScoreReader.getAverageReviewScoreForBook("1"));
        assertEquals(OptionalDouble.of(6), bookScoreReader.getAverageReviewScoreForBook("2"));
        assertEquals(OptionalDouble.of(5), bookScoreReader.getAverageReviewScoreForBook("3"));
        assertEquals(OptionalDouble.empty(), bookScoreReader.getAverageReviewScoreForBook("4"));
    }

    @Test
    public void checkPerformance() throws Exception {
        BookScoreReader bookScoreReader = setup(true);
        try {
            bookScoreReader.gaveReview("100000000", "1000000001");
        }catch (Exception e) {}
        try {
            printStringList(bookScoreReader.getReviewedBooks("100000000"));
        } catch (Exception e) {}
        try {
            printStringList(bookScoreReader.getReviewers("1000000001"));
        } catch (Exception e) {}
        try {
            bookScoreReader.getAllReviewsByReviewer("1000000000");
        } catch (Exception e) {}
        try {
            bookScoreReader.getScore("1000000000", "1000000001");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    private void printStringList(List<String> stringList) {
        for (String line : stringList)
            System.out.println(line);
    }






}