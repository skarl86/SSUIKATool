package ssu.gui.controller;

import ssu.object.patient.Patient;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by NCri on 2015. 11. 11..
 */
public interface IKADataRequestInterface {

    /**
     * GraphView에서 사용자가 Atom을 입력할 때마다 입력한 값으로 시작하는 Atom들의 리스트를 리턴.
     * @param value 사용자가 입력한 값.
     * @return 사용자가 입력한 값으로 시작하는 Atom들의 리스트.
     */
    public ArrayList<String> getAtomCompletionListInGraphView(String value);

    /**
     * Formal Rule String을 파라미터로 같은 Rule을 리턴하는 메소드.
     * @param formalStr Formal Rule String
     * @return 존재하면 Rule 객체, 없으면 null
     */
    public Rule getRuleByFormalFormat(String formalStr);

    /**
     * Atom명을 파라미터로 받아 해당 Atom에 맞는 value type의 string list를 리턴하는 인터페이스
     * @param atom
     * @return
     */
    public ArrayList<String> getAtomValueList(String atom);

    /**
     * 파라미터로 받은 consequent를 가지는 Rule의 리스트를 리턴.
     * @param checkConsequents
     * @return
     */
    public ArrayList<Rule> getRuleByConsequent(ArrayList<String> checkConsequents);

    /**
     * Atom에 해당하는 Value값이 있으면 Value를 포함한 String 배열을 넘겨준다.
     * 없을 경우는 "" 값을 넣은 String 배열을 넘겨준다.
     * @param atom
     * @return
     */
    public String[] getAtomAndValue(Atom atom);

    /**
     * Atom Formal 문자열 값을 넘기면 그에 해당하는 Atom과 Value 리스트를 넘겨준다.
     * Consequent 또는 Antecedent를 키로 하고 값에 대항하는 리스트에 Atom이 Key이고 Value가 값인
     * ArrayList를 Value로 갖는 Map객체를 넘겨준다.
     * @param atomFormalString
     * @return
     */
    public HashMap<String, ArrayList<HashMap<String, String>>> getAtomAndValue(String atomFormalString);

    /**
     * 모든 Atom 리스트를 리턴.
     * @return
     */
    public ArrayList<String> getAllAtomList();

    /**
     * Value를 가지지 않는 모든 Atom 리스트를 리턴.
     * @return
     */
    public ArrayList<String> getAllAtomsExceptValueList();

    /**
     * 사용자가 Atom을 입력할 때마다 입력한 값으로 시작하는 Atom들의 리스트를 리턴.
     * @param value 사용자가 입력한 값.
     * @return 사용자가 입력한 값으로 시작하는 Atom들의 리스트.
     */
    public ArrayList<String> getAtomCompletionList(String value);

    /**
     * Edit Dialog 창에서 OK를 누를 때 호출.
     * @return 편집 혹은 추가한 rule의 id.
     * - To-do Lists
     * -- 추후 리턴받은 rule id가 해당 소견에 이미 있는 것인지 없는 것인지 체크할 필요 있음.
     */
    public Long ruleEditDialogOK(ArrayList<String> antecedents, ArrayList<String> consequents, String author, Long patientId, int indexOfOpinion);

    /**
     * 사용자가 Atom을 입력할 때마다 입력한 Atom들이 포함된 Rule들의 리스트를 리턴.
     * @param antecedents 사용자가 입력한 Atom들.
     * @param consequents 사용자가 입력한 Atom들.
     * @return 사용자가 입력한 Atom들이 존재하는 Rule들의 리스트.
     */
    public ArrayList<String> getRuleCompletionList(ArrayList<String> antecedents, ArrayList<String> consequents);
    public Map<String, List<IKADataController.PatientListElement>> getPatientsList();
    public IKADataController.PatientDefaultListElement getPatientsDefaultList(Long patientRegId);
    public List<IKADataController.OpinionReferenceList> getOpinionReferenceList(Long patientRegId, Long ruleId);

    /**
     * 환자 ID에 해당하는 Condition이 존재하는지를 확인.
     * @param patientId
     * @param antecedents
     * @param consequents
     * @return 존재 여부를 Return한다.
     */
    public boolean checkExistedRuleByConditions(Long patientId, ArrayList<String> antecedents, ArrayList<String> consequents);

    /**
     * Atom Formal 문자열을 가진 임시 환자 리스트를 넘겨준다.
     * @param formalString
     * @return
     */
    public ArrayList<Patient> getTempPatientList(String formalString);

    /**
     * DataManager Wrapping 메소드.
     */
    public void loadAtomList();
    public void loadRuleList();
    public void loadTestItemList();
    public void loadPatients();

    public Rule getRule(Long ruleId);

    /**
     * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
     */
    public void applicationQuit();

    /**
     * "환자 상세 정보"의 출력 될 정보 리스트.
     * @param patientRegId
     * @return
     */
    public List<IKADataController.PatientDetailListElement> getPatientsDetailList(Long patientRegId);

    /**
     * 환자의 소견을 가져온다.
     * @param patientRegId
     * @param indexOfOpinion
     * @return
     */
    public List<IKADataController.OpinionReferenceList> getRuleReferenceListInOpinion(Long patientRegId, int indexOfOpinion);

    /**
     * "환자 소견"의 출력 될 정보 리스트.
     * @param patientRegId
     * @return 소견 내용 리스트 전달.
     */
    public List<String> getPatientOpinion(Long patientRegId);

    /**
     *
     * @param selectedOpinionIndex  현재 선택된 소견 인덱스.
     * @param curPatientId          현재 선택된 환자 ID.
     */
    public void saveRuleByPatientOpinion(int selectedOpinionIndex, Long curPatientId);
}
