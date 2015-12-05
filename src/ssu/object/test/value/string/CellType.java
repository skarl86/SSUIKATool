package ssu.object.test.value.string;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 12. 5..
 */
public class CellType extends StringValue {

    public CellType(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_CELL + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
