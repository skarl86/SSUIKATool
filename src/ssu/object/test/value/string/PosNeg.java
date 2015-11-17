package ssu.object.test.value.string;

import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class PosNeg extends StringValue {

    public PosNeg(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_POSNEG + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
