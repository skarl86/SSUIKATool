package ssu.object;

import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.test.*;
import ssu.object.test.value.*;
import ssu.object.test.value.string.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class PatientManager {

//    private ArrayList<Patient> allPatients;
    private HashMap<Long, Patient> allPatients;

    // 혹시 추후 멀티스레딩을 위한.
    private volatile static PatientManager uniqueInstance;

    private PatientManager() {
        this.allPatients = new HashMap<Long, Patient>();
    }

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static PatientManager getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (PatientManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PatientManager();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }

    /**
     *
     * @param allTestItems
     */
    public void loadPatients(HashMap<String, TestItem> allTestItems) {
        try {
            InputStream is = this.getClass().getResourceAsStream(Tags.PATIENT_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(Tags.PATIENT_SPLITER);
                /*
                 * Patient 형식
                 * 20150603_40373_김춘자_F_56_AST,N:25-ALT,N:29-ALP,N:75_AST, AST가 높습니다. 간 및 담도계 기능이상이 의심됩니다.:0-AST, ALT가 높습니다. 간 및 담도계 기능이상이 의심됩니다.:1,2
                 */
                String regDate = tokens[0];
                Long regId = Long.parseLong(tokens[1]);
                String name = tokens[2];

                String gender = tokens[3];
                int age = Integer.parseInt(tokens[4]);

                Patient newPatient = new Patient(name, age, gender, regId, regDate);
                // test 결과 parsing.
                if (tokens[5].contains(Tags.PATIENT_TEST_RESULT_SPLITER)) {
                    String[] tests = tokens[5].split(Tags.PATIENT_TEST_RESULT_SPLITER);

                    for (int i=0; i<tests.length; i++) {
                        newPatient.addTestResult(createTestResult(tests[i], allTestItems));
                    }
                } else {
                    newPatient.addTestResult(createTestResult(tokens[5], allTestItems));
                }

                // opinion parsing.
                if (tokens[6].contains(Tags.PATIENT_OPINION_SPLITER)) {
                    String[] opinions = tokens[6].split(Tags.PATIENT_OPINION_SPLITER);

                    for (int i=0; i<opinions.length; i++) {
                        newPatient.addOpinion(createOpinion(opinions[i]));
                    }
                } else {
                    newPatient.addOpinion(createOpinion(tokens[6]));
                }

                addPatient(newPatient);

                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param parseString
     * @return
     */
    private TestResult createTestResult(String parseString, HashMap<String, TestItem> allTestItems) {
        String[] testItemValues = parseString.split(Tags.PATIENT_TEST_VALUE_SPLITER);
        String testItemName = testItemValues[0];

        TestResult newTestResult = new TestResult(allTestItems.get(testItemName));

        if (testItemValues.length > 2) { // Numerical & String value가 같이 존재.
            newTestResult.addTestValue(createTestValue(testItemValues[1]));
            newTestResult.addTestValue(createTestValue(testItemValues[2]));
        } else {
            newTestResult.addTestValue(createTestValue(testItemValues[1]));
        }

        return newTestResult;
    }

    /**
     *
     * @param parseString
     * @return
     */
    private TestValue createTestValue(String parseString) {
        String[] testValueTypeValue = parseString.split(Tags.TEST_VALUE_SPLITER);

        String testValueType = testValueTypeValue[0];
        TestValue newTestValue = null;
        if (testValueType.equals(Tags.TEST_VALUE_TYPE_NUMERIC)) {
            newTestValue = new NumericalValue(Double.parseDouble(testValueTypeValue[1]));
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_HIGHLOW)){     // High, Low
            newTestValue = new HighLow(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_POSNEG)) {     // Positive, Negative
            newTestValue = new PosNeg(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_NORMAL)) {      // Normal
            newTestValue = new Normal(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_REACTIVE_NONREACTIVE)) {   // Reactive, Non-Reactive
            newTestValue = new Reactive_NonReative(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_MTHFR_C677T)) {
            newTestValue = new MTHFR_C677T(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_FOUNDNOTFOUND)) {   // Found, Not found
            newTestValue = new Found_NotFound(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_BLOOD)) {      // Blood type
            newTestValue = new BloodType(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_RH)) {
            newTestValue = new RhType(testValueTypeValue[1]);
        } else if (testValueType.equals(Tags.TEST_VALUE_TYPE_RANGE)) {
            newTestValue = new Range(testValueTypeValue[1]);
        } else { // 현재 없는 타입
            System.out.println("Unknown TestValue : " + testValueTypeValue[1]);
            newTestValue = new Etc(testValueTypeValue[1]);
        }

        return newTestValue;
    }

    /**
     *
     * @param parseString
     * @return
     */
    private Opinion createOpinion(String parseString) {
        Opinion newOpinion = null;
        if (parseString.contains(Tags.PATIENT_OPINION_RULELIST_SPLITER)) { // 소견에 대한 미리 지정한 rule이 있을 경우.
            String[] opinionRules = parseString.split(Tags.PATIENT_OPINION_RULELIST_SPLITER);
            String opinion = opinionRules[0];

            newOpinion = new Opinion(opinion);
            if (opinionRules[1].contains(Tags.PATIENT_OPINION_RULE_SPLITER)) {
                String[] ruleNums = opinionRules[1].split(Tags.PATIENT_OPINION_RULE_SPLITER);

                for (int i=0; i<ruleNums.length; i++) {
                    newOpinion.addRule(Long.parseLong(ruleNums[i]));
                }
            } else {
                newOpinion.addRule(Long.parseLong(opinionRules[1]));
            }
        } else {    // 소견에 대해 지정한 rule이 없을 경우.
            newOpinion = new Opinion(parseString);
        }

        return newOpinion;
    }


    /**
     *
     */
    public void savePatients() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(Tags.PATIENT_FILE_PATH).getPath()));

            for (Map.Entry<Long, Patient> entry : this.allPatients.entrySet()) {
                Patient patient = entry.getValue();

                bw.write(patient.printSavingFormat() + "\n");
            }
//            for (Patient patient : this.allPatients) {
//                bw.write(patient.printSavingFormat() + "\n");
//            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param patient
     * @return
     */
    public void addPatient(Patient patient) {

        this.allPatients.put(patient.getRegId(), patient);
//        return this.allPatients.add(patient);
    }

    /*
     * Getter & Setter
     */

    public HashMap<Long, Patient> getAllPatients() {
        return allPatients;
    }
}
