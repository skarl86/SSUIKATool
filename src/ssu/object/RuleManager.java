package ssu.object;

import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.io.*;
import java.util.ArrayList;
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
            InputStream is = this.getClass().getResourceAsStream(Tags.RULE_CONFIGURE_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
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
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(Tags.RULE_CONFIGURE_FILE_PATH).getPath()));
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
            InputStream is = this.getClass().getResourceAsStream(Tags.RULE_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(Tags.RULE_SPLITER, 5);
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

                if (tokens.length > 5 && tokens[5].contains(Tags.RULE_PATIENT_SPLITER)) {
                    String[] patientOpTokens = tokens[5].split(Tags.RULE_PATIENT_SPLITER);
                    for (int i=0; i<patientOpTokens.length; i++) {
                        String[] patientOpinions = patientOpTokens[i].split(Tags.RULE_PATIENT_OPINION_SPLITER);
                        Long patientId = Long.parseLong(patientOpinions[0]);
                        ArrayList<Integer> opinions = new ArrayList<Integer>();
                        for (int j=1; j<=patientOpinions.length-1; j++) {
                            opinions.add(Integer.parseInt(patientOpinions[j]));
                        }

                        newRule.addPatientAllOpinion(patientId, opinions);
                    }
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
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(Tags.RULE_FILE_PATH).getPath()));
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


    /**
     * Antecedent, Consequent로 이미 존재하는 Rule인지 검색.
     * @param antecedents Antecedent들의 이름 리스트.
     * @param consequents Conseqeunt들의 이름 리스트.
     * @return 이미 존재하는 Rule 경우 해당 Rule의 객체를 리턴, 아니면 null을 리턴.
     */
    public Rule getExistedRuleByCondition(ArrayList<String> antecedents, ArrayList<String> consequents) {

        // 파라미터의 조건들을 Formal 형식의 Rule string을 생성.
        String formalFormat = "";
        for (int i=0; i<antecedents.size(); i++) {
            formalFormat += antecedents.get(i);
            if (i < antecedents.size() - 1) formalFormat += ",";
        }
        formalFormat += "->";
        for (int i=0; i<consequents.size(); i++) {
            formalFormat += consequents.get(i);
            if (i < consequents.size() - 1) formalFormat += ",";
        }

        // Rule의 Formal 형식을 비교해서 동일한 형식인 경우 같은 Rule.
        for (Map.Entry<Long, Rule> entry : this.allRules.entrySet()) {
            if (formalFormat.equals(entry.getValue().printFormalFormat())) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     *
     * @param atom
     * @return
     */
    public ArrayList<Rule> getAllRulesByConseqent(String atom) {
        ArrayList<Rule> rules = new ArrayList<Rule>();

        for (Map.Entry<Long, Rule> entry : this.allRules.entrySet()) {
            Rule rule = entry.getValue();

            for (Atom con : rule.getConsequents()) {
                if (con.getName().equals(atom)) {
                    rules.add(rule);
                    break;
                }
            }
        }

        return rules;
    }



    /**
     * Exp : Rule을 추가.
     * @param rule
     */
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
