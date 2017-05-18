package il.ac.technion.cs.sd.book.app;

import il.ac.technion.cs.sd.book.ext.LineStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dani9590 on 26/04/17.
 */
public class StorageForTest implements LineStorage {
    List<String> list = new ArrayList<>();
    @Override
    public void appendLine(String st) {
        list.add(st);
    }

    @Override
    public String read(int numberOfLine) throws InterruptedException {
        if (numberOfLine >= list.size())
            throw new InterruptedException();
        String item = list.get(numberOfLine);
        Thread.sleep(item.length());
        return item;
    }

    @Override
    public int numberOfLines() throws InterruptedException {
        Thread.sleep(100);
        return list.size();
    }
}
