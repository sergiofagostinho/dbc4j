package pt.unl.fct.di.dbc4j.examples.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  DictionaryTest.class,
  ImmutableListTest.class,
  NameListTest.class,
  QueueTest.class,
  RelaxedNameListTest.class,
  Simple2DCoordinatesTest.class,
  StackTest.class,
  WebUserTest.class
})
public class AllTests {
	// nothing to do...
}
