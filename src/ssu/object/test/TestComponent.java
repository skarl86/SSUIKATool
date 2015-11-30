package ssu.object.test;

import ssu.object.Savable;

/**
 * Created by JasonHong on 2015. 11. 30..
 */
public abstract class TestComponent implements Savable {

    public String print() {
        throw new UnsupportedOperationException();
    }

    public void add(TestComponent testComponent) {
        throw new UnsupportedOperationException();
    }

    public void remove(TestComponent testComponent) {
        throw new UnsupportedOperationException();
    }

    public int count() {
        throw new UnsupportedOperationException();
    }

    public String getCode() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(String name) {
        throw new UnsupportedOperationException();
    }

}
