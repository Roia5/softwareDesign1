package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LineStorageFactoryImpl implements LineStorageFactory {
  private static class LineStorageImpl implements LineStorage {
    private final List<String> list = new ArrayList<>();

    @Override
    public void appendLine(String s) {
      list.add(s);
    }

    @Override
    public String read(int lineNumber) throws InterruptedException {
      String $ = list.get(lineNumber);
      Thread.sleep($.length());
      return $;
    }

    @Override
    public int numberOfLines() throws InterruptedException {
      Thread.sleep(100);
      return list.size();
    }
  }

  private final Map<String, LineStorage> files = new HashMap<>();

  // this really should have thrown an interrupted exception :| oh well
  @Override
  public LineStorage open(String fileName) {
    try {
      Thread.sleep(files.size() * 100);
      if (!files.containsKey(fileName))
        files.put(fileName, new LineStorageImpl());
      return files.get(fileName);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
