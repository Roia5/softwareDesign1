package il.ac.technion.cs.sd.book.app;

import org.junit.Test;


import java.io.File;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Roey on 14/05/2017.
 */
public class BookScoreInitializerImplTest {


    @Test
    public void testParseBasic() throws Exception {
        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("small.xml").getFile())).useDelimiter("\\Z").next();
        BookScoreInitializerImpl bookScoreInitializer = new BookScoreInitializerImpl();
        bookScoreInitializer.setup(fileContents);
        List<String> bookLines = bookScoreInitializer.getBookFileLines();
        List<String> reviewerLines = bookScoreInitializer.getReviewerFileLines();
        assertTrue(reviewerLines.size() == 1);
        assertEquals(reviewerLines.get(0),"123 6 Boobar-5,Foobar-10,Moobar-3");
        List<String> list = new ArrayList<>();
        list.add("Boobar 5 123-5");
        list.add("Foobar 10 123-10");
        list.add("Moobar 3 123-3");
        assertEquals(list, bookLines);
    }

    @Test
    public void checkParsingSavesLatest() throws Exception {
        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("test.xml").getFile())).useDelimiter("\\Z").next();
        BookScoreInitializerImpl bookScoreInitializer = new BookScoreInitializerImpl();
        bookScoreInitializer.setup(fileContents);
        List<String> bookLines = bookScoreInitializer.getBookFileLines();
        List<String> reviewerLines = bookScoreInitializer.getReviewerFileLines();
        assertTrue(reviewerLines.size() == 1);
        assertEquals(reviewerLines.get(0),"123 4 Foobar-5,Moobar-3");
        List<String> list = new ArrayList<>();
        list.add("Foobar 5 123-5");
        list.add("Moobar 3 123-3");
        assertEquals(list, bookLines);
    }
    @Test
    public void testLarge() throws Exception {
        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("testLarge.xml").getFile())).useDelimiter("\\Z").next();
        BookScoreInitializerImpl bookScoreInitializer = new BookScoreInitializerImpl();
        bookScoreInitializer.setup(fileContents);
        List<String> bookLines = bookScoreInitializer.getBookFileLines();
        List<String> reviewerLines = bookScoreInitializer.getReviewerFileLines();
        assertEquals(2,reviewerLines.size());
        assertEquals(Arrays.asList("123 4 Doobar-5,Foobar-5,Moobar-3","1234 5 Ahalan-8,Coobar-4,Doobar-10,Foobar-1")
                ,reviewerLines);
        assertEquals(Arrays.asList("Ahalan 8 1234-8","Coobar 4 1234-4","Doobar 7 123-5,1234-10","Foobar 3 123-5,1234-1","Moobar 3 123-3"),bookLines);

    }

}