package ssu.object.test.value;

import ssu.object.Savable;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public abstract class TestValue implements Savable {

    public String getTestValue() {
        throw new UnsupportedOperationException("Not yet supported TestValue getValue");
    }

    @Override
    public String printSavingFormat() {
        throw new UnsupportedOperationException("Not yet supported TestValue printSavingFormat");
    }

}
