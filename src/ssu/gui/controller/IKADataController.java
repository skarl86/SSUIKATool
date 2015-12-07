package ssu.gui.controller;

import javafx.scene.control.TreeItem;
import ssu.gui.controller.entity.PatientDetailRow;
import ssu.object.*;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;
import ssu.object.test.*;

import java.util.*;

/**
 * Created by NCri on 2015. 11. 13..
 */
public class IKADataController extends IKAController implements IKADataRequestInterface {
    /**
     * 싱글톤 및 멀티 쓰레드 대비.
     */
    protected volatile static IKADataController uniqueInstance;

    public static IKADataController getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (IKADataController.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new IKADataController();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }
        return uniqueInstance;
    }

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
        String testValue;
        String testTextValue;
    }

    public class OpinionReferenceList{
        Long ruleId;
        String rule;
        String author;
        Long madeDate;
        Long modifiedDate;
    }

    /**
     * Manager
     */
    protected AtomManager atomManager = AtomManager.getInstance();
    protected RuleManager ruleManager = RuleManager.getInstance();
    protected TestItemManager testItemManager = TestItemManager.getInstance();
    protected PatientManager patientManager = PatientManager.getInstance();

    private static final String DELIMITER = "_";

    public static final String ANTECEDENT = "antecedent";
    public static final String CONSEQUENT = "consequent";

    public static enum DataType {
        PATIENT_LIST,
        PATIENT_DEFAULT_INFOMATION,
        PATIENT_DETAIL_INFOMATION,
        PATIENT_DIAGNOSIS
    }

    public Rule getRule(Long ruleId){
        return ruleManager.getAllRules().get(ruleId);
    }
    /**
     * "소견관련 Rule List"의 출력 될 정보 리스트.
     * @param patientRegId
     * @return 소견 정보 리스트 전달.
     */
    public List<OpinionReferenceList> getOpinionReferenceList(Long patientRegId, Long ruleId){
        List<OpinionReferenceList> pList = new ArrayList<OpinionReferenceList>();

        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(patientRegId.equals(pat.getRegId())) {
                for(Opinion opn : pat.getAllOpinions()){
                    if(this.ruleManager.getAllRules().containsKey(ruleId)){
                        Rule rule = this.ruleManager.getAllRules().get(ruleId);
                        OpinionReferenceList op = new OpinionReferenceList();
                        op.author = rule.getAuthor();
                        op.madeDate = rule.getMadeDate();
                        op.modifiedDate = rule.getModifiedDate();
                        op.rule = rule.printFormalFormat();
                        op.ruleId = rule.getId();
                        pList.add(op);
                    }
                }
            }
        }

//        for (Patient pat : this.patientManager.getAllPatients()){
//            if(patientRegId.equals(pat.getRegId())) {
//                for(Opinion opn : pat.getAllOpinions()){
//                    if(this.ruleManager.getAllRules().containsKey(ruleId)){
//                        Rule rule = this.ruleManager.getAllRules().get(ruleId);
//                        OpinionReferenceList op = new OpinionReferenceList();
//                        op.author = rule.getAuthor();
//                        op.madeDate = rule.getMadeDate();
//                        op.modifiedDate = rule.getModifiedDate();
//                        op.rule = rule.printFormalFormat();
//                        op.ruleId = rule.getId();
//                        pList.add(op);
//                    }
//                }
//            }
//        }

        return pList;
    }

    /**
     * "환자 소견"의 출력 될 정보 리스트.
     * @param patientRegId
     * @return 소견 내용 리스트 전달.
     */
    public List<String> getPatientOpinion(Long patientRegId){
        List<String> pList = new ArrayList<String>();

        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(patientRegId.equals(pat.getRegId())){
                for(Opinion opn : pat.getAllOpinions()){
                    pList.add(opn.getOpinion());
                }
            }
        }
//        for (Patient pat : this.patientManager.getAllPatients()){
//            if(patientRegId.equals(pat.getRegId())){
//                for(Opinion opn : pat.getAllOpinions()){
//                    pList.add(opn.getOpinion());
//                }
//            }
//        }
        return pList;
    }

    /**
     *
     * @param patientRegId
     * @param indexOfOpinion
     * @return
     */
    public List<OpinionReferenceList> getRuleReferenceListInOpinion(Long patientRegId, int indexOfOpinion){
        List<OpinionReferenceList> ruleListInOpinion = new ArrayList<OpinionReferenceList>();
        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(patientRegId.equals(pat.getRegId())){
                for(Long ruleId : pat.getAllOpinions().get(indexOfOpinion).getRules()){
                    Rule rule = this.ruleManager.getAllRules().get(ruleId);
                    OpinionReferenceList op = new OpinionReferenceList();
                    op.author = rule.getAuthor();
                    op.madeDate = rule.getMadeDate();
                    op.modifiedDate = rule.getModifiedDate();
                    op.rule = rule.printFormalFormat();
                    op.ruleId = rule.getId();
                    ruleListInOpinion.add(op);
                }
            }
        }
//        for (Patient pat : this.patientManager.getAllPatients()){
//            if(patientRegId.equals(pat.getRegId())){
//                for(Long ruleId : pat.getAllOpinions().get(indexOfOpinion).getRules()){
//                    Rule rule = this.ruleManager.getAllRules().get(ruleId);
//                    OpinionReferenceList op = new OpinionReferenceList();
//                    op.author = rule.getAuthor();
//                    op.madeDate = rule.getMadeDate();
//                    op.modifiedDate = rule.getModifiedDate();
//                    op.rule = rule.printFormalFormat();
//                    op.ruleId = rule.getId();
//                    ruleListInOpinion.add(op);
//                }
//            }
//        }

        return ruleListInOpinion;
    }

    /**
     * "환자 상세 정보"의 출력 될 정보 리스트.
     * @param patientRegId
     * @return
     */
    public List<PatientDetailListElement> getPatientsDetailList(Long patientRegId){
        List<PatientDetailListElement> pList = new ArrayList<PatientDetailListElement>();
        // 수정 필요..
        /*
        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(patientRegId.equals(pat.getRegId())){
                for(TestResult rst : pat.getAllTestResults()){
                    TestItem itm = rst.getTestItem();
                    PatientDetailListElement elm = new PatientDetailListElement();
                    elm.testName = itm.getName();
                    if(1 < rst.getTestValues().size()){
                        elm.testValue = rst.getTestValues().get(0).getTestValue();
                        elm.testTextValue = rst.getTestValues().get(1).getTestValue();
                    }else{
                        elm.testValue = rst.getTestValues().get(0).getTestValue();
                        elm.testTextValue = "-";
                    }
                    pList.add(elm);
                }
            }
        }
//        for(Patient pat : this.patientManager.getAllPatients()){
//            if(patientRegId.equals(pat.getRegId())){
//                for(TestResult rst : pat.getAllTestResults()){
//                    TestItem itm = rst.getTestItem();
//                    PatientDetailListElement elm = new PatientDetailListElement();
//                    elm.testName = itm.getName();
//                    if(1 < rst.getTestValues().size()){
//                        elm.testValue = rst.getTestValues().get(0).getTestValue();
//                        elm.testTextValue = rst.getTestValues().get(1).getTestValue();
//                    }else{
//                        elm.testValue = rst.getTestValues().get(0).getTestValue();
//                        elm.testTextValue = "-";
//                    }
//                    pList.add(elm);
//                }
//            }
//        }
        */
        return pList;
    }

    /**
     * "환자 기본 정보"의 출력 될 정보 리스트를 전달.
     * @param patientRegId
     * @return 환자 기본 정보.
     */
    public PatientDefaultListElement getPatientsDefaultList(Long patientRegId){

        PatientDefaultListElement elm = null;
        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(pat.getRegId().equals(patientRegId)){
                elm = new PatientDefaultListElement();
                elm.age = pat.getAge();
                elm.gender = pat.getGender();
                elm.name = pat.getName();

                break;
            }
        }
//        for(Patient pat : this.patientManager.getAllPatients()){
//            if(pat.getRegId().equals(patientRegId)){
//
//                elm = new PatientDefaultListElement();
//                elm.age = pat.getAge();
//                elm.gender = pat.getGender();
//                elm.name = pat.getName();
//
//                break;
//            }
//        }

        return elm;
    }

    /**
     * "환자목록"의 출력 될 날짜별 환자 정보 리스트를 전달.
     * @return 날짜별 환자 정보.
     */
    public Map<String, List<PatientListElement>> getPatientsList(){
        Map patientMap = new HashMap<String, PatientListElement>();
        ArrayList<PatientListElement> tempList = null;
        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            PatientListElement elm = new PatientListElement();
            elm.date = pat.getRegDate();
            elm.regId = pat.getRegId();
            elm.name = pat.getName();

            if(patientMap.containsKey(elm.date)){
                tempList = (ArrayList)patientMap.get(elm.date);
                tempList.add(elm);
                patientMap.put(elm.date, tempList);
            }
            tempList = new ArrayList<PatientListElement>();
            tempList.add(elm);
            patientMap.put(elm.date, tempList);
        }
//        for(Patient pat : this.patientManager.getAllPatients()){
//            PatientListElement elm = new PatientListElement();
//            elm.date = pat.getRegDate();
//            elm.regId = pat.getRegId();
//            elm.name = pat.getName();
//
//            if(patientMap.containsKey(elm.date)){
//                tempList = (ArrayList)patientMap.get(elm.date);
//                tempList.add(elm);
//                patientMap.put(elm.date, tempList);
//            }
//            tempList = new ArrayList<PatientListElement>();
//            tempList.add(elm);
//            patientMap.put(elm.date, tempList);
//        }

        return patientMap;
    }

    /**
     * DataManager Wrapping 메소드.
     */
    public void loadAtomList(){ this.atomManager.loadAtomList(); }
    public void loadRuleList(){ this.ruleManager.loadRuleList(atomManager.getAllAtoms()); }
    public void loadTestItemList(){ this.testItemManager.loadTestItemList(); }
    public void loadPatients() { this.patientManager.loadPatients( /*this.testItemManager.getAllTestItems()*/); } //수정필요.

    /**
     * 환자 소견과 관련 Rule을 지우는 메소드로써, 참조하는 Rule ID를 삭제하고 실질적인 Rule은 삭제하지 않는다.
     * @param indexOfOpinion 환자 소견 객체의 index.
     * @param ruleId 사용자가 선택한 Rule의 id
     * @return 정상적으로 삭제되면 true, 해당 소견에 없는 rule id면 false를 리턴.
     */
    public boolean deleteRule(int indexOfOpinion, Long patientId, Long ruleId) {

        Opinion opinion = null;

        // Rule의 Patient, Opinion의 레퍼런스 정보를 지움.
        Rule deleteRule = this.ruleManager.getAllRules().get(ruleId);
        deleteRule.removePatientOneOpinion(patientId, indexOfOpinion);

        // Patient의 Opinion에서 Rule의 레퍼런스 정보를 지움.
        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(pat.getRegId().equals(patientId)){
                opinion = pat.getAllOpinions().get(indexOfOpinion);
                break;
            }
        }
