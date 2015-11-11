package ssu.object.patient;

import ssu.object.Savable;
import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class Opinion implements Savable {

    private String opinion;
    private ArrayList<Long> rules;

    public Opinion(String opinion) {
        this.opinion = opinion;
        this.rules = new ArrayList<Long>();
    }

    @Override
    public String printSavingFormat() {
        String line = getOpinion();
        if (rules.size() > 0) {
            line += Tags.PATIENT_OPINION_RULELIST_SPLITER;
        }

        for (int i=0; i<rules.size(); i++) {
            Long ruleId = rules.get(i);
            line += ruleId;

            if (i < rules.size() - 1) {
                line += Tags.PATIENT_OPINION_RULE_SPLITER;
            }
        }

        return line;
    }

    /**
     * 소견과 관련된 rule의 id를 리스트에 추가.
     * @param ruleId 추가할 rule의 id.
     * @return 성공여부(무조건 true).
     */
    public boolean addRule(Long ruleId) {
        return this.rules.add(ruleId);
    }

    /**
     * 소견과 관련된 rule의 id를 리스트에서 제거.
     * @param ruleId 제거할 rule의 id.
     * @return 제거하면 true, 없는 rule이면 false.
     */
    public boolean removeRule(Long ruleId) {
       return this.rules.remove(ruleId);
    }

    /*
    Getter & Setter
     */
    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public ArrayList<Long> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Long> rules) {
        this.rules = rules;
    }
}