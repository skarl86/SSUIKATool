package ssu.parser;


import ssu.object.PatientManager;
import ssu.object.Tags;
import ssu.object.TestItemManager;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.test.*;
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

    static int PATIENT_TESTVALUE_COUNT = 6;
    static int PATIENT_TESTVALUE_CODE_INDEX = 0;
    static int PATIENT_TESTVALUE_SUBCODE_INDEX = 1;
    static int PATIENT_TESTVALUE_NAME_INDEX = 2;
    static int PATIENT_TESTVALUE_NUMERICAL_INDEX = 3;
    static int PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX = 4;
    static int PATIENT_TESTVALUE_HIGHLOW_INDEX = 5;


    public static void main(String[] args) {
        testItemManager.loadTestItemList();

        final File folder = new File("input/origin");
        listFilesForFolder(folder);



        testItemManager.saveTestItemList();

        patientManager.savePatients();

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

                    System.out.println("환자 생성 : " + regDate + "_" + regID);

                    // 환자 객체 생성.
                    Patient newPatient = new Patient(name, age, gender, regID, regDate);

                    line = br.readLine();

                    // 2. 환자 검사결과 부분.
                    HashMap<String, TestComponent> allTestItems = testItemManager.getAllTestItems();
                    while (!line.equals("*")) {
                        String[] values = line.split(",", PATIENT_TESTVALUE_COUNT);

                        // 2-1. 환자 데이터에서 기존의 testitem 중에 없는 것을 추가.
                        TestComponent testComponent = allTestItems.get(values[PATIENT_TESTVALUE_CODE_INDEX]);

                        if (testComponent == null) {    // TestCategory 생성
                            // TestCategory 생성.
                            TestCategory newTestCategory = new TestCategory(values[PATIENT_TESTVALUE_CODE_INDEX]);

                            // TestTypes 생성.
                            TestItem newTestItem = testItemManager.createTestItem(values[PATIENT_TESTVALUE_SUBCODE_INDEX],
                                    values[PATIENT_TESTVALUE_NAME_INDEX],
                                    values[PATIENT_TESTVALUE_NAME_INDEX] + "에 관한 검사항목입니다.",
                                    Tags.getParsedType(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX], values[PATIENT_TESTVALUE_HIGHLOW_INDEX]));

                            newTestCategory.add(newTestItem);

                            allTestItems.put(newTestCategory.getCode(), newTestCategory);

                            System.out.println("Main : " + values[PATIENT_TESTVALUE_CODE_INDEX] + "_" + values[PATIENT_TESTVALUE_NAME_INDEX] + "이 추가됨.");
                        } else if (!values[PATIENT_TESTVALUE_SUBCODE_INDEX].isEmpty()) {
                            TestCategory testCategory = (TestCategory) allTestItems.get(values[PATIENT_TESTVALUE_CODE_INDEX]);

                            if (!testCategory.contains(values[PATIENT_TESTVALUE_NAME_INDEX])) {
                                TestItem testItem = testItemManager.createTestItem(values[PATIENT_TESTVALUE_SUBCODE_INDEX],
                                        values[PATIENT_TESTVALUE_NAME_INDEX],
                                        values[PATIENT_TESTVALUE_NAME_INDEX] + "에 관한 검사항목입니다.",
                                        Tags.getParsedType(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX], values[PATIENT_TESTVALUE_HIGHLOW_INDEX]));

                                testCategory.add(testItem);

                                System.out.println("Sub : " + values[PATIENT_TESTVALUE_CODE_INDEX] + "_" + values[PATIENT_TESTVALUE_NAME_INDEX] + "이 추가됨.");
                            }
                        }

                        // 2-2. 환자 객체에 TestResult 생성후 추가.
                        if (newPatient.getAllTestResults().containsKey(values[PATIENT_TESTVALUE_CODE_INDEX])) { // 이미 카테고리가 있는 경우.
                            TestResultCategory testResultCategory = (TestResultCategory) newPatient.getAllTestResults().get(values[PATIENT_TESTVALUE_CODE_INDEX]);
                            TestResult newTestResult = new TestResult(values[PATIENT_TESTVALUE_SUBCODE_INDEX]);
                            newTestResult.addTestValue(new NumericalValue(Double.parseDouble(values[PATIENT_TESTVALUE_NUMERICAL_INDEX])));
                            if (!values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].isEmpty() || !values[PATIENT_TESTVALUE_HIGHLOW_INDEX].isEmpty()) {
                                newTestResult.addTestValue(Tags.getParsedStringValue(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX], values[PATIENT_TESTVALUE_HIGHLOW_INDEX]));
                            }


                            testResultCategory.add(newTestResult);
                        } else {
                            TestResultCategory testResultCategory = new TestResultCategory(values[PATIENT_TESTVALUE_CODE_INDEX]);
                            TestResult newTestResult = new TestResult(values[PATIENT_TESTVALUE_SUBCODE_INDEX]);
                            newTestResult.addTestValue(new NumericalValue(Double.parseDouble(values[PATIENT_TESTVALUE_NUMERICAL_INDEX])));
                            if (!values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX].isEmpty() || !values[PATIENT_TESTVALUE_HIGHLOW_INDEX].isEmpty()) {
                                newTestResult.addTestValue(Tags.getParsedStringValue(values[PATIENT_TESTVALUE_EXCEPT_HIGHLOW_INDEX], values[PATIENT_TESTVALUE_HIGHLOW_INDEX]));
                            }

                            testResultCategory.add(newTestResult);

                            newPatient.getAllTestResults().put(testResultCategory.getCode(), testResultCategory);
                        }

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
