package ssu.object.test;

import ssu.object.Savable;

/**
 * Created by JasonHong on 2015. 11. 30..
 */
public abstract class TestResultComponent implements Savable {

    public String print() {
        throw new UnsupportedOperationException();
    }

    public void add(TestResultComponent testResultComponent) {
        throw new UnsupportedOperationException();
    }

    public void remove(TestResultComponent testResultComponent) {
        throw new UnsupportedOperationException();
    }

    public int count() {
        throw new UnsupportedOperationException();
    }

}
