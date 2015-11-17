package ssu.object.test.value.string;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class Found_NotFound extends StringValue {
    public Found_NotFound(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_FOUNDNOTFOUND + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
