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
import ssu.gui.controller.IKAController;
import ssu.object.*;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class IKATool extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private IKAController mainController = IKAController.getInstance();

    @Override
    public void init() {
        /**
         * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
         */
        // AtomManager 설정.
//        this.mainController.atomManager = AtomManager.getInstance();
        this.mainController.loadAtomList();

        // RuleManager 설정.
//        this.mainController.ruleManager = RuleManager.getInstance();
        this.mainController.loadRuleList();

        // TestItemManager 설정.
//        this.mainController.testItemManager  = TestItemManager.getInstance();
        this.mainController.loadTestItemList();

        // PatientManager 설정.
//        this.mainController.patientManager = PatientManager.getInstance();
        this.mainController.loadPatients();

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
    public void quit() { this.mainController.applicationQuit(); }

}
