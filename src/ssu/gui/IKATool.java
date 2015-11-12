package ssu.gui;/**
 * Created by JasonHong on 2015. 11. 9..
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ssu.object.*;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;

public class IKATool extends Application {

    /**
     * Manager
     */
    private AtomManager atomManager;
    private RuleManager ruleManager;
    private TestItemManager testItemManager;
    private PatientManager patientManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        /**
         * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
         */
        // AtomManager 설정.
        this.atomManager = AtomManager.getInstance();
        this.atomManager.loadAtomList();

        // RuleManager 설정.
        this.ruleManager = RuleManager.getInstance();
        this.ruleManager.loadRuleList(this.atomManager.getAllAtoms());

        // TestItemManager 설정.
        this.testItemManager = TestItemManager.getInstance();
        this.testItemManager.loadTestItemList();

        // PatientManager 설정.
        this.patientManager = PatientManager.getInstance();
        this.patientManager.loadPatients(this.testItemManager.getAllTestItems());

        System.out.println("Application sucessfully initiallized.");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("IKAMainTool.fxml"));
        primaryStage.setTitle("Interactive Knowledge Acquisition");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        quit();
                        System.out.println("Application sucessfully closed.");
                        System.exit(0);
                    }
                });
            }
        });
    }

    /**
     * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
     */
    public void quit() {
        // AtomManager 저장.
        this.atomManager.saveAtomList();

        // RuleManager 저장.
        this.ruleManager.saveRuleList();

        // TestItemManager 저장.
        this.testItemManager.saveTestItemList();

        // PatientManager 저장.
        this.patientManager.savePatients();
    }

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

}
