package ssu.object.test;

import ssu.object.Savable;
import ssu.object.Tags;
import ssu.object.test.value.TestValue;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestResult extends TestResultComponent implements Savable {

    private boolean abnormal;
    private ArrayList<TestValue> testValues;
    private String code;

    public TestResult(String code) {
        this.abnormal = false;
        this.code = code;
        this.testValues = new ArrayList<TestValue>();
    }

    public boolean addTestValue(TestValue testValue) {
        return this.testValues.add(testValue);
    }

    public boolean isAbnormal() {
        return this.abnormal;
    }

    @Override
    public String printSavingFormat() {
        String line = "";
        line += getCode() + Tags.PATIENT_TEST_VALUE_SPLITER;
        for (int i=0; i<this.testValues.size(); i++) {
            TestValue testValue = this.testValues.get(i);
            line += testValue.printSavingFormat();

            if (i < this.testValues.size() - 1) {
                line += Tags.PATIENT_TEST_VALUE_SPLITER;
            }
        }

        return line;
    }

    /*
     * Getter & Setter
     */

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<TestValue> getTestValues() {
        return testValues;
    }

    public void setTestValues(ArrayList<TestValue> testValues) {
        this.testValues = testValues;
    }

}
