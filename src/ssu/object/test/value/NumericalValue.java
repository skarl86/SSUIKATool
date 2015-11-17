package ssu.object.test.value;

import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class NumericalValue extends TestValue {

    private double value;

    public NumericalValue(double value) {
        this.value = value;
    }

    @Override
    public String getTestValue() {
        return Double.toString(value);
    }

    @Override
    public String printSavingFormat() {
        String line = Tags.TEST_VALUE_TYPE_NUMERIC + Tags.TEST_VALUE_SPLITER + getTestValue();

        return line;
    }
}
