package il.ac.technion.cs.sd.book.app;

import Database.Reader;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

/**
 * Created by Roey on 04/06/2017.
 */
class TestBookScoreModule extends AbstractModule {
    private LineStorageFactory lsf = null;
    TestBookScoreModule() {}
    TestBookScoreModule(LineStorageFactory lsf) {
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