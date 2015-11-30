package ssu.object.test;

import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by JasonHong on 2015. 11. 30..
 */
public class TestCategory extends TestComponent {

    private ArrayList<TestComponent> testComponents;
    private String code;

    public TestCategory(String code) {
        this.code = code;
        this.testComponents = new ArrayList<TestComponent>();
    }

    @Override
    public void add(TestComponent testComponent) {
        this.testComponents.add(testComponent);
    }

    @Override
    public void remove(TestComponent testComponent) {
        this.testComponents.remove(testComponent);
    }

    @Override
    public String printSavingFormat() {
        String line = "";

        for (int i=0; i<this.testComponents.size(); i++) {
            line += getCode() + Tags.TEST_ITEM_SPLITER + this.testComponents.get(i).printSavingFormat();

            if (i < this.testComponents.size() - 1) {
                line += "\n";
            }
        }

        return line;
    }

    @Override
    public String print() {
        return printSavingFormat();
    }

    @Override
    public int count() {
        int count = 0;

        for (TestComponent testComponent : this.testComponents) {
            count += testComponent.count();
        }

        return count;
    }

    @Override
    public boolean contains(String name) {
        for (TestComponent testComponent : this.testComponents) {
            if (testComponent.contains(name)) {
                return true;
            }
        }

        return false;
    }

    /*
    Getter & Setter
     */

    public ArrayList<TestComponent> getTestComponents() {
        return testComponents;
    }

    public void setTestComponents(ArrayList<TestComponent> testComponents) {
        this.testComponents = testComponents;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
