package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

class TestingLineStorageModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(LineStorageFactory.class).to(LineStorageFactoryImpl.class).in(Singleton.class);
  }
}
