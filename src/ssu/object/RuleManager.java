package ssu.object;

import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class RuleManager {

    private Long ruleNumber;
    private HashMap<Long, Rule> allRules;

    // 혹시 추후 멀티스레딩을 위한.
    private volatile static RuleManager uniqueInstance;

    private RuleManager() {
        this.allRules = new HashMap<Long, Rule>();
    }

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static RuleManager getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (RuleManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new RuleManager();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }

    /**
     * Exp: RuleManager의 설정 파일 불러옴.
     * 현재 rule의 total count만 관리.
     */
    private void loadRuleConfigure() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Tags.RULE_CONFIGURE_FILE_PATH));
            String line = br.readLine();

            while (line != null) {
                this.ruleNumber = Long.parseLong(line);
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
     * Exp: RuleManager의 설정 파일 저장.
     * 현재 rule의 total count만 관리.
     */
    private void saveRuleConfigure() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Tags.RULE_CONFIGURE_FILE_PATH));
            bw.write(this.ruleNumber.toString());

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exp: 지정한 파일을 읽어와 rule list를 만듬.
     */
    public void loadRuleList(HashMap<String, Atom> allAtoms) {

        // configure 먼저 설정.
        loadRuleConfigure();

        try {
            BufferedReader br = new BufferedReader(new FileReader(Tags.RULE_FILE_PATH));
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(Tags.RULE_SPLITER);
                /*
                 * Rule 형식
                 * id,author,madeDate,modifiedDate,AST+ALT-LiverDisease
                 */
                Long id = Long.parseLong(tokens[0]);
                String author = tokens[1];
                Long madeDate = Long.parseLong(tokens[2]);
                Long modifedDate = Long.parseLong(tokens[3]);

                Rule newRule = new Rule(id, author, madeDate, modifedDate);
                String[] semiRuleTokens = tokens[4].split(Tags.RULE_THEN_SPLITER);

                if (semiRuleTokens[0].contains(Tags.RULE_ATOM_SPLITER)) {
                    String[] antecedents = semiRuleTokens[0].split(Tags.RULE_ATOM_SPLITER);

                    for (int i=0; i<antecedents.length; i++) {
                        String atomName = antecedents[i];
                        newRule.addAntecedent(allAtoms.get(atomName));
                    }
                } else {
                    newRule.addAntecedent(allAtoms.get(semiRuleTokens[0]));
                }

                if (semiRuleTokens[1].contains(Tags.RULE_ATOM_SPLITER)) {
                    String[] consequents = semiRuleTokens[1].split(Tags.RULE_ATOM_SPLITER);

                    for (int i=0; i<consequents.length; i++) {
                        String atomName = consequents[i];
                        newRule.addConsequent(allAtoms.get(atomName));
                    }
                } else {
                    newRule.addConsequent(allAtoms.get(semiRuleTokens[1]));
                }

                allRules.put(newRule.getId(), newRule);

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
     * Exp: 지정한 파일에 rule list을 저장함.
     * @return
     */
    public void saveRuleList() {

        // configure 먼저 저장.
        saveRuleConfigure();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Tags.RULE_FILE_PATH));
            for (Map.Entry<Long, Rule> entry : this.allRules.entrySet()) {
                Rule rule = entry.getValue();
                bw.write(rule.printSavingFormat() + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HashMap<Long, Rule> getAllRules() {
        return this.allRules;
    }

    public void addRule(Rule rule) {
        this.allRules.put(rule.getId(), rule);
        this.ruleNumber += 1;
    }

    public void removeRule(Rule rule) {
        this.allRules.remove(rule);
    }

    public Long getRuleNumber() {
        return ruleNumber;
    }

}
