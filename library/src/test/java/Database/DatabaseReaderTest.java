package Database;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import java.util.*;


import static org.junit.Assert.*;

/**
 * Created by dani9590 on 25/04/17.
 */
public class DatabaseReaderTest {
    /*private DatabaseInterface mockInterface;
    private List<String> stringList;
    private DatabaseReader databaseReader;
    @Rule public Timeout globalTimeout = Timeout.seconds(10);
    @Before
    public void setup() {
        mockInterface = Mockito.mock(DatabaseInterface.class);
        stringList = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            stringList.add((String)args[0]);
            return null;
        }).when(mockInterface).appendLine(Mockito.any());
        databaseReader = new DatabaseReader(mockInterface);
    }

    @Test
    public void checkItemsSorted() throws Exception {

        String[] stringsNotSorted = new String[] {"134,73","00032,100","1501,40"};
        String[] stringsSorted = new String[] {"00032,100","134,73","1501,40"};
        long start = System.currentTimeMillis();
        databaseReader.insertStrings(stringsNotSorted);
        long end = System.currentTimeMillis();
        assertTrue(Arrays.asList(stringsSorted).equals(stringList));
        System.out.println(end - start);

    }
    @Test
    public void checkEmptyArrayInserted() throws Exception {
        databaseReader.insertStrings(new String[]{});
        Mockito.verify(mockInterface,Mockito.never()).appendLine(Mockito.any());
    }
    @Test
    public void insertNullThrowsException() {
        try {
            databaseReader.insertStrings(null);
        } catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    @Test
    public void checkFindReturnsCorrectValue() throws Exception {
        Mockito.when(mockInterface.read(0)).thenReturn("00032,100");
        Mockito.when(mockInterface.read(1)).thenReturn("134,73");
        Mockito.when(mockInterface.read(2)).thenReturn("1501,40");
        Mockito.when(mockInterface.numberOfLines()).thenReturn(3);
        assertEquals(databaseReader.find("00032"),"100");

    }

    @Test(expected = InterruptedException.class)
    public void checkThrownExceptionOnMissingElement() throws Exception {
        Mockito.when(mockInterface.read(0)).thenReturn("00032,100");
        Mockito.when(mockInterface.read(1)).thenReturn("134,73");
        Mockito.when(mockInterface.read(2)).thenReturn("1501,40");
        Mockito.when(mockInterface.numberOfLines()).thenReturn(3);
        databaseReader.find("0003");

    }

    @Test(expected = InterruptedException.class)
    public void testMillionStrings() throws Exception {
        Mockito.doAnswer(invocation -> {
            Thread.sleep(13);
            return "123,70";
        }).when(mockInterface).read(Mockito.anyInt());
        Mockito.doAnswer(invocation -> {
            Thread.sleep(100);
            return 1000000;
        }).when(mockInterface).numberOfLines();
        databaseReader.find("2000000");
    }

*/
}