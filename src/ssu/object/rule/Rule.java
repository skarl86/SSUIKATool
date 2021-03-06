package ssu.object.rule;

import ssu.object.Savable;
import ssu.object.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class Rule implements Savable {

    private Long id;
    private String author;
    private Long madeDate;
    private Long modifiedDate;
    private ArrayList<ValuedAtom> antecedents;
    private ArrayList<ValuedAtom> consequents;
    // 해당 Rule과 관련된 환자와 소견 id를 저장.
    private HashMap<Long, ArrayList<Integer>> byPatientsOpinions;

    public Rule(Long id, String author, Long madeDate, Long modifiedDate) {
        this.id = id;
        this.author = author;
        this.madeDate = madeDate;
        this.modifiedDate = modifiedDate;
        this.antecedents = new ArrayList<ValuedAtom>();
        this.consequents = new ArrayList<ValuedAtom>();
        this.byPatientsOpinions = new HashMap<Long, ArrayList<Integer>>();
    }

    public Rule(Long id, String author) {
        this.id = id;
        this.author = author;
        this.madeDate = 0L;
        this.modifiedDate = 0L;
        this.antecedents = new ArrayList<ValuedAtom>();
        this.consequents = new ArrayList<ValuedAtom>();
        this.byPatientsOpinions = new HashMap<Long, ArrayList<Integer>>();
    }

    /**
     * 해당 Rule이 특정 Atom들을 포함하고 있는지 체크.
     * @param checkAntecedents
     * @param checkConsequents
     * @return 해당 조건들이 이 Rule의 subpart일 경우 true, 아니면 false
     */
    public boolean containAtoms(ArrayList<String> checkAntecedents, ArrayList<String> checkConsequents) {

        // 파라미터 모두 Atom이 없을 경우
        if (checkAntecedents.size() == 0 && checkConsequents.size() == 0) {
            return false;
        } else {
            // 1.
            ArrayList<String> antecedentStringList = new ArrayList<String>();
            ArrayList<String> consequentStringList = new ArrayList<String>();

            for (ValuedAtom antecedent : getAntecedents()) {
//                if (antecedent.getName().contains(Tags.ATOM_VALUE_SPLITER)) {
//                    antecedentStringList.add(antecedent.getName().substring(0, antecedent.getName().indexOf(Tags.ATOM_VALUE_SPLITER)));
//                }

                antecedentStringList.add(antecedent.getAtom().getName());
            }
            for (ValuedAtom consequent : getConsequents()) {
//                if (consequent.getName().contains(Tags.ATOM_VALUE_SPLITER)) {
//                    consequentStringList.add(consequent.getName().substring(0, consequent.getName().indexOf(Tags.ATOM_VALUE_SPLITER)));
//                }

                consequentStringList.add(consequent.getAtom().getName());
            }

            return (antecedentStringList.containsAll(checkAntecedents) && consequentStringList.containsAll(checkConsequents));
        }
    }

    /**
     * Exp: Rule의 formal한 형식을 출력.
     * @return Rule string.
     */
    public String printFormalFormat() {
        String ruleString = "";

        for (int i=0; i<this.antecedents.size(); i++) {
            ValuedAtom va = this.antecedents.get(i);
            String atomStr = va.printSavingFormat();
            ruleString += atomStr;
            if (i < this.antecedents.size() - 1) ruleString += ",";
        }

        ruleString += "->";

        for (int i=0; i<this.consequents.size(); i++) {
            ValuedAtom va = this.consequents.get(i);
            String atomStr = va.printSavingFormat();
            ruleString += atomStr;
            if (i < this.consequents.size() - 1) ruleString += ",";
        }


        return  ruleString;
    }

    @Override
    public String printSavingFormat() {
        String line = getId() + Tags.RULE_SPLITER;
        line += getAuthor() + Tags.RULE_SPLITER;
        line += getMadeDate() + Tags.RULE_SPLITER;
        line += getModifiedDate() + Tags.RULE_SPLITER;

        // Rule 형식.
        String semiRuleString = "";
        for (int i=0; i<getAntecedents().size(); i++) {
            ValuedAtom atom = getAntecedents().get(i);
            semiRuleString += atom.printSavingFormat();
            if (i < getAntecedents().size() - 1) {
                semiRuleString += Tags.RULE_ATOM_SPLITER;
            }
        }
        semiRuleString += Tags.RULE_THEN_SPLITER;
        for (int i=0; i<getConsequents().size(); i++) {
            ValuedAtom atom = getConsequents().get(i);
            semiRuleString += atom.printSavingFormat();
            if (i < getConsequents().size() - 1) {
                semiRuleString += Tags.RULE_ATOM_SPLITER;
            }
        }
        line += semiRuleString;

        if (!getByPatientsOpinions().isEmpty()) {
            line += Tags.RULE_SPLITER;
            // 참조하는 환자와 소견 id
            ArrayList<Long> patients = new ArrayList<Long>(getByPatientsOpinions().keySet());
            String patientOpStr = "";
            for (int i=0; i<patients.size(); i++) {
                Long patient = patients.get(i);
                patientOpStr += patient + Tags.RULE_PATIENT_OPINION_SPLITER;

                ArrayList<Integer> opinions = getByPatientsOpinions().get(patient);
                for (int j=0; j<opinions.size(); j++) {
                    patientOpStr += opinions.get(j);

                    if (j < opinions.size() - 1) {
                        patientOpStr += Tags.RULE_PATIENT_OPINION_SPLITER;
                    }
                }

                if (i < patients.size() - 1) {
                    patientOpStr += Tags.RULE_PATIENT_SPLITER;
                }
            }

            line += patientOpStr;
        }

        return line;
    }

    /**
     * 파라미터로 받은 atom을 antecedent 리스트에 추가.
     * @param atom
     * @return
     */
    public boolean addAntecedent(ValuedAtom atom) {
        return this.antecedents.add(atom);
    }

    /**
     * 파라미터로 받은 atom을 antecedent 리스트에서 제거.
     * @param atom
     * @return
     */
    public boolean removeAntecedent(ValuedAtom atom) {
        return this.antecedents.remove(atom);
    }

    /**
     * 파라미터로 받은 atom을 consequent 리스트에 추가.
     * @param atom
     * @return
     */
    public boolean addConsequent(ValuedAtom atom) {
        return this.consequents.add(atom);
    }

    /**
     * 파라미터로 받은 atom을 consequent 리스트에서 제거.
     * @param atom
     * @return
     */
    public boolean removeConsequent(ValuedAtom atom) {
        return this.consequents.remove(atom);
    }

    /**
     * 파라미터로 받은 환자 id와 optinon indices를 추가.
     * @param patientId
     * @param opinions
     */
    public void addPatientAllOpinion(Long patientId, ArrayList<Integer> opinions) {
        if (this.byPatientsOpinions.containsKey(patientId)) {
            ArrayList<Integer> dest = this.byPatientsOpinions.get(patientId);
            for (Integer opId : opinions) {
                if (!dest.contains(opId)) {
                    dest.add(opId);
                }
            }
        } else {
            this.byPatientsOpinions.put(patientId, opinions);
        }
    }

    /**
     * 파라미터로 받은 환자의 id와 소견 id를 추가.
     * @param patientId
     * @param indexOfOpinion
     */
    public void addPatientOneOpinion(Long patientId, int indexOfOpinion) {
        if (this.byPatientsOpinions.containsKey(patientId)) {
            ArrayList<Integer> opinions = this.byPatientsOpinions.get(patientId);
            if (!opinions.contains(indexOfOpinion)) {
                opinions.add(indexOfOpinion);
            }
        } else {
            ArrayList<Integer> opinions = new ArrayList<Integer>();
            opinions.add(indexOfOpinion);

            this.byPatientsOpinions.put(patientId, opinions);
        }
    }

    /**
     * 파라미터로 받은 환자의 id와 소견 id를 제거.
     * @param patientId
     * @param indexOfOpinion
     */
    public void removePatientOneOpinion(Long patientId, int indexOfOpinion) {
        if (this.byPatientsOpinions.containsKey(patientId)) {
            ArrayList<Integer> opinions = this.byPatientsOpinions.get(patientId);
            if (opinions.contains(indexOfOpinion)) {
                opinions.remove((Integer) indexOfOpinion);

                if (opinions.size() == 0) {
                    this.byPatientsOpinions.remove(patientId);
                }
            }
        }
    }

    /*
    Getter & Setter
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getMadeDate() {
        return madeDate;
    }

    public void setMadeDate(Long madeDate) {
        this.madeDate = madeDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ArrayList<ValuedAtom> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(ArrayList<ValuedAtom> antecedents) {
        this.antecedents = antecedents;
    }

    public ArrayList<ValuedAtom> getConsequents() {
        return consequents;
    }

    public void setConsequents(ArrayList<ValuedAtom> consequents) {
        this.consequents = consequents;
    }

    public HashMap<Long, ArrayList<Integer>> getByPatientsOpinions() {
        return byPatientsOpinions;
    }
}
