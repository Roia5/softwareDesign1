package il.ac.technion.cs.sd.book.app;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import org.mockito.Mockito;

/**
 * Created by dani9590 on 16/05/17.
 */
public class MockFactory implements LineStorageFactory {
    private static final String BOOK = BookScoreInitializerImpl.book_filename;
    private static final String REVIEWER = BookScoreInitializerImpl.reviewer_filename;
    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {
        LineStorage mockStorage = Mockito.mock(LineStorage.class);
        if (s.equals(REVIEWER)) {
            try {
                Mockito.when(mockStorage.read(Mockito.anyInt())).thenReturn("123 0");
                Mockito.doNothing().when(mockStorage).appendLine(Mockito.anyString());
                Mockito.when(mockStorage.numberOfLines()).thenReturn(1);
            } catch (InterruptedException e) {
                throw new IndexOutOfBoundsException("Mock threw exception!");
            }
        }
        if (s.equals(BookScoreInitializerImpl.reviewer_data_filename)) {
            try {
                Mockito.when(mockStorage.read(Mockito.anyInt())).thenReturn("5 1-4,2-6,3-5");
                Mockito.doNothing().when(mockStorage).appendLine(Mockito.anyString());
                Mockito.when(mockStorage.numberOfLines()).thenReturn(1);
            } catch (InterruptedException e) {
                throw new IndexOutOfBoundsException("Mock threw exception!");
            }
        }
        if (s.equals(BOOK)) {
            try {
                Mockito.when(mockStorage.read(0)).thenReturn("1 0");
                Mockito.when(mockStorage.read(1)).thenReturn("2 1");
                Mockito.when(mockStorage.read(2)).thenReturn("3 2");

                Mockito.doNothing().when(mockStorage).appendLine(Mockito.anyString());
                Mockito.when(mockStorage.numberOfLines()).thenReturn(3);
            } catch (InterruptedException e) {
                throw new IndexOutOfBoundsException("Mock threw exception!");
            }
        }
        if (s.equals(BookScoreInitializerImpl.book_data_filename)) {
            try {
                Mockito.when(mockStorage.read(0)).thenReturn("4 123-4");
                Mockito.when(mockStorage.read(1)).thenReturn("6 123-6");
                Mockito.when(mockStorage.read(2)).thenReturn("5 123-5");

                Mockito.doNothing().when(mockStorage).appendLine(Mockito.anyString());
                Mockito.when(mockStorage.numberOfLines()).thenReturn(3);
            } catch (InterruptedException e) {
                throw new IndexOutOfBoundsException("Mock threw exception!");
            }
        }

        return mockStorage;

    }
}
