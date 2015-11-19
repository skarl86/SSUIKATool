package ssu.parser;


import ssu.object.PatientManager;
import ssu.object.Tags;
import ssu.object.TestItemManager;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.test.TestItem;
import ssu.object.test.TestResult;
import ssu.object.test.value.NumericalValue;
import ssu.object.test.value.string.*;

import java.io.*;
import java.util.HashMap;

/**
 * Created by JasonHong on 2015. 11. 19..
 */
public class PatientParser {

    /**
     * Manager
     */
    static TestItemManager testItemManager = TestItemManager.getInstance();
    static PatientManager patientManager = PatientManager.getInstance();

    /**
     * Constants
     */
    static int PATIENT_INFO_COUNT = 5;
    static int PATIENT_INFO_REGDATE_INDEX = 0;
    static int PATIENT_INFO_REGID_INDEX = 1;
    static int PATIENT_INFO_NAME_INDEX = 2;
    static int PATIENT_INFO_GENDER_INDEX = 3;
    static int PATIENT_INFO_AGE_INDEX = 4;

    static int PATIENT_TESTVALUE_COUNT = 4;
    static int PATIENT_TESTVALUE_NAME_INDEX = 0;
    static int PATIENT_TESTVALUE_NUMERICAL_INDEX = 1;
    static int PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX = 2;
    static int PATIENT_TESTVALUE_HIGHLOW_INDEX = 3;


    public static void main(String[] args) {
        testItemManager.loadTestItemList();

        final File folder = new File("input/origin");
        listFilesForFolder(folder);

        testItemManager.saveTestItemList();

        patientManager.savePatients();

        patientManager.loadPatients(testItemManager.getAllTestItems());
    }

    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = br.readLine();
                    // 1. 환자 기본정보
                    String[] defValues = line.split(",", PATIENT_INFO_COUNT);
                    String name = defValues[PATIENT_INFO_NAME_INDEX];
                    int age = Integer.parseInt(defValues[PATIENT_INFO_AGE_INDEX]);
                    String gender = defValues[PATIENT_INFO_GENDER_INDEX];
                    Long regID = Long.parseLong(defValues[PATIENT_INFO_REGID_INDEX]);
                    String regDate = defValues[PATIENT_INFO_REGDATE_INDEX];

                    // 환자 객체 생성.
                    Patient newPatient = new Patient(name, age, gender, regID, regDate);

                    line = br.readLine();

                    // 2. 환자 검사결과 부분.
                    while (!line.equals("*")) {
                        String[] values = line.split(",", PATIENT_TESTVALUE_COUNT);

                        // 2-1. 환자 데이터에서 기존의 testitem 중에 없는 것을 추가.
                        HashMap<String, TestItem> allTestItems = testItemManager.getAllTestItems();
                        TestItem testItem = allTestItems.get(values[PATIENT_TESTVALUE_NAME_INDEX]);
                        if (testItem == null) {
                            // TestItem 객체 생성.
                            TestItem newTestItem = new TestItem(values[PATIENT_TESTVALUE_NAME_INDEX], values[PATIENT_TESTVALUE_NAME_INDEX] + "에 관한 검사항목입니다.");
                            // TestItem의 type을 지정함.
                            if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].isEmpty() && values[PATIENT_TESTVALUE_HIGHLOW_INDEX].isEmpty()) { // 문자값이 모두 존재하지 않는 경우.
                                // 예비처리를 위한 HL, POSNEG를 추가해줌.
                                newTestItem.addType(Tags.TEST_VALUE_TYPE_POSNEG);
                                newTestItem.addType(Tags.TEST_VALUE_TYPE_HIGHLOW);
                            } else if (!values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].isEmpty()) {     // High, Low를 제외한 나머지 문자값이 존재하는 경우.
                                if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_NEG) ||
                                        values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_POS)) { // Positive, Negative
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_POSNEG);
                                } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains("Norm")) {    // Norm
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_NORMAL);
                                } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains("-")) {       // Range
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_RANGE);
                                } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_FOUND)
                                        || values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_NOT_FOUND)) { // Found, Non found
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_FOUNDNOTFOUND);
                                } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_A) ||
                                            values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_B) ||
                                            values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_AB) ||
                                            values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_O)) {     // Blood type
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_BLOOD);
                                } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_REACTIVE) ||
                                            values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_NONREATIVE)) {
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_REACTIVE_NONREACTIVE);
                                } else {        // etc : 추후 타입이 추가될 수 있음.
                                    newTestItem.addType(Tags.TEST_VALUE_TYPE_ETC);
                                }
                            } else if (!values[PATIENT_TESTVALUE_HIGHLOW_INDEX].isEmpty()) {  //  // High, Low 문자값이 존재하는 경우.
                                newTestItem.addType(Tags.TEST_VALUE_TYPE_HIGHLOW);
                            }
                            testItemManager.addTestItem(newTestItem);
                            testItem = newTestItem;

                            System.out.println(values[PATIENT_TESTVALUE_NAME_INDEX] + "이 추가됨.");
                        }
                        // 2-2. 환자 객체에 TestResult 생성후 추가.
                        TestResult newTestResult = new TestResult(testItem);
                        // Numerial Value 추가.
                        newTestResult.addTestValue(new NumericalValue(Double.parseDouble(values[PATIENT_TESTVALUE_NUMERICAL_INDEX])));
                        // String Value 추가.
                        if (!values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].isEmpty()) {
                            if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_NEG) ||
                                    values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_POS)) { // Positive, Negative
                                newTestResult.addTestValue(new PosNeg(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains("Norm")) {    // Norm
                                newTestResult.addTestValue(new Normal(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_FOUND)
                                    || values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_NOT_FOUND)) { // Found, Non found
                                newTestResult.addTestValue(new Found_NotFound(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_A) ||
                                    values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_B) ||
                                    values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_AB) ||
                                    values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_BLOOD_O)) {     // Blood type
                                newTestResult.addTestValue(new BloodType(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_REACTIVE) ||
                                    values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains(Tags.TEST_VALUE_TYPE_NONREATIVE)) {
                                newTestResult.addTestValue(new Reactive_NonReative(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            } else if (values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].contains("-")) {       // Range
                                newTestResult.addTestValue(new Range(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            } else {        // etc : 추후 타입이 추가될 수 있음.
                                newTestResult.addTestValue(new Etc(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX]));
                            }
                        } else if (!values[PATIENT_TESTVALUE_HIGHLOW_INDEX].isEmpty()) {
                            newTestResult.addTestValue(new HighLow(values[PATIENT_TESTVALUE_HIGHLOW_INDEX]));
                        }
                        // TestResult를 환자 객체에 추가.
                        newPatient.addTestResult(newTestResult);

                        line = br.readLine();
                    }
                    // 3. "*" 제거.
                    line = br.readLine();
                    // 4. 소견 부분.
                    while (line != null) {
                        Opinion newOpinion = new Opinion(line);

                        // 소견 추가.
                        newPatient.addOpinion(newOpinion);

                        line = br.readLine();
                    }

                    // 5. PatientManager에 최종적으로 추가.
                    patientManager.addPatient(newPatient);

                    br.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
