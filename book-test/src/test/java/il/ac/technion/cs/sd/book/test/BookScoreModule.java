package il.ac.technion.cs.sd.book.test;

import Database.Reader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.app.BookScoreReaderImpl;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
  private LineStorageFactory lsf = null;
  BookScoreModule() {}
  BookScoreModule(LineStorageFactory lsf) {
    this.lsf = lsf;
  }
  @Override
  protected void configure() {
    bind(BookScoreInitializer.class).to(BookScoreInitializerImpl.class);
    bind(BookScoreReader.class).to(BookScoreReaderImpl.class);
    if (lsf == null) {
      bind(Reader.class).annotatedWith(Names.named("reviewer_filename"))
              .toInstance(new Reader(BookScoreInitializerImpl.reviewer_filename));
      bind(Reader.class).annotatedWith(Names.named("book_filename"))
              .toInstance(new Reader(BookScoreInitializerImpl.book_filename));
      bind(Reader.class).annotatedWith(Names.named("reviewer_data_filename"))
              .toInstance(new Reader(BookScoreInitializerImpl.reviewer_data_filename));
      bind(Reader.class).annotatedWith(Names.named("book_data_filename"))
              .toInstance(new Reader(BookScoreInitializerImpl.book_data_filename));
    } else {
      bind(Reader.class).annotatedWith(Names.named("reviewer_filename"))
              .toInstance(new Reader(lsf,BookScoreInitializerImpl.reviewer_filename));
      bind(Reader.class).annotatedWith(Names.named("book_filename"))
              .toInstance(new Reader(lsf,BookScoreInitializerImpl.book_filename));
      bind(Reader.class).annotatedWith(Names.named("reviewer_data_filename"))
              .toInstance(new Reader(lsf,BookScoreInitializerImpl.reviewer_data_filename));
      bind(Reader.class).annotatedWith(Names.named("book_data_filename"))
              .toInstance(new Reader(lsf,BookScoreInitializerImpl.book_data_filename));
    }
  }
}