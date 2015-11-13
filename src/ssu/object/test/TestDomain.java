package ssu.object.test;

import java.util.ArrayList;

/**
 * Exp : 검사의 domain 정보를 담는 class
 */
public class TestDomain {

    private String name;
    private String description;
    private TestChecker testChecker;
    private ArrayList<TestResult> allTestResults;

    public TestDomain(String name, String description, TestChecker testChecker) {
        this.name = name;
        this.description = description;
        this.testChecker = testChecker;
    }

    /*
    Getter & Setter
     */
    public void setAllTestResults(ArrayList<TestResult> allTestResults) {
        this.allTestResults = allTestResults;
    }
}
