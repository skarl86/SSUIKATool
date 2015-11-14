package ssu.gui;/**
 * Created by JasonHong on 2015. 11. 9..
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ssu.gui.controller.IKAController;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKAPaneController;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.object.*;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class IKATool extends Application implements Initializable {

    public static void main(String[] args) {
        launch(args);
    }

    private String ACTION_DELETE ="deleteButton";
    private String ACTION_EDIT ="editButton";
    private String ACTION_ADD ="addButton";

    private IKADataController dataController = IKADataController.getInstance();
    private IKAPaneController paneController = IKAPaneController.getInstance();

    @FXML private TreeView patientTreeView;

    @FXML private TableView<IKAPaneController.PatientRow> patientTableView;
    @FXML private TableColumn<IKAPaneController.PatientRow, String> subjectColumn;
    @FXML private TableColumn<IKAPaneController.PatientRow, String> textValueColumn;

    @FXML private TableView<IKAPaneController.PatientDetailRow> patientDetailTable;
    @FXML private TableColumn<IKAPaneController.PatientDetailRow, String> testNameColumn;
    @FXML private TableColumn<IKAPaneController.PatientDetailRow, String> testNumValueColumn;
    @FXML private TableColumn<IKAPaneController.PatientDetailRow, String> testTextValueColumn;

    @FXML private TextArea opinionTextArea;
    @FXML private Button leftOpinionButton;
    @FXML private Button rightOpinionButton;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private TableView<IKAPaneController.PatientReferenceRow> ruleReferenceTableView;
    @FXML private TableColumn<IKAPaneController.PatientReferenceRow, String> ruleIdColumn;
    @FXML private TableColumn<IKAPaneController.PatientReferenceRow, String> ruleColumn;
    @FXML private TableColumn<IKAPaneController.PatientReferenceRow, String> authorColumn;
    @FXML private TableColumn<IKAPaneController.PatientReferenceRow, String> madeDateColumn;
    @FXML private TableColumn<IKAPaneController.PatientReferenceRow, String> modifiedDateColumn;

    private Long currentPatientId;

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

    @FXML protected void actionReferenceRuleList(ActionEvent event) throws IOException {
        System.out.println("[INTERACTION] Modal Rule Edit View : " + ((Button)event.getSource()).getId());

        IKAPaneController.PatientReferenceRow selectedItem = ruleReferenceTableView.getSelectionModel().getSelectedItem();

        if( ((Button)event.getSource()).getId().equals(ACTION_ADD)){
            modalRuleEditView(event, selectedItem);
        }

        if(selectedItem != null){
            if(((Button)event.getSource()).getId().equals(ACTION_EDIT)){
                modalRuleEditView(event, selectedItem);
            }else if(((Button)event.getSource()).getId().equals(ACTION_DELETE)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // 데이터 삭제.
                    paneController.deleteRuleReferenceList(dataController, ruleReferenceTableView);
                } else {
                    // 취소.
                }
            }
        }


    }

    @FXML protected void handleLeftClickButtonAction(ActionEvent event){
        System.out.println(event);
        this.paneController.previousOpinion(this.dataController, currentPatientId);
    }

    @FXML protected void handleRightClickButtonAction(ActionEvent event){
        System.out.println(event);
        this.paneController.nextOpinion(this.dataController, currentPatientId);
    }

    public void initView(){
        this.paneController.createPatientTree(patientTreeView, this.dataController.getPatientsList());
        this.paneController.createPatientDefaultList(patientTableView,subjectColumn,textValueColumn);
        this.paneController.createPatientDetailList(patientDetailTable, testNameColumn, testNumValueColumn, testTextValueColumn );
        this.paneController.createPatientOpinionList(opinionTextArea);
        this.paneController.createPatientOpinionReferenceList(ruleReferenceTableView, ruleIdColumn,
                ruleColumn, authorColumn, madeDateColumn, modifiedDateColumn);

        // Action 등록.
        patientTreeView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                if(selectedItem.getValue().split(" ").length > 1){
                    currentPatientId = Long.valueOf(selectedItem.getValue().split(" ")[0]);
                    paneController.refreshPatientDefaultList(dataController, currentPatientId);
                    paneController.refreshPatientDetailList(dataController, currentPatientId);
                    paneController.refreshPatientOpinionList(dataController,currentPatientId);
                    paneController.refreshPatientOpinionReferenceList(dataController, currentPatientId, 0);
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
        System.out.println("[UI] Initialize View.");
        initView();
    }

    private void modalRuleEditView(ActionEvent event, IKAPaneController.PatientReferenceRow selectedItem) throws IOException {
        Stage stage = new Stage();
//        ResourceBundle resources = ResourceBundle.getBundle("ssu.gui");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RulePopUpView.fxml"));
        Parent root = fxmlLoader.load();

        IKARulePopUpViewController controller = fxmlLoader.getController();
        controller.setRule(dataController, selectedItem.getRuleId());

        stage.setScene(new Scene(root));
        stage.setTitle("Rule Editor");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }
}
