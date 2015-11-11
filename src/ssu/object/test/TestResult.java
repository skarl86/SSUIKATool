package ssu.object.test;

import ssu.object.Savable;
import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestResult implements Savable {

    private boolean abnormal;
    private TestItem testItem;
    private ArrayList<TestValue> testValues;

    public TestResult(TestItem testItem) {
        this.abnormal = false;
        this.testItem = testItem;
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
        line += getTestItem().getName() + Tags.PATIENT_TEST_VALUE_SPLITER;
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
    public TestItem getTestItem() {
        return testItem;
    }

    public void setTestItem(TestItem testItem) {
        this.testItem = testItem;
    }

    public ArrayList<TestValue> getTestValues() {
        return testValues;
    }

    public void setTestValues(ArrayList<TestValue> testValues) {
        this.testValues = testValues;
    }

}
