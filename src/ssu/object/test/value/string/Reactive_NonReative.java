package ssu.object.test.value.string;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class Reactive_NonReative extends StringValue {
    public Reactive_NonReative(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_REACTIVE_NONREACTIVE + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
