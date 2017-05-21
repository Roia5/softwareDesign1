package il.ac.technion.cs.sd.book.app;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import net.bytebuddy.implementation.bytecode.Throw;
import org.mockito.Mockito;

import java.util.*;

/**
 * Created by dani9590 on 18/05/17.
 */
public class PerformanceTestFactory implements LineStorageFactory {

    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new IndexOutOfBoundsException("sleeping was interrupted!");
        }
        LineStorage storage = new StorageForTest();

        if (s.equals(BookScoreInitializerImpl.book_filename)) {
            for (int i = 0; i < 100000; i++) {
                String st = "1000000000"+ " "+String.valueOf(i);
                storage.appendLine(st);
            }

        }

        if (s.equals(BookScoreInitializerImpl.book_data_filename)) {
            String st = "7 ";
            for (int i = 0; i < 100; i++) {
                st += String.valueOf(i+1000000000) + "-5" + (i == 99 ? "" : ",");
            }
            for (int i = 0; i < 100000; i++) {
                storage.appendLine(st);
            }
        }
        if (s.equals(BookScoreInitializerImpl.reviewer_filename)) {
            for (int i =0; i < 1000000; i++) {
                String st = "1000000000"+ " "+String.valueOf(i);
                storage.appendLine(st);
            }
        }

        if (s.equals(BookScoreInitializerImpl.reviewer_data_filename)) {
            String st = "7 ";
            for (int i = 0; i < 100; i++) {
                st += String.valueOf(i+1000000000) + "-5" + (i == 99 ? "" : ",");
            }
            for (int i = 0; i < 1000000; i++) {
                storage.appendLine(st);
            }
        }

        return storage;
    }
}
