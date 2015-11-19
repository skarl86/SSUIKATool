package ssu.object.test.value.string;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 19..
 */
public class Range extends StringValue {
    public Range(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_RANGE + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
