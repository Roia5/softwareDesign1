package il.ac.technion.cs.sd.book.app;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

/**
 * Created by dani9590 on 18/05/17.
 */
public class PerformanceTestFactory implements LineStorageFactory {
    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {
        return new StorageForTest();
    }
}
