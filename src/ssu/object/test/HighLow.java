package ssu.object.test;

import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class HighLow extends StringValue {

    private String value;

    public HighLow(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_HIGHLOW + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
