package il.ac.technion.cs.sd.book.app;

import com.google.inject.AbstractModule;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

/**
 * Created by dani9590 on 16/05/17.
 */
public class MockFactoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LineStorageFactory.class).to(MockFactory.class);
    }
}
