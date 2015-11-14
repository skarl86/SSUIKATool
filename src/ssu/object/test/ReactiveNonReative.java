package ssu.object.test;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class ReactiveNonReative extends StringValue {
    public ReactiveNonReative(String value) {
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
