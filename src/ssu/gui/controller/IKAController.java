package ssu.gui.controller;

import javafx.stage.Stage;
import ssu.object.*;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;
import ssu.object.test.TestItem;
import ssu.object.test.TestResult;

import java.util.*;

import static ssu.gui.controller.IKAController.DataType.PATIENT_LIST;

/**
 * Created by NCri on 2015. 11. 11..
 */
public class IKAController implements IKADataRequestInterface{

    public class PatientListElement{
        String date;
        Long regId;
        String name;
    }

    public class PatientDefaultListElement{
        String name;
        int age;
        String gender;
    }

    public class PatientDetailListElement{
        String testName;
        Long testValue;
    }

    /**
     * Manager
     */
    protected AtomManager atomManager = AtomManager.getInstance();
    protected RuleManager ruleManager = RuleManager.getInstance();
    protected TestItemManager testItemManager = TestItemManager.getInstance();
    protected PatientManager patientManager = PatientManager.getInstance();

    public static enum DataType {
        PATIENT_LIST,
        PATIENT_DEFAULT_INFOMATION,
        PATIENT_DETAIL_INFOMATION,
        PATIENT_DIAGNOSIS
    }

    /**
     * 생성자.
     */
//    public IKAController() { };

    /**
     * 싱글톤 및 멀티 쓰레드 대비.
     */
    private volatile static IKAController uniqueInstance;

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static IKAController getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (AtomManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new IKAController();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }


    public ArrayList getPatients(){
        return this.patientManager.getAllPatients();
    }


    // 미구현.
    private List<String> getPatientOpinion(Long patientRegId){
        for (Patient pat : this.patientManager.getAllPatients()){
            if(patientRegId == pat.getRegId()){
                for(Opinion opn : pat.getAllOpinions()){
                    opn.getOpinion();
                }
            }
        }
        return null;
    }
    private List<PatientDetailListElement> getPatientsDetailList(Long patientRegId){
        List<PatientDetailListElement> pList = new ArrayList<PatientDetailListElement>();

        for(Patient pat : this.patientManager.getAllPatients()){
            if(patientRegId == pat.getRegId()){
                for(TestResult rst : pat.getAllTestResults()){

                }
            }
        }
        return pList;
    }
    private List<PatientDefaultListElement> getPatientsDefaultList(){
        List<PatientDefaultListElement> pList = new ArrayList<PatientDefaultListElement>();

        for(Patient pat : this.patientManager.getAllPatients()){
            PatientDefaultListElement elm = new PatientDefaultListElement();
            elm.age = pat.getAge();
            elm.gender = pat.getGender();
            elm.name = pat.getName();

            pList.add(elm);
        }
        return pList;
    }

    private List<PatientListElement> getPatientsList(){
        List<PatientListElement> pList = new ArrayList<PatientListElement>();

        for(Patient pat : this.patientManager.getAllPatients()){
            PatientListElement elm = new PatientListElement();
            elm.date = pat.getRegDate();
            elm.regId = pat.getRegId();
            elm.name = pat.getName();

            pList.add(elm);
        }

        return pList;
    }

    /**
     * DataManager Wrapping 메소드.
     */
    public void loadAtomList(){ this.atomManager.loadAtomList(); }
    public void loadRuleList(){ this.ruleManager.loadRuleList(atomManager.getAllAtoms()); }
    public void loadTestItemList(){ this.testItemManager.loadTestItemList(); }
    public void loadPatients() { this.patientManager.loadPatients(this.testItemManager.getAllTestItems()); }


    /**
     * Edit Dialog 창에서 OK를 누를 때 호출.
     * @return 편집 혹은 추가한 rule의 id.
     * - To-do Lists
     * -- 추후 리턴받은 rule id가 해당 소견에 이미 있는 것인지 없는 것인지 체크할 필요 있음.
     */
    public Long ruleEditDialogOK(ArrayList<String> antecedents, ArrayList<String> consequents, String author) {
        // 1. Rule이 이미 존재하는 경우 -> 기존의 Rule을 사용하되 modifedtTime과 author를 변경.
        // 2. Rule이 존재하지 않는 경우
        // 2-1. Atom이 존재하는 경우 -> AtomManager에서 key값을 넘겨 해당 Atom을 받아와 처리.
        // 2-2. Atome이 존재하지 않는 경우 -> 새로운 Atom을 만들고 AtomManager에 추가한 후 그 Atom을 사용.
        GregorianCalendar gc = new GregorianCalendar();
        Long modifiedTime = gc.getTimeInMillis();

        Rule newRule = this.ruleManager.getExistedRuleByCondition(antecedents, consequents);
        if (newRule != null) {  // Rule이 이미 존재하는 경우.
            newRule.setAuthor(author);
            newRule.setModifiedDate(modifiedTime);
        } else {                // Rule이 존재하지 않는 경우.
            newRule = new Rule(this.ruleManager.getRuleNumber(), author, modifiedTime, modifiedTime);

            for (String antecedent : antecedents) {
                // WARN : 현재 Atom은 무조건 Class만 생성되도록 함.
                newRule.addAntecedent(this.atomManager.getAtomOrCreate(antecedent, Tags.ATOM_TYPE_CLASS));
            }
            for (String consequent : consequents) {
                // WARN : 현재 Atom은 무조건 Class만 생성되도록 함.
                newRule.addConsequent(this.atomManager.getAtomOrCreate(consequent, Tags.ATOM_TYPE_CLASS));
            }

            this.ruleManager.addRule(newRule);
        }

        return newRule.getId();
    }

    /**
     * 사용자가 Atom을 입력할 때마다 입력한 값으로 시작하는 Atom들의 리스트를 리턴.
     * @param value 사용자가 입력한 값.
     * @return 사용자가 입력한 값으로 시작하는 Atom들의 리스트.
     */
    public ArrayList<String> getAtomCompletionList(String value) {
        // 파라미터의 value로 각 Atom들을 비교(첫글자부터) 비교해서 해당되면 리스트에 삽입.
        ArrayList<String> completionList = new ArrayList<String>();

        for (Map.Entry<String, Atom> entry : this.atomManager.getAllAtoms().entrySet()) {
            if (entry.getKey().startsWith(value)) {
                completionList.add(entry.getKey());
            }
        }

        return completionList;
    }

    /**
     * 사용자가 Atom을 입력할 때마다 입력한 Atom들이 포함된 Rule들의 리스트를 리턴.
     * @param antecedents 사용자가 입력한 Atom들.
     * @param consequents 사용자가 입력한 Atom들.
     * @return 사용자가 입력한 Atom들이 존재하는 Rule들의 리스트.
     */
    public ArrayList<String> getRuleCompletionList(ArrayList<String> antecedents, ArrayList<String> consequents) {
        ArrayList<String> completionList = new ArrayList<String>();

        for (Map.Entry<Long, Rule> entry : this.ruleManager.getAllRules().entrySet()) {
            Rule rule = entry.getValue();

            if (rule.containAtoms(antecedents, consequents)) {
                completionList.add(rule.printFormalFormat());
            }
        }

        return completionList;
    }

    /**
     * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
     */
    public void applicationQuit(){
        // AtomManager 저장.
        this.atomManager.saveAtomList();

        // RuleManager 저장.
        this.ruleManager.saveRuleList();

        // TestItemManager 저장.
        this.testItemManager .saveTestItemList();

        // PatientManager 저장.
        this.patientManager.savePatients();
    }
}
