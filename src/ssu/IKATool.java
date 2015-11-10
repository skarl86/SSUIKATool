package ssu;

import ssu.object.AtomManager;
import ssu.object.PatientManager;
import ssu.object.RuleManager;
import ssu.object.TestItemManager;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.rule.Atom;
import ssu.object.rule.AtomClass;
import ssu.object.rule.Rule;
import ssu.object.test.NumericalValue;
import ssu.object.test.TestItem;
import ssu.object.test.TestResult;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class IKATool {

    public static void main(String[] args) {

        /**
         * Testing AtomManager
         */
        /*
        AtomManager atomManager = AtomManager.getInstance();
        atomManager.loadAtomList();
        HashMap<String, Atom> allAtoms = atomManager.getAllAtoms();

        for (Map.Entry<String, Atom> entry : allAtoms.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue().getName() + "-" + entry.getValue().getType());
        }

        atomManager.saveAtomList();
        */

        /**
         * Testing RuleManager
         */
        /*
        AtomManager atomManager = AtomManager.getInstance();
        atomManager.loadAtomList();
        HashMap<String, Atom> allAtoms = atomManager.getAllAtoms();
        RuleManager ruleManager = RuleManager.getInstance();

        ruleManager.loadRuleList(allAtoms);
        HashMap<Long, Rule> allRules = ruleManager.getAllRules();

        for (Map.Entry<Long, Rule> entry : allRules.entrySet()) {
            System.out.println(entry.getValue().printSavingFormat());
        }

        GregorianCalendar gc = new GregorianCalendar();
        Long time = gc.getTimeInMillis();
        Rule newRule = new Rule(ruleManager.getRuleCount(), "홍진영", time, time);
        newRule.addAntecedent(allAtoms.get("AST"));
        newRule.addAntecedent(allAtoms.get("ALP"));
        newRule.addAntecedent(allAtoms.get("ALT"));
        newRule.addConsequent(allAtoms.get("LiverDisease"));

        ruleManager.addRule(newRule);

        ruleManager.saveRuleList();
        */

        /**
         * Testing TestItemManager
         */
        /*
        TestItemManager testItemManager = TestItemManager.getInstance();
        testItemManager.loadTestItemList();
        HashMap<String, TestItem> allTestItems = testItemManager.getAllTestItems();

        for (Map.Entry<String, TestItem> entry : allTestItems.entrySet()) {
            System.out.println(entry.getValue().printSavingFormat());
        }

        TestItem newTestItem = new TestItem("AAA", "TEST");
        testItemManager.addTestItem(newTestItem);

        testItemManager.saveTestItemList();
        */

        /**
         * Testing PatientManager
         */
        /*
        TestItemManager testItemManager = TestItemManager.getInstance();
        testItemManager.loadTestItemList();
        HashMap<String, TestItem> allTestItems = testItemManager.getAllTestItems();

        PatientManager patientManager = PatientManager.getInstance();
        patientManager.loadPatients(allTestItems);
        ArrayList<Patient> allPatients = patientManager.getAllPatients();

        for (Patient patient : allPatients) {
            System.out.println(patient.printSavingFormat());
        }

        Patient newPatient = new Patient("아무개", 30, "M", 50123L, "20151108");
        TestResult tr1 = new TestResult(allTestItems.get("AST"));
        tr1.addTestValue(new NumericalValue(32));
        newPatient.addTestResult(tr1);

        TestResult tr2 = new TestResult(allTestItems.get("ALP"));
        tr2.addTestValue(new NumericalValue(27));
        newPatient.addTestResult(tr2);

        Opinion op1 = new Opinion("AST가 높습니다. 간 및 담도계 기능이상이 의심됩니다.");
        op1.addRule(0L);
        newPatient.addOpinion(op1);

        Opinion op2 = new Opinion("AST, ALT가 높습니다. 간 및 담도계 기능이상이 의심됩니다.");
        newPatient.addOpinion(op2);

        patientManager.addPatient(newPatient);

        patientManager.savePatients();
        */

        /**
         * GUI
         */
    }

}
