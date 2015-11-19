package ssu.object.rule;

import ssu.object.Savable;
import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class Rule implements Savable {

    private Long id;
    private String author;
    private Long madeDate;
    private Long modifiedDate;
    private ArrayList<Atom> antecedents;
    private ArrayList<Atom> consequents;

    public Rule(Long id, String author, Long madeDate, Long modifiedDate) {
        this.id = id;
        this.author = author;
        this.madeDate = madeDate;
        this.modifiedDate = modifiedDate;
        this.antecedents = new ArrayList<Atom>();
        this.consequents = new ArrayList<Atom>();
    }

    public Rule(Long id, String author) {
        this.id = id;
        this.author = author;
        this.madeDate = 0L;
        this.modifiedDate = 0L;
        this.antecedents = new ArrayList<Atom>();
        this.consequents = new ArrayList<Atom>();
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
            ArrayList<String> antecedentStringList = new ArrayList<String>();
            ArrayList<String> consequentStringList = new ArrayList<String>();

            for (Atom antecedent : getAntecedents()) {
                antecedentStringList.add(antecedent.getName());
            }
            for (Atom consequent : getConsequents()) {
                consequentStringList.add(consequent.getName());
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
            Atom atom = this.antecedents.get(i);
            ruleString += atom.getName();
            if (i < this.antecedents.size() - 1) ruleString += ",";
        }

        ruleString += "->";

        for (int i=0; i<this.consequents.size(); i++) {
            Atom atom = this.consequents.get(i);
            ruleString += atom.getName();
            if (i < this.consequents.size() - 1) ruleString += ",";
        }


        return  ruleString;
    }

    /**
     * Exp: Rule의 저장 format으로 출력.
     * @return
     */
    @Override
    public String printSavingFormat() {
        String line = getId() + Tags.RULE_SPLITER;
        line += getAuthor() + Tags.RULE_SPLITER;
        line += getMadeDate() + Tags.RULE_SPLITER;
        line += getModifiedDate() + Tags.RULE_SPLITER;

        String semiRuleString = "";
        for (int i=0; i<getAntecedents().size(); i++) {
            Atom atom = getAntecedents().get(i);
            semiRuleString += atom.getName();
            if (i < getAntecedents().size() - 1) {
                semiRuleString += Tags.RULE_ATOM_SPLITER;
            }
        }
        semiRuleString += Tags.RULE_THEN_SPLITER;
        for (int i=0; i<getConsequents().size(); i++) {
            Atom atom = getConsequents().get(i);
            semiRuleString += atom.getName();
            if (i < getConsequents().size() - 1) {
                semiRuleString += Tags.RULE_ATOM_SPLITER;
            }
        }
        line += semiRuleString;

        return line;
    }

    public boolean addAntecedent(Atom atom) {
        return this.antecedents.add(atom);
    }

    public boolean removeAntecedent(Atom atom) {
        return this.antecedents.remove(atom);
    }

    public boolean addConsequent(Atom atom) {
        return this.consequents.add(atom);
    }

    public boolean removeConsequent(Atom atom) {
        return this.consequents.remove(atom);
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

    public ArrayList<Atom> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(ArrayList<Atom> antecedents) {
        this.antecedents = antecedents;
    }

    public ArrayList<Atom> getConsequents() {
        return consequents;
    }

    public void setConsequents(ArrayList<Atom> consequents) {
        this.consequents = consequents;
    }

}
