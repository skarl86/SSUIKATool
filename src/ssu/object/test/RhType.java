package ssu.object.test;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class RhType extends StringValue {
    public RhType(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_RH + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
