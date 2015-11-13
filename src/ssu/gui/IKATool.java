package ssu.gui;/**
 * Created by JasonHong on 2015. 11. 9..
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ssu.gui.controller.IKAController;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKAPaneController;
import ssu.object.*;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class IKATool extends Application implements Initializable {

    public static void main(String[] args) {
        launch(args);
    }

    private IKADataController dataController = IKADataController.getInstance();
    private IKAPaneController paneController = IKAPaneController.getInstance();

    @FXML
    private TreeView patientTreeView;
    @FXML
    private TableView<IKAPaneController.PatientRow> patientTableView;
    @FXML
    private TableColumn<IKAPaneController.PatientRow, String> subjectColumn;
    @FXML
    private TableColumn<IKAPaneController.PatientRow, String> textValueColumn;
    @FXML
    private TableView<IKAPaneController.PatientDetailRow> patientDetailTable;
    @FXML
    private TableColumn<IKAPaneController.PatientDetailRow, String> testNameColumn;
    @FXML
    private TableColumn<IKAPaneController.PatientDetailRow, String> testNumValueColumn;
    @FXML
    private TableColumn<IKAPaneController.PatientDetailRow, String> testTextValueColumn;
    @FXML
    private TextArea opinionTextArea;

    @Override
    public void init() {
        /**
         * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
         */
        // AtomManager 설정.
//        this.dataController.atomManager = AtomManager.getInstance();
        this.dataController.loadAtomList();

        // RuleManager 설정.
//        this.dataController.ruleManager = RuleManager.getInstance();
        this.dataController.loadRuleList();

        // TestItemManager 설정.
//        this.dataController.testItemManager  = TestItemManager.getInstance();
        this.dataController.loadTestItemList();

        // PatientManager 설정.
//        this.dataController.patientManager = PatientManager.getInstance();
        this.dataController.loadPatients();

        System.out.println("Application sucessfully initiallized.");
    }

    public void initView(){
        System.out.println("Init View.");

        this.paneController.createPatientTree(patientTreeView, this.dataController.getPatientsList());
        this.paneController.createPatientDefaultList(patientTableView,subjectColumn,textValueColumn);
        this.paneController.createPatientDetailList(patientDetailTable, testNameColumn, testNumValueColumn, testTextValueColumn );
        this.paneController.createPatientOpinionList(opinionTextArea);

        // Action 등록.
        patientTreeView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                if(selectedItem.getValue().split(" ").length > 1){
                    Long regId = Long.valueOf(selectedItem.getValue().split(" ")[0]);
                    paneController.refreshPatientDefaultList(dataController, regId);
                    paneController.refreshPatientDetailList(dataController, regId);
                    paneController.refreshPatientOpinionList(dataController,regId);
                }
                // do what ever you want
            }

        });
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("IKAMainTool.fxml"));
        primaryStage.setTitle("Interactive Knowledge Acquisition");

//        initView();

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
    public void quit() { this.dataController.applicationQuit(); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
    }
}