//        for(Patient pat : this.patientManager.getAllPatients()){
//            if(pat.getRegId().equals(patientId)){
//                opinion = pat.getAllOpinions().get(indexOfOpinion);
//                break;
//            }
//
//        }

        if (opinion != null) {
            ArrayList<Long> rules = opinion.getRules();

            for (Long id : rules) {
                if (id.equals(ruleId)) {
                    rules.remove(id);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Edit Dialog 창에서 OK를 누를 때 호출.
     * @return 편집 혹은 추가한 rule의 id.
     * - To-do Lists
     * -- 추후 리턴받은 rule id가 해당 소견에 이미 있는 것인지 없는 것인지 체크할 필요 있음.
     */
    public Long ruleEditDialogOK(ArrayList<String> antecedents, ArrayList<String> consequents, String author, Long patientId, int indexOfOpinion) {
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
            // 해당 Rule을 참조하는 환자 ID와 opinion id를 기록.
            newRule.addPatientOneOpinion(patientId, indexOfOpinion);
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

            // 해당 Rule을 참조하는 환자 ID와 opinion id를 기록.
            newRule.addPatientOneOpinion(patientId, indexOfOpinion);

            this.ruleManager.addRule(newRule);

        }
//        for(Patient pat : this.patientManager.getAllPatients()){
//            if(pat.getRegId().equals(patientId)){
//                Opinion opinion = pat.getAllOpinions().get(indexOfOpinion);
//                opinion.addRule(newRule.getId());
//                break;
//            }
//
//        }
        for (Map.Entry<Long, Patient> entry : this.patientManager.getAllPatients().entrySet()) {
            Patient pat = entry.getValue();
            if(pat.getRegId().equals(patientId)){
                Opinion opinion = pat.getAllOpinions().get(indexOfOpinion);
                opinion.addRule(newRule.getId());
                break;
            }
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
            String atomStr = entry.getKey();
            if (atomStr.startsWith(value) && !atomStr.contains("_")) {
                completionList.add(entry.getKey());
            }
        }

        return completionList;
    }

    /**
     * GraphView에서 사용자가 Atom을 입력할 때마다 입력한 값으로 시작하는 Atom들의 리스트를 리턴.
     * @param value 사용자가 입력한 값.
     * @return 사용자가 입력한 값으로 시작하는 Atom들의 리스트.
     */
    public ArrayList<String> getAtomCompletionListInGraphView(String value) {
        // 파라미터의 value로 각 Atom들을 비교(첫글자부터) 비교해서 해당되면 리스트에 삽입.
        ArrayList<String> completionList = new ArrayList<String>();

        for (Map.Entry<String, Atom> entry : this.atomManager.getAllAtoms().entrySet()) {
            String atomStr = entry.getKey();
            if (atomStr.startsWith(value)) {
                completionList.add(entry.getKey());
            }
        }

        return completionList;
    }

    /**
     * 모든 Atom 리스트를 리턴.
     * @return
     */
    public ArrayList<String> getAllAtomList() {
        ArrayList<String> atomList = new ArrayList<String>();

        for (Map.Entry<String, Atom> entry : this.atomManager.getAllAtoms().entrySet()) {
            Atom atom = entry.getValue();
            atomList.add(entry.getValue().getName());
        }

        return atomList;
    }

    /**
     * Value를 가지지 않는 모든 Atom 리스트를 리턴.
     * @return
     */
    public ArrayList<String> getAllAtomsExceptValueList() {
        ArrayList<String> atomList = new ArrayList<String>();

        for (Map.Entry<String, Atom> entry : this.atomManager.getAllAtoms().entrySet()) {
            Atom atom = entry.getValue();
            if (!atom.getName().contains("_"))  // _ 이면 Atom과 Value가 존재하는 것. Value가 존재하지 않은 Atom의 목록을 넘겨야함.
                atomList.add(entry.getValue().getName());
        }

        return atomList;
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
     * Formal Rule String을 파라미터로 같은 Rule을 리턴하는 메소드.
     * @param formalStr Formal Rule String
     * @return 존재하면 Rule 객체, 없으면 null
     */
    public Rule getRuleByFormalFormat(String formalStr) {
        HashMap<Long, Rule> allRules = RuleManager.getInstance().getAllRules();

        for (Map.Entry<Long, Rule> entry : allRules.entrySet()) {
            Rule rule = entry.getValue();

            if (rule.printFormalFormat().equals(formalStr)) {
                return rule;
            }
        }

        return null;
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

    /**
     * Atom명을 파라미터로 받아 해당 Atom에 맞는 value type의 string list를 리턴하는 인터페이스
     * @param atom
     * @return
     */
    public ArrayList<String> getAtomValueList(String atom) {
        ArrayList<String> list = new ArrayList<String>();

        // 1. Atom 리스트를 검색해서 이미 존재하는 Atom이면 해당 Atom의 StringValue들을 가져옴
        // 2. Atom 리스트에 없을 경우 동일한 이름의 검사항목이 있는지 검색 후 해당 검사항목의 StringValue들을 가져옴
        // 3. 없을 경우 기본 StringValue들을 가져옴
        HashMap<String, Atom> allAtoms = this.atomManager.getAllAtoms();
        if (allAtoms.containsKey(atom)) {
            for (String type : allAtoms.get(atom).getStringValues()) {
                list.addAll(Tags.getTypeList(type));
            }
        } else if (this.testItemManager.containsTestItem(atom)) {
            list.addAll(this.testItemManager.getStringValueByName(atom));
        } else {
            list.addAll(Tags.getTypeList(""));
        }

        return list;
    }

    /**
     * 파라미터로 받은 consequent를 가지는 Rule의 리스트를 리턴.
     * @param checkConsequents
     * @return
     */
    public ArrayList<Rule> getRuleByConsequent(ArrayList<String> checkConsequents) {
        ArrayList<Rule> ruleList = new ArrayList<Rule>();

        for (Map.Entry<Long, Rule> entry : this.ruleManager.getAllRules().entrySet()) {
            Rule rule = entry.getValue();

            // 1. Consequent의 이름으로 이루어진 List를 생성.
            ArrayList<String> consList = new ArrayList<String>();
            for (Atom cons : rule.getConsequents()) {
                consList.add(cons.getName());
            }

            // 2. CheckList를 생성해서 파라미터로 받은 consequent 리스트와 비교한 결과를 CheckList에 저장.
            ArrayList<Boolean> checkList = new ArrayList<Boolean>();
            for (String checkCons : checkConsequents) {
                checkList.add(consList.contains(checkCons));
            }

            // 3. 포함하지 않는 것이 없다면 해당 rule을 추가.
            if (!checkList.contains(false)) {
                ruleList.add(rule);
            }
        }

        return ruleList;
    }

    /**
     * Atom에 해당하는 Value값이 있으면 Value를 포함한 String 배열을 넘겨준다.
     * 없을 경우는 "" 값을 넣은 String 배열을 넘겨준다.
     * @param atom
     * @return
     */

    public String[] getAtomAndValue(Atom atom){
        String[] atomAndValue = new String[2];
        if(atom.getName().contains(DELIMITER)) {
            atomAndValue[0] = atom.getName().split(DELIMITER)[0];
            atomAndValue[1] = atom.getName().split(DELIMITER)[1];
        }else{
            atomAndValue[0] = atom.getName().split(DELIMITER)[0];
            atomAndValue[1] = "";
        }

        return atomAndValue;
    }

    /**
     * Atom에 해당하는 Value값이 있으면 Atom을 Key, Value를 값으로한 Map 객체를 넘겨준다.
     * 없을 경우는 Atom을 Key, Value를 ""값으로한 Map 객체을 넘겨준다.
     * @param atom
     * @return
     */
    private HashMap<String, String> makeAtomAndValue(Atom atom){
        HashMap<String, String> atomAndValue = new HashMap<String, String>();

        if(atom.getName().contains(DELIMITER)) {
            atomAndValue.put(atom.getName().split(DELIMITER)[0], atom.getName().split(DELIMITER)[1]);
        }else{
            atomAndValue.put(atom.getName().split(DELIMITER)[0], "");
        }

        return atomAndValue;
    }

    /**
     * Atom Formal 문자열 값을 넘기면 그에 해당하는 Atom과 Value 리스트를 넘겨준다.
     * Consequent 또는 Antecedent를 키로 하고 값에 대항하는 리스트에 Atom이 Key이고 Value가 값인
     * ArrayList를 Value로 갖는 Map객체를 넘겨준다.
     * @param atomFormalString
     * @return
     */
    public HashMap<String, ArrayList<HashMap<String, String>>> getAtomAndValue(String atomFormalString){
        HashMap<String, ArrayList<HashMap<String, String>>> anteAndConsEachValue = new HashMap<String, ArrayList<HashMap<String, String>>>();

        Rule rule = getRuleByFormalFormat(atomFormalString);

        for(Atom atom : rule.getAntecedents()){
            HashMap<String, String> atomAndValue = makeAtomAndValue(atom);

            if(anteAndConsEachValue.containsKey(ANTECEDENT)){
                anteAndConsEachValue.get(ANTECEDENT).add(atomAndValue);
            }else{
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                list.add(atomAndValue);
                anteAndConsEachValue.put(ANTECEDENT, list);
            }
        }

        for(Atom atom : rule.getConsequents()){
            HashMap<String, String> atomAndValue = makeAtomAndValue(atom);

            if(anteAndConsEachValue.containsKey(CONSEQUENT)){
                anteAndConsEachValue.get(CONSEQUENT).add(atomAndValue);
            }else{
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                list.add(atomAndValue);
                anteAndConsEachValue.put(CONSEQUENT, list);
            }
        }

        return anteAndConsEachValue;
    }

    /**
     *  파라미터로 받은 Rule을 참조하는 환자 목록을 리턴.
     * @param formalString Rule의 Formal 형태
     * @return
     */
    public ArrayList<Patient> getTempPatientList(String formalString){
        ArrayList<Patient> tempPatientList = new ArrayList<Patient>();

        Rule rule = getRuleByFormalFormat(formalString);
        HashMap<Long, Patient> patientMap = patientManager.getAllPatients();

        if(rule != null){
            // key -> patient id, values -> opinion index
            HashMap<Long, ArrayList<Integer>> patientsOpinions = rule.getByPatientsOpinions();

            for(Map.Entry<Long, ArrayList<Integer>> patientOp : patientsOpinions.entrySet()){
                Patient pat = patientMap.get(patientOp.getKey());

                Patient tempPat = new Patient(pat.getName(), pat.getAge(), pat.getGender(), pat.getRegId(), pat.getRegDate());
                tempPat.setAllTestResults(pat.getAllTestResults());
                for (Integer index : patientOp.getValue()) {
                    tempPat.addOpinion(pat.getAllOpinions().get(index));
                }

                tempPatientList.add(tempPat);
            }
        }

        return tempPatientList;
    }

    /**
     * 환자 ID에 해당하는 Condition이 존재하는지를 확인.
     * @param patientId
     * @param antecedents
     * @param consequents
     * @return 존재 여부를 Return한다.
     */
    public boolean checkExistedRuleByConditions(Long patientId, ArrayList<String> antecedents, ArrayList<String> consequents) {
        Rule rule = ruleManager.getExistedRuleByCondition(antecedents, consequents);
        Patient currentPat = patientManager.getAllPatients().get(patientId);

        for (Opinion op : currentPat.getAllOpinions()) {
            for (Long ruleId : op.getRules()) {
                if ((rule != null) &&(ruleId == rule.getId())) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * 환자 Id로 상세 정보를 TreeItem으로 만들어 리턴.
     * @param patientId
     * @return
     */
    public TreeItem<PatientDetailRow> getTestResultTreeByPatientId(Long patientId) {

        HashMap<String, TestComponent> testComponents = this.testItemManager.getAllTestItems();

        TreeItem<PatientDetailRow> rootNode = new TreeItem<>(new PatientDetailRow(patientId + "","",""));
        if (this.patientManager.getAllPatients().containsKey(patientId)) {
            Patient patient = this.patientManager.getAllPatients().get(patientId);

            // patient의 상세 항목 정보를 가져옴.
            ArrayList<TestResultComponent> testResultComponents = new ArrayList<>(patient.getAllTestResults().values());
            for (TestResultComponent testResultComponent : testResultComponents) {
                TestResultCategory testResultCategory = (TestResultCategory) testResultComponent;

                if (testResultCategory.getTestResultComponents().size() > 1) { // 서브 항목이 포함된 검사항목일 경우.
                    ArrayList<TestResultComponent> testResults = testResultCategory.getTestResultComponents();
                    TestResult parent = (TestResult) testResults.get(0);
//                    String result = "";
//                    for (TestValue testValue : parent.getTestValues()) {
//                        result += testValue.getTestValue() + " ";
//                    }

                    TestCategory parentTestCategory = (TestCategory) testComponents.get(testResultCategory.getCode());
                    TreeItem<PatientDetailRow> parentNode = null;
                    if (parent.getTestValues().size() > 1) { // 문자값이 있을 경우.
                        parentNode = new TreeItem<PatientDetailRow>(new PatientDetailRow(((TestItem) parentTestCategory.getTestComponents().get(0)).getName(),
                                parent.getTestValues().get(0).getTestValue(),
                                parent.getTestValues().get(1).getTestValue()));
                    } else {                                 // 문자값이 없을 경우.
                        parentNode = new TreeItem<PatientDetailRow>(new PatientDetailRow(((TestItem) parentTestCategory.getTestComponents().get(0)).getName(),
                                parent.getTestValues().get(0).getTestValue(),
                                "-"));
                    }



                    for (int i=1; i<parentTestCategory.getTestComponents().size(); i++) {
                        TestResult testResult = (TestResult) testResultCategory.getTestResultComponents().get(i);
                        TestItem testItem = (TestItem) parentTestCategory.getTestComponents().get(i);

//                        String value = "";
//                        for (TestValue testValue : testResult.getTestValues()) {
//                            value += testValue.getTestValue() + " ";
//                        }
                        TreeItem<PatientDetailRow> childNode = null;
                        if (testResult.getTestValues().size() > 1) { // 문자값이 있을 경우.
                            childNode = new TreeItem<PatientDetailRow>(new PatientDetailRow(testItem.getName(),
                                    testResult.getTestValues().get(0).getTestValue(),
                                    testResult.getTestValues().get(1).getTestValue()));
                        } else {                                 // 문자값이 없을 경우.
                            childNode = new TreeItem<PatientDetailRow>(new PatientDetailRow(testItem.getName(),
                                    testResult.getTestValues().get(0).getTestValue(),
                                    "-"));
                        }

                        parentNode.getChildren().add(childNode);

                    }

                    rootNode.getChildren().add(parentNode);
                } else {
//                    String label = "";
                    TestResult testResult = (TestResult) testResultCategory.getTestResultComponents().get(0);
                    TestCategory testCategory = (TestCategory) testComponents.get(testResult.getCode());
                    TestItem testItem = (TestItem) testCategory.getTestComponents().get(0);
//                    for (TestValue testValue : testResult.getTestValues()) {
//                        label += testValue.getTestValue() + " ";
//                    }
                    TreeItem<PatientDetailRow> childNode = null;
                    if (testResult.getTestValues().size() > 1) { // 문자값이 있을 경우.
                        childNode = new TreeItem<PatientDetailRow>(new PatientDetailRow(testItem.getName(),
                                testResult.getTestValues().get(0).getTestValue(),
                                testResult.getTestValues().get(1).getTestValue()));
                    } else {                                 // 문자값이 없을 경우.
                        childNode = new TreeItem<PatientDetailRow>(new PatientDetailRow(testItem.getName(),
                                testResult.getTestValues().get(0).getTestValue(),
                                "-"));
                    }
                    rootNode.getChildren().add(childNode);
                }
            }

            return rootNode;

        } else {
            return null;
        }

    }

    /**
     * 파라미터로 받은 소견의 문자열에서 포함된 검사항목의 이름을 리스트로 리턴
     * @param opinionFormalString
     * @return
     */
    public ArrayList<String> getHighlightElementByOpinion(String opinionFormalString){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<TestComponent> testComponents = new ArrayList<TestComponent>(this.testItemManager.getAllTestItems().values());

        for (TestComponent testComponent : testComponents) {
            TestCategory testCategory = (TestCategory) testComponent;

            for (TestComponent testComponent1 : testCategory.getTestComponents()) {
                TestItem testItem = (TestItem) testComponent1;

                if (opinionFormalString.contains(testItem.getName())) {
                    list.add(testItem.getName());
                }
            }
        }

        return list;
    }

    /**
     *
     * @param selectedOpinionIndex  현재 선택된 소견 인덱스.
     * @param curPatientId          현재 선택된 환자 ID.
     */
    public void saveRuleByPatientOpinion(int selectedOpinionIndex, Long curPatientId){
//        // 1. 환자 id와 소견 id를 통해 각각의 Rule list를 가져옴
//        // 2-1. 각 rule의 antecedent가 여러개인 경우
//        // 2-2. 각 rule의 antecedent가 하나인 경우
//        String line = "";
//        Patient patient = this.patientManager.getAllPatients().get(curPatientId);
//        if (patient != null) {
//            Opinion opinion = patient.getAllOpinions().get(selectedOpinionIndex);
//            if (opinion != null) {
//                for (Long index : opinion.getRules()) {
//                    Rule rule = this.ruleManager.getAllRules().get(index);
//                    if (rule != null) {
//                        if (rule.getAntecedents().size() > 1) {
//                            for (int i=0; i<rule.getAntecedents().size(); i++) {
//                                line += rule.getAntecedents().get(i).getName() + ":" + "J" + rule.getId();
//                                if (i < rule.getAntecedents().size() - 1) {
//                                    line += "\n";
//                                }
//                            }
//                        } else {
//                            line += rule.getAntecedents().get(0).getName() + ":" + "J" + rule.getId() + "\n";
//                        }
//                        line += "J" + rule.getId() + ":" + rule.getConsequents().get(0).getName() + "\n";
//                    }
//                }
//            }
//        }
    }


}
