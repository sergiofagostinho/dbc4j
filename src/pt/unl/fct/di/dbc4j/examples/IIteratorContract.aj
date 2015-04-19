package pt.unl.fct.di.dbc4j.examples;

public aspect IIteratorContract {

	public boolean IIterator.preNext() {
		return
			hasNext();
	}
}
