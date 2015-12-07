package ssu.gui;/**
 * Created by JasonHong on 2015. 11. 9..
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKAPaneController;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.gui.controller.entity.PatientDefaultInfoRow;
import ssu.gui.controller.entity.PatientDetailRow;
import ssu.gui.controller.entity.PatientOpinionRow;
import ssu.gui.view.GraphView;
import ssu.object.AtomManager;
import ssu.object.PatientManager;
import ssu.object.RuleManager;
import ssu.object.TestItemManager;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.rule.Rule;
import ssu.util.AppTestLog;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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


    @FXML private TableView<PatientOpinionRow> patientOpinionTableView;
    @FXML private TableColumn<PatientOpinionRow, String> patientOpinionIDColumn;
    @FXML private TableColumn<PatientOpinionRow, String> patientOpinionColumn;


    @FXML private SplitPane leftSplitPane;
    @FXML private SplitPane rightSplitPane;

    @FXML private TreeView patientTreeView;

    @FXML private TableView<PatientDefaultInfoRow> patientTableView;
    @FXML private TableColumn<PatientDefaultInfoRow, String> subjectColumn;
    @FXML private TableColumn<PatientDefaultInfoRow, String> textValueColumn;

    @FXML private TreeTableView<PatientDetailRow> patientDetailTable;
    @FXML private TreeTableColumn<PatientDetailRow, String> testNameColumn;
    @FXML private TreeTableColumn<PatientDetailRow, String> testNumValueColumn;
    @FXML private TreeTableColumn<PatientDetailRow, String> testTextValueColumn;

//    @FXML private TableView<IKAPaneController.PatientDetailRow> patientDetailTable;
//    @FXML private TableColumn<IKAPaneController.PatientDetailRow, String> testNameColumn;
//    @FXML private TableColumn<IKAPaneController.PatientDetailRow, String> testNumValueColumn;
//    @FXML private TableColumn<IKAPaneController.PatientDetailRow, String> testTextValueColumn;

//    @FXML private Label opinionPageLabel;

//    @FXML private TextArea opinionTextArea;
//    @FXML private Button leftOpinionButton;
//    @FXML private Button rightOpinionButton;

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

    private String authorName = "";

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
        if(currentPatientId > 0 && patientOpinionTableView.getSelectionModel().getSelectedItems().size() > 0) {
            if(dataController.getPatientOpinion(currentPatientId).size() > 0){
                IKAPaneController.PatientReferenceRow selectedItem = ruleReferenceTableView.getSelectionModel().getSelectedItem();
                modalRuleEditView(event, null);
            }
        }else{
            showWarningSelectedOpinionAlert();
        }
    }

    @FXML protected void clickEditButton(ActionEvent event) throws IOException {
        IKAPaneController.PatientReferenceRow selectedItem = ruleReferenceTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            modalRuleEditView(event, selectedItem);
        }else{
            showWarningSelectedRuleAlert();
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
            }else{
                showWarningSelectedRuleAlert();
            }

        }
    }
    /*
    Save button 클릭 시 rule 저장
     */
    @FXML protected void clickSaveButton(ActionEvent event){

        // 현재 선택된 소견 ID
        final int selectedOpinionIndex = patientOpinionTableView.getSelectionModel().getSelectedIndex();
        // 현재 환자 ID
        final Long curPatientID = currentPatientId;
        FileChooser fileChooser = new FileChooser();
        Stage fileWindowStage = new Stage();
        File file = fileChooser.showSaveDialog(fileWindowStage);
        if (file != null) {
            System.out.println("Save Path : "+file.getPath().toString());

            String line = "";
            Patient patient = PatientManager.getInstance().getAllPatients().get(curPatientID);
            if (patient != null) {
                Opinion opinion = patient.getAllOpinions().get(selectedOpinionIndex);
                if (opinion != null) {
                    for (Long index : opinion.getRules()) {
                        Rule rule = RuleManager.getInstance().getAllRules().get(index);
                        if (rule != null) {
                            if (rule.getAntecedents().size() > 1) {
                                for (int i=0; i<rule.getAntecedents().size(); i++) {
                                    line += rule.getAntecedents().get(i).getName() + "\t" + "J" + rule.getId() + "\n";
                                }
                            } else {
                                line += rule.getAntecedents().get(0).getName() + "\t" + "J" + rule.getId() + "\n";
                            }
                            line += "J" + rule.getId() + ":" + rule.getConsequents().get(0).getName() + "\n";
                        }
                    }
                }
            }

            if (!line.isEmpty()) {
                try {
                    File saveFile = new File(file.getPath() + "_" + curPatientID + "_" + selectedOpinionIndex + "_graph.txt");
                    if (!saveFile.exists()) {
                        saveFile.createNewFile();
                    }
                    FileWriter fw = new FileWriter(saveFile);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("Antecedent\tConsequent\n" + line);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

//    @FXML protected void handleLeftClickButtonAction(ActionEvent event){
//        System.out.println(event);
//        this.paneController.previousOpinion(this.dataController, currentPatientId);
//        paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);
//    }
//
//    @FXML protected void handleRightClickButtonAction(ActionEvent event){
//        System.out.println(event);
//        this.paneController.nextOpinion(this.dataController, currentPatientId);
//        paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);
//    }

    public void showWarningSelectedRuleAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("확인창");
        alert.setHeaderText("확인 부탁드립니다.");
        alert.setContentText("선택된 룰이 존재하지 않습니다.");

        alert.showAndWait();
    }
    public void showWarningSelectedOpinionAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("확인창");
        alert.setHeaderText("확인 부탁드립니다.");
        alert.setContentText("소견을 먼저 선택해주세요.");

        alert.showAndWait();
    }
    public void initView(){
        this.paneController.createPatientTree(patientTreeView, this.dataController.getPatientsList());
        this.paneController.createPatientDefaultList(patientTableView,subjectColumn,textValueColumn);
        this.paneController.createPatientDetailList(patientDetailTable, testNameColumn, testNumValueColumn, testTextValueColumn );
//        this.paneController.createPatientOpinionList(opinionTextArea);
        this.paneController.createOpinionList(dataController, patientOpinionTableView, patientOpinionIDColumn, patientOpinionColumn, currentPatientId);
        this.paneController.createPatientOpinionReferenceList(ruleReferenceTableView, ruleIdColumn,
                ruleColumn, authorColumn, madeDateColumn, modifiedDateColumn);
//        paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);

        // Action 등록.
        patientTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<String> selectedItem = (TreeItem<String>) newValue;
            if(selectedItem.getValue().split(" ").length > 1){
                currentPatientId = Long.valueOf(selectedItem.getValue().split(" ")[0]);
                paneController.refreshPatientDefaultList(dataController, currentPatientId);
                paneController.refreshPatientDetailList(dataController, currentPatientId, null);
                paneController.refreshPatientOpinionList(dataController,currentPatientId);
                paneController.refreshPatientOpinionReferenceList(dataController, currentPatientId, 0);
//                    paneController.refreshOpinionPageLabel(dataController, opinionPageLabel, currentPatientId);
            }
        });

        patientOpinionTableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            AppTestLog.printLog(newValue.getOpinion());
            paneController.refreshPatientDetailList(dataController, currentPatientId, dataController.getHighlightElementByOpinion(newValue.getOpinion()));
            paneController.refreshPatientOpinionReferenceList(dataController,currentPatientId,patientOpinionTableView.getSelectionModel().getSelectedIndex());
        }));
    }

