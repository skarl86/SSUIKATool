package ssu.gui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by NCri on 2015. 11. 11..
 */
public interface IKADataRequestInterface {

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
    public Long ruleEditDialogOK(ArrayList<String> antecedents, ArrayList<String> consequents, String author);

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
     * DataManager Wrapping 메소드.
     */
    public void loadAtomList();
    public void loadRuleList();
    public void loadTestItemList();
    public void loadPatients();
}
