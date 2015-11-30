package ssu.object.patient;

import ssu.object.Savable;
import ssu.object.Tags;
import ssu.object.test.TestComponent;
import ssu.object.test.TestResultCategory;
import ssu.object.test.TestResultComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class Patient implements Savable {

    private String name;
    private int age;
    private String gender;
    private Long regId;
    private String regDate;
    private HashMap<String, TestResultComponent> allTestResults;
    private ArrayList<Opinion> allOpinions;

    public Patient(String name, int age, String gender, Long regID, String regDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.regId = regID;
        this.regDate = regDate;
        this.allTestResults = new HashMap<String, TestResultComponent>();
        this.allOpinions = new ArrayList<Opinion>();
    }

    public void addTestResult(String key, TestResultComponent testResultComponent) {
        this.allTestResults.put(key, testResultComponent);
    }

    /**
     * 파라미터로 받은 소견을 추가.
     * @param opinion
     * @return
     */
    public boolean addOpinion(Opinion opinion) {
        return this.allOpinions.add(opinion);
    }

    /**
     * 파라미터로 받은 소견을 제거.
     * @param opinion
     * @return
     */
    public boolean removeOpinion(Opinion opinion) {
        return this.allOpinions.remove(opinion);
    }

    @Override
    public String printSavingFormat() {
        String line = getRegDate() + Tags.PATIENT_SPLITER;
        line += getRegId() + Tags.PATIENT_SPLITER;
        line += getName() + Tags.PATIENT_SPLITER;
        line += getGender() + Tags.PATIENT_SPLITER;
        line += getAge() + Tags.PATIENT_SPLITER;
        // TestResult component
        ArrayList<TestResultComponent> testResultComponents = new ArrayList<TestResultComponent>(this.allTestResults.values());
        for (int i=0; i<testResultComponents.size(); i++) {
            line += testResultComponents.get(i).printSavingFormat();

            if (i <testResultComponents.size() - 1) {
                line += Tags.PATIENT_TEST_RESULT_SPLITER;
            }
        }

//        for (int i=0; i<allTestResults.size(); i++) {
//            TestResult testResult = allTestResults.get(i);
//            line += testResult.printSavingFormat();
//
//            if (i < allTestResults.size() - 1) {
//                line += Tags.PATIENT_TEST_RESULT_SPLITER;
//            }
//        }

        line += Tags.PATIENT_SPLITER;

        // opinion
        for (int i=0; i<allOpinions.size(); i++) {
            Opinion opinion = allOpinions.get(i);
            line += opinion.printSavingFormat();

            if (i < allOpinions.size() - 1) {
                line += Tags.PATIENT_OPINION_SPLITER;
            }
        }

        return line;
    }

    /*
     * Getter & Setter
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getRegId() {
        return regId;
    }

    public void setRegId(Long regId) {
        this.regId = regId;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public HashMap<String, TestResultComponent> getAllTestResults() {
        return allTestResults;
    }

    public ArrayList<Opinion> getAllOpinions() {
        return allOpinions;
    }

    public void setAllOpinions(ArrayList<Opinion> allOpinions) {
        this.allOpinions = allOpinions;
    }
}
