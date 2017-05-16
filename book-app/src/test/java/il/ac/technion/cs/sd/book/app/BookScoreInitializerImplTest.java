package il.ac.technion.cs.sd.book.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Scanner;
/**
 * Created by Roey on 14/05/2017.
 */
public class BookScoreInitializerImplTest {
    @Test
    public void setup() throws Exception {
        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("small.xml").getFile())).useDelimiter("\\Z").next();
        new BookScoreInitializerImpl().setup(fileContents);
    }

}