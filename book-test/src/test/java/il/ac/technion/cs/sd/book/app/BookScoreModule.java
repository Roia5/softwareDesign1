package il.ac.technion.cs.sd.book.app;

import Database.Reader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(BookScoreInitializer.class).to(BookScoreInitializerImpl.class);
    bind(BookScoreReader.class).to(BookScoreReaderImpl.class);

    bind(Reader.class).annotatedWith(Names.named("reviewer_filename"))
            .toInstance(new Reader(BookScoreInitializerImpl.reviewer_filename));
    bind(Reader.class).annotatedWith(Names.named("book_filename"))
            .toInstance(new Reader(BookScoreInitializerImpl.book_filename));
  }
}