//    public int getOpinionIndex(){
//        return Integer.valueOf(opinionPageLabel.getText().split("/")[0].trim()) - 1;
//    }
    @Override
    public void start(Stage primaryStage) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("IKAMainTool.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("IKAMainToolNewScene.fxml"));
        primaryStage.setTitle("Interactive Knowledge Acquisition");

//        initView();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> Platform.runLater(new Runnable() {
            @Override
            public void run() {
                quit();
                System.out.println("Application sucessfully closed.");
                System.exit(0);
            }
        }));
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

        Text label = new Text("Consequent : ");

        final ComboBox consequentComboBox = new ComboBox();
        consequentComboBox.setEditable(true);
        consequentComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if(!(consequentComboBox.getEditor().getText().length() == 0)) {
                consequentComboBox.show();
                ObservableList data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomCompletionListInGraphView(newValue));
                consequentComboBox.setItems(data);
            }
        });

        consequentComboBox.addEventFilter(KeyEvent.KEY_RELEASED, event1 -> {
            if (event1.getCode() == KeyCode.ENTER) {
                AppTestLog.printLog("Enter");
                graphView.drawRules(RuleManager.getInstance().getAllRulesByConseqent(consequentComboBox.getEditor().getText()));
                consequentComboBox.getEditor().clear();
                consequentComboBox.getSelectionModel().clearSelection();
            }
        });

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

    /*
    메뉴 바에서 FileExport 클릭 시 이벤트
    윈도우 파일 브라우져 창 뜸
     */
    @FXML protected void menuBarFileExportClickEvent(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        Stage fileWindowStage = new Stage();
        File file = fileChooser.showSaveDialog(fileWindowStage);
        if (file != null) {
            System.out.println("Save Path : "+file.getPath().toString());
            AtomManager.getInstance().saveAtomListToPath(file.getPath() + "_atoms.txt");
            PatientManager.getInstance().savePatientsToPath(file.getPath() + "_patients.txt");
            RuleManager.getInstance().saveRuleListAndConfigureToPath(file.getPath() + "_rules.txt", file.getPath() + "_rules_configure.txt");
            TestItemManager.getInstance().saveTestItemListToPath(file.getPath() + "_test_items.txt");
        }
    }


    /**
     * Exp : App 종료시 설정되어 있는 Atom, Rule, TestItem, Patient의 정보를 모두 Save.
     */
    public void quit() { this.dataController.applicationQuit(); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("[UI] Initialize View.");
        alertInputAuthor();
        initView();
    }
    @FXML protected void alertInputAuthor(){
        TextInputDialog dialog = new TextInputDialog("Anonymous");
        dialog.setTitle("Input Author");
        dialog.setHeaderText("작성자 정보 입력창");
        dialog.setContentText("작성자 명을 입력해주세요 : ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Your name: " + result.get());
            authorName = result.get();
            leftSplitPane.setDisable(false);
            rightSplitPane.setDisable(false);
        }
        //System.out.println("Your name: " + result.get());
        leftSplitPane.setDisable(false);
        rightSplitPane.setDisable(false);
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
//        controller.setOpinionIndex(getOpinionIndex());
        controller.setPatientID(currentPatientId);
        controller.setAuthor(authorName);

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
        SwingUtilities.invokeLater(() -> {
            GraphView panel = new GraphView();
            panel.setPreferredSize(new Dimension(700,600));
            swingNode.setContent(panel.getGraphComponent());
            panel.repaint();
            graphView = panel;

            System.out.println("initJPanel");
        });
    }

}
