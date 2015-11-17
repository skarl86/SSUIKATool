package ssu.object.test.value.string;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class MTHFR_C677T extends StringValue {
    public MTHFR_C677T(String value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return this.value;
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_MTHFR_C677T + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
