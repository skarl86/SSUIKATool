package ssu.object.test;

import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by JasonHong on 2015. 11. 30..
 */
public class TestResultCategory extends TestResultComponent {

    private ArrayList<TestResultComponent> testResultComponents;
    private String code;

    public TestResultCategory(String code) {
        this.testResultComponents = new ArrayList<TestResultComponent>();
        this.code = code;
    }

    public TestResultCategory(TestComponent testComponent) {
        this.testResultComponents = new ArrayList<TestResultComponent>();
    }

    @Override
    public void add(TestResultComponent testResultComponent) {
        this.testResultComponents.add(testResultComponent);
    }

    @Override
    public void remove(TestResultComponent testResultComponent) {
        this.testResultComponents.remove(testResultComponent);
    }

    @Override
    public String printSavingFormat() {
        String line = "";

        for (int i=0; i<this.testResultComponents.size(); i++) {
            line += getCode() + Tags.PATIENT_TEST_VALUE_SPLITER + this.testResultComponents.get(i).printSavingFormat();

            if (i < this.testResultComponents.size() - 1) {
                line += Tags.PATIENT_TEST_RESULT_SPLITER;
            }
        }

        return line;
    }

    @Override
    public String print() {
        return "";
    }

    @Override
    public int count() {
        return 0;
    }

    /*
    Getter & Setter
     */

    public ArrayList<TestResultComponent> getTestResultComponents() {
        return testResultComponents;
    }

    public String getCode() {
        return code;
    }
}
