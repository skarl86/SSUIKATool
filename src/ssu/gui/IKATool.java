package ssu.gui;/**
 * Created by JasonHong on 2015. 11. 9..
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ssu.gui.controller.IKAController;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKAPaneController;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.object.*;
import ssu.object.patient.Opinion;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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

    @FXML private Label opinionPageLabel;

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

    private Long currentPatientId = -2L;

    /**
     * View
     */
    private GraphView graphView;

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

    @FXML protected void clickAddButton(ActionEvent event) throws IOException {
        if(currentPatientId > 0) {
            if(dataController.getPatientOpinion(currentPatientId).size() > 0){
                IKAPaneController.PatientReferenceRow selectedItem = ruleReferenceTableView.getSelectionModel().getSelectedItem();
                modalRuleEditView(event, null);
            }
        }
    }

    @FXML protected void clickEditButton(ActionEvent event) throws IOException {
        IKAPaneController.PatientReferenceRow selectedItem = ruleReferenceTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            modalRuleEditView(event, selectedItem);
        }
    }

    @FXML protected void clickDeleteButton(ActionEvent event){
        if(currentPatientId > 0) {
            IKAPaneController.PatientReferenceRow selectedItem = ruleReferenceTableView.getSelectionModel().getSelectedItem();
            if(selectedItem != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("확인");
                alert.setHeaderText(selectedItem.getRule());
                alert.setContentText("정말 삭제 하시겠습니까?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    // 데이터 삭제.
                    paneController.deleteRuleReferenceList(dataController, ruleReferenceTableView, currentPatientId);
                } else {
                    // 취소.
                }
            }
        }
    }

    @FXML protected void handleLeftClickButtonAction(ActionEvent event){
        System.out.println(event);
        this.paneController.previousOpinion(this.dataController, currentPatientId);
        paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);
    }

    @FXML protected void handleRightClickButtonAction(ActionEvent event){
        System.out.println(event);
        this.paneController.nextOpinion(this.dataController, currentPatientId);
        paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);
    }

    public void initView(){
        this.paneController.createPatientTree(patientTreeView, this.dataController.getPatientsList());
        this.paneController.createPatientDefaultList(patientTableView,subjectColumn,textValueColumn);
        this.paneController.createPatientDetailList(patientDetailTable, testNameColumn, testNumValueColumn, testTextValueColumn );
        this.paneController.createPatientOpinionList(opinionTextArea);
        this.paneController.createPatientOpinionReferenceList(ruleReferenceTableView, ruleIdColumn,
                ruleColumn, authorColumn, madeDateColumn, modifiedDateColumn);
        paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);

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
                    paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);
                }
                // do what ever you want
            }

        });
    }

    public int getOpinionIndex(){
        return Integer.valueOf(opinionPageLabel.getText().split("/")[0].trim()) - 1;
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

    /*
    MenuBar에서 View -> Graph 클릭 시 새로운 창 띄움
     */
    @FXML protected void menuBarGraphClickEvent(ActionEvent event){
        System.setProperty("javafx.embed.singleThread", "true");

        Stage graphStage = new Stage();

        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        StackPane pane = new StackPane();

        TitledPane tp = new TitledPane("My Titled Pane", new Button(""));
        tp.setText("Graph");
        tp.setPrefSize(600, 500);
        tp.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        tp.setMinSize(600, 500);
        tp.minWidth(600);
        tp.minHeight(500);


        VBox vbox = new VBox();
        vbox.setPadding(new Insets(1));
        vbox.setSpacing(2);
        VBox.setVgrow(swingNode,Priority.ALWAYS);
        vbox.setPrefSize(600,500);

        Text label = new Text("TEST : ");

        ComboBox consequentComboBox = new ComboBox();

        consequentComboBox.getItems().addAll("간질환","폐질환");

        HBox hBox = new HBox(2);

        HBox.setHgrow(label, Priority.ALWAYS);
        HBox.setHgrow(consequentComboBox, Priority.ALWAYS);

        hBox.getChildren().add(label);
        hBox.getChildren().add(consequentComboBox);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        pane.getChildren().add(tp);

        tp.setContent(vbox);

        vbox.getChildren().add(swingNode);
        vbox.getChildren().add(hBox);

        graphStage.setResizable(false);
        graphStage.setScene(new Scene(pane, 600, 500));
        graphStage.show();

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

        if(selectedItem != null)
            controller.setRule(dataController, selectedItem.getRuleId());
        else
            controller.setRule(dataController, null);
        controller.setOpinionIndex(getOpinionIndex());
        controller.setPatientID(currentPatientId);

        stage.setScene(new Scene(root));
        stage.setTitle("Rule Editor");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }

    /*
    Java Swing 삽입 부분
     */
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            /*
            Java Swing Code 삽입 부분
             */
            @Override
            public void run() {
                GraphView panel = new GraphView();
                panel.setPreferredSize(new Dimension(700,600));
                swingNode.setContent(panel.getGraphComponent());
                panel.repaint();
                System.out.println("initJPanel");
            }
        });
    }
}
