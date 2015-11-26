package ssu.object.patient;

import ssu.object.Savable;
import ssu.object.Tags;
import ssu.object.test.TestResult;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class Patient implements Savable {

    private String name;
    private int age;
    private String gender;
    private Long regId;
    private String regDate;
    private ArrayList<TestResult> allTestResults;
    private ArrayList<Opinion> allOpinions;

    public Patient(String name, int age, String gender, Long regID, String regDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.regId = regID;
        this.regDate = regDate;
        this.allTestResults = new ArrayList<TestResult>();
        this.allOpinions = new ArrayList<Opinion>();
    }

    /**
     * 파라미터로 받은 테스트 결과를 추가.
     * @param testResult
     * @return
     */
    public boolean addTestResult(TestResult testResult) {
        return this.allTestResults.add(testResult);
    }

    /**
     * 파라미터로 받은 테스트 결과를 제거.
     * @param testResult
     * @return
     */
    public boolean removeTestResult(TestResult testResult) {
        return  this.allTestResults.remove(testResult);
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
        // Testresult
        for (int i=0; i<allTestResults.size(); i++) {
            TestResult testResult = allTestResults.get(i);
            line += testResult.printSavingFormat();

            if (i < allTestResults.size() - 1) {
                line += Tags.PATIENT_TEST_RESULT_SPLITER;
            }
        }

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

    public ArrayList<TestResult> getAllTestResults() {
        return allTestResults;
    }

    public void setAllTestResults(ArrayList<TestResult> allTestResults) {
        this.allTestResults = allTestResults;
    }

    public ArrayList<Opinion> getAllOpinions() {
        return allOpinions;
    }

    public void setAllOpinions(ArrayList<Opinion> allOpinions) {
        this.allOpinions = allOpinions;
    }
}
