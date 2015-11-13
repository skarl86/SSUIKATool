package ssu.object.test;

/**
 * Created by JasonHong on 2015. 11. 13..
 */
public class LiverTestChecker extends TestChecker {


    public LiverTestChecker() {}

    @Override
    public boolean checkAbnormal(int age, String gender, TestResult testResult) {
        // TestResult 항목에 따라 또 다른 method 호출하도록 구성 필요.

        return false;
    }
}
