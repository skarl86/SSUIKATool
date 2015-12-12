package ssu.gui.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.controlsfx.control.textfield.TextFields;
import ssu.gui.controller.entity.AtomRow;
import ssu.gui.controller.entity.PatientDetailRow;
import ssu.gui.controller.entity.PatientRow;
import ssu.gui.controller.entity.PreviousRuleRow;
import ssu.gui.controller.factory.AtomCallFactor;
import ssu.gui.controller.factory.PreviousRuleCallFactor;
import ssu.gui.controller.factory.ValueCallFactor;
import ssu.object.patient.Opinion;
import ssu.object.patient.Patient;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;
import ssu.object.rule.ValuedAtom;
import ssu.object.test.TestResult;
import ssu.util.AppTestLog;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by NCri on 2015. 11. 14..
 */
public class IKARulePopUpViewController extends IKAController implements Initializable{

    private Rule _selectedRule;
    private final static int COMBO_TYPE_ANT = 0;
    private final static int COMBO_TYPE_COSEQ = 1;
    private final static String ANTECEDENT_TABLE = "antecendentTable";
    private final static String CONSEQUENT_TABLE = "conseqeuntTable";
    private final static String DELIMITER_CONDITION_AND_VALUE = "_";

    private final static int EXCEPTION_NONE = -1;
    private final static int EXCEPTION_EMPTY_RULE = 1;
    private final static int EXCEPTION_DUPLICATE_RULE = 2;
    private final static int EXCEPTION_DUPLICATE_ATOM = 3;

    public final static String VALUE_COMBOBOX_ID = "valueList";

    @FXML TableView<AtomRow> antecedentTableView;
    @FXML TableView<AtomRow> consequentTableView;
    @FXML TableView<PreviousRuleRow> completeRuleTable;
    @FXML TableView<PatientRow> patientTable;

    @FXML TreeTableView<PatientDetailRow> patientDetailTable;

    @FXML TableColumn<AtomRow, String> antecedentColumn;
    @FXML TableColumn<AtomRow, String> antecedentValueColumn;
    @FXML TableColumn<AtomRow, String> consequentColumn;
    @FXML TableColumn<AtomRow, String> consequentValueColumn;

    @FXML TableColumn<PatientRow, String> idColumn;
    @FXML TableColumn<PatientRow, String> nameColumn;
    @FXML TableColumn<PatientRow, String> genderColumn;
    @FXML TableColumn<PatientRow, String> ageColumn;

    @FXML TreeTableColumn<PatientDetailRow, String> testNameColumn;
    @FXML TreeTableColumn<PatientDetailRow, String> testValueColumn;
    @FXML TreeTableColumn<PatientDetailRow, String> textValueColumn;

    @FXML TableColumn<PreviousRuleRow, String> completeRuleColumn;

//    @FXML ComboBox<String> antecedentComboBox;
//    @FXML ComboBox<String> consequentComboBox;
    @FXML TextField antecedentTextField;
    @FXML TextField consequentTextField;

    @FXML GridPane mainView;

    @FXML Label authorLabel;

    @FXML HBox splitTopBox;

    @FXML TextArea opinionTextArea;

    private Long patientID;
    private int indexOfOpinion;
//    private String newInputValue;
    private ArrayList<Patient> patientsList;

    private HashMap<Integer, ComboBox<String>> antecedentValueMap = new HashMap<Integer, ComboBox<String>>();
    private HashMap<Integer, ComboBox<String>> consequentValueMap = new HashMap<Integer, ComboBox<String>>();

    /**
     *
     */


    /**
     * UI상 Cancel 버튼을 눌렀을 때 Event를 처리하는 메소드.
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleclickCancel(ActionEvent event) throws IOException {
        System.out.println(event);
        Stage stage = (Stage) mainView.getScene().getWindow();
        stage.close();
    }

    /**
     * UI상의 OK Button을 눌렀을 때 Event 처리.
     * @param event
     */
    @FXML
    protected void handleClickOK(ActionEvent event){
        ArrayList<String> antList = makeAtomList(antecedentTableView);
        ArrayList<String> consqList = makeAtomList(consequentTableView);

        String contentText = "";

        int exceptionType = isOKException(antList, consqList);

        if(exceptionType != EXCEPTION_NONE){
            switch (isOKException(antList, consqList)){
                case EXCEPTION_DUPLICATE_ATOM:
                    contentText = "중복 된 Atom이 존재합니다. 확인해주세요.";
                    break;
                case EXCEPTION_EMPTY_RULE:
                    contentText = "조건 또는 결과의 Atom이 비어있습니다. 확인해주세요.";
                    break;
                case EXCEPTION_DUPLICATE_RULE:
                    contentText = "중복 된 Rule이 존재합니다. 확인해주세요.";
                    break;
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("에러");
            alert.setHeaderText("Rule이 잘못되었습니다.");
            alert.setContentText(contentText);
            alert.showAndWait();
        }else{
            IKADataController.getInstance().ruleEditDialogOK(antList, consqList, authorLabel.getText(), patientID,indexOfOpinion);
            IKAPaneController.getInstance().refreshPatientOpinionReferenceList(
                    IKADataController.getInstance(),patientID, indexOfOpinion);

            Stage stage = (Stage) mainView.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * UI 흐름상 Conditions(Antecedent, Consequent) Cell을 생성(또는 갱신)할 때
     * Value Combobox의 객체를 가져올 때 사용.
     * @param tableView
     * @param index
     * @return
     */
    public ComboBox<String> getAtomComboBox(TableView tableView, int index){
        ComboBox<String> valueList = null;

        if(tableView.getId().equals(CONSEQUENT_TABLE)){
            valueList = consequentValueMap.get(index);
            if(valueList == null) {
                valueList = new ComboBox<String>();
                consequentValueMap.put(index, valueList);
            }else {
                valueList = consequentValueMap.get(index);
            }
        }else if(tableView.getId().equals(ANTECEDENT_TABLE)){
            valueList = antecedentValueMap.get(index);
            if(valueList == null) {
                valueList = new ComboBox<String>();
                antecedentValueMap.put(index, valueList);
            }else {
                valueList = antecedentValueMap.get(index);
            }
        }

        return valueList;
    }

    /**
     * UI 흐름상 Conditions(Antecedent, Consequent) Table과 그에 해당하는 Value를
     * Delimiter를 합친 String 형태로 반환해야 하기 때문에
     * 이때 사용하는 메소드.
     * @param tableView
     * @return
     */
    private ArrayList<String> makeAtomList(TableView<AtomRow> tableView){
        ArrayList<String> atomList = new ArrayList<String>();
        for(int i = 0 ; i < tableView.getItems().size(); i++){
            AtomRow item = tableView.getItems().get(i);

            String atomValue = getAtomValue(tableView.getId(), i);

            if(atomValue.length() == 0){
                atomList.add(item.getAtom());
            }else{
                atomList.add(item.getAtom()+ DELIMITER_CONDITION_AND_VALUE + atomValue);
            }
        }

        return atomList;
    }

    /**
     * UI상 Atom의 Value값이 Combobox의 Text로 되어있기 때문에
     * Table에 해당하는 Value 값을 가져오기 오기 위한 메소드.
     * @param tableName
     * @param index
     * @return
     */
    private String getAtomValue(String tableName, int index){
        String value = "";
        ComboBox<String> valueComboBox = null;

        if(tableName.equals(CONSEQUENT_TABLE)){
            valueComboBox = consequentValueMap.get(index);
        }else if(tableName.equals(ANTECEDENT_TABLE)){
            valueComboBox = antecedentValueMap.get(index);
        }

        if(valueComboBox.getSelectionModel().getSelectedItem() != null) {
            value = valueComboBox.getSelectionModel().getSelectedItem().toString();
        }

        return value;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppTestLog.printLog("[UI] Initialize PopViewController");

        TextFields.bindAutoCompletion(
                antecedentTextField,
                IKADataController.getInstance().getAllAtomsExceptValueList()
        );


        TextFields.bindAutoCompletion(
                consequentTextField,
                IKADataController.getInstance().getAllAtomsExceptValueList()
        );

        antecedentTextField.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    AppTestLog.printLog("Enter");
                    AppTestLog.printLog(antecedentTextField.getText());
                    antecedentTableView.getItems().add(new AtomRow(antecedentTextField.getText(), ""));
                    refreshCompletionRule();
                }
            }
        });

        consequentTextField.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    AppTestLog.printLog("Enter");
                    AppTestLog.printLog(consequentTextField.getText());
                    consequentTableView.getItems().add(new AtomRow(consequentTextField.getText(), ""));
                    refreshCompletionRule();
                }
            }
        });
    }

//    private void initAutoCompleteComboBox(final ComboBox<String> comboBox, final TableView<AtomRow> tableView){
//        ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAllAtomList());
//        comboBox.setItems(new SortedList<String>(data, Collator.getInstance()));
//
//        comboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
//            // ComboBox에 텍스트를 입력할 때 마다 불리는 이벤트.
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if(!(comboBox.getEditor().getText().length() == 0)) {
//                    comboBox.show();
//                    newInputValue = newValue;
//                    ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomCompletionList(newValue));
//                    comboBox.setItems(data);
//                }
//            }
//        });
//
//        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            // ComboBox에 리스트 중에서 선택 했을 때.
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                if(newValue != null){
//                    AppTestLog.printLog("Selection");
//                    tableView.getItems().add(new AtomRow(newInputValue, ""));
//                    refreshAutoCompleteAfterSelectionOrEnter(comboBox);
//                    comboBox.getEditor().clear();
//                    comboBox.getSelectionModel().clearSelection();
//                }
//            }
//        });
//
//        comboBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
//            // ComboBox에 텍스트를 입력한 후 "Enter"를 선택했을때,
//            @Override
//            public void handle(KeyEvent event) {
//                AppTestLog.printLog("Enter");
//                if(event.getCode() == KeyCode.ENTER){
//                    tableView.getItems().add(new AtomRow(newInputValue, ""));
//                    refreshAutoCompleteAfterSelectionOrEnter(comboBox);
//                    comboBox.getEditor().clear();
//                    comboBox.getSelectionModel().clearSelection();
//
//                }
//            }
//        });
//    }

    /**
     * 처음 모든 Table들을 초기화 하기 위한 메소드.
     */
    private void initTable(){
        AppTestLog.printLog("Initailize Table");

        antecedentColumn.setCellValueFactory(
                new PropertyValueFactory<AtomRow, String>("atom")
        );

        consequentColumn.setCellValueFactory(
                new PropertyValueFactory<AtomRow, String>("atom")
        );

        antecedentValueColumn.setCellValueFactory(
                new PropertyValueFactory<AtomRow, String>("value")
        );

        consequentValueColumn.setCellValueFactory(
                new PropertyValueFactory<AtomRow, String>("value")
        );

        completeRuleColumn.setCellValueFactory(
                new PropertyValueFactory<PreviousRuleRow, String>("previousRule")
        );

        idColumn.setCellValueFactory(new PropertyValueFactory<PatientRow, String>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<PatientRow, String>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<PatientRow, String>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<PatientRow, String>("age"));

        ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        String[] atomValue = null;

        if(_selectedRule != null){
            for(ValuedAtom atm : _selectedRule.getAntecedents()){
//                atomValue = IKADataController.getInstance().getAtomAndValue(atm);
//                antecendentData.add(new AtomRow(atomValue[0], atomValue[1]));
                antecendentData.add(new AtomRow(atm.getAtom().getName(), atm.getValue()));
            }
            for(ValuedAtom atm : _selectedRule.getConsequents()){
//                atomValue = IKADataController.getInstance().getAtomAndValue(atm);
//                consequentData.add(new AtomRow(atomValue[0], atomValue[1]));
                consequentData.add(new AtomRow(atm.getAtom().getName(), atm.getValue()));
            }
        }

        antecedentColumn.setCellFactory(new AtomCallFactor(this));
        consequentColumn.setCellFactory(new AtomCallFactor(this));

        antecedentValueColumn.setCellFactory(new ValueCallFactor(this));
        consequentValueColumn.setCellFactory(new ValueCallFactor(this));

        completeRuleColumn.setCellFactory(new PreviousRuleCallFactor(this, completeRuleTable));

        antecedentTableView.setItems(antecendentData);
        antecedentTableView.setId(ANTECEDENT_TABLE);
        consequentTableView.setItems(consequentData);
        consequentTableView.setId(CONSEQUENT_TABLE);

        completeRuleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if( newSelection != null){
                if(completeRuleTable.getSelectionModel().getSelectedItem() != null) {
                    if(newSelection.getPreviousRule() != null){
                        ArrayList<Patient> patients = IKADataController.getInstance()
                                .getTempPatientList(newSelection.getPreviousRule());
                        if(!patients.isEmpty()){
                            patientsList = patients;
                            refreshPatientTableView(patients);
                        }
                    }
                }
            }
        });

        // 수정 필요...
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if( newSelection != null) {
                if (patientTable.getSelectionModel().getSelectedItem() != null) {
                    Patient selectedPat = patientsList.get(patientTable.getItems().indexOf(newSelection));
                    ArrayList<Opinion> tempOpin = selectedPat.getAllOpinions();
                    opinionTextArea.setText(tempOpin.get(0).getOpinion());
                    refreshPatientDetailTableView(selectedPat.getRegId());
                }
            }
        });
    }

    /**
     * UI상으로 모든 Conditions 작성이 완료 된 후
     * Exception에 해당하는 경우를 Check 하기 위한 메소드.
     * @param antecedentList
     * @param conseqeuntList
     * @return
     */
    private int isOKException(ArrayList<String> antecedentList,
                                  ArrayList<String> conseqeuntList){
        int exceptionType = -1;

        if(antecedentList.isEmpty() || conseqeuntList.isEmpty()){
            exceptionType = EXCEPTION_EMPTY_RULE;
        }
        if(isDuplicateAtom(antecedentList, conseqeuntList)){
            exceptionType = EXCEPTION_DUPLICATE_ATOM;
        }
        if(isDuplicateRule(antecedentList, conseqeuntList)){
            exceptionType = EXCEPTION_DUPLICATE_RULE;
        }
        return exceptionType;
    }
    private boolean isDuplicateRule(ArrayList<String> antecedentList, ArrayList<String> conseqeuntList){
        return IKADataController.getInstance().checkExistedRuleByConditions(patientID, antecedentList, conseqeuntList);
    }

    /**
     * UI상으로 중복체크를 막기 전까지 임시로 처리.
     * @param antecedentList
     * @param conseqeuntList
     * @return
     */
    private boolean isDuplicateAtom(ArrayList<String> antecedentList, ArrayList<String> conseqeuntList) {
        Set<String> set = new HashSet<String>();
        String delimeter = "_";
        boolean isDuplicate = false;

        for(String atom : antecedentList){
            if(atom.contains(delimeter)){
                isDuplicate = !set.add(atom.split(delimeter)[0]);
            }else{
                isDuplicate = !set.add(atom);
            }

            if(isDuplicate) return isDuplicate;
        }

        set.clear();

        for(String atom : conseqeuntList){
            if(atom.contains(delimeter)){
                isDuplicate = !set.add(atom.split(delimeter)[0]);
            }else{
                isDuplicate = !set.add(atom);
            }

            if(isDuplicate) return isDuplicate;
        }

        return isDuplicate;
    }

    /**
     * UI상 환자 상세 정보가 Previous Rule을 선택 할 때 마다 갱신되어야 하기 때문에
     * 갱신을 위한 메소드.
     * @param patientID
     */
    private void refreshPatientDetailTableView(Long patientID){

        testNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<PatientDetailRow, String> p) -> new ReadOnlyStringWrapper(
                        p.getValue().getValue().getTestName())
        );
        testValueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<PatientDetailRow, String> p) -> new ReadOnlyStringWrapper(
                        p.getValue().getValue().getTestValue())
        );
        textValueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<PatientDetailRow, String> p) -> new ReadOnlyStringWrapper(
                        p.getValue().getValue().getTextValue())
        );

        // 수정 필요...
        TreeItem<PatientDetailRow> root = IKADataController.getInstance().getTestResultTreeByPatientId(patientID);

        patientDetailTable.setRoot(root);
    }

    /**
     * UI상 환자 정보가 Previous Rule을 선택 할 때 마다 갱신되어야 하기 때문에
     * 갱신을 위한 메소드.
     * @param tempPatients
     */
    private void refreshPatientTableView(ArrayList<Patient> tempPatients){
        ObservableList<PatientRow> tempPatientsData = FXCollections.observableArrayList();

        for(Patient pat : tempPatients){
            tempPatientsData.add(
                    new PatientRow(
                            pat.getName(),pat.getGender(),String.valueOf(pat.getAge()), String.valueOf(pat.getRegId())));
        }

        patientTable.setItems(tempPatientsData);
    }

    /**
     * UI상 Previous Rule을 선택 했을 때, Conditions과 Values가 변경되어야 하는데
     * 이 때 Table 갱신을 하기 위한 메소드.
     * Map은 Antecedent와 Consequent 상수를 키로 갖고
     * 그에 해당하는 Atom을 키로 Value List를 값으로 하는 Map을 값으로 갖는다.
     * Atom의 Value는 ComboBox 객체의 Selected Text 값이기 때문에 이를 가져오는 과정이 포함되어 있다.
     * @param anteAndConsEachValues
     */
    public void refreshAtomTableView(HashMap<String, ArrayList<HashMap<String, String>>> anteAndConsEachValues){
        ArrayList<HashMap<String, String>> antcAtomAndValue = anteAndConsEachValues.get(IKADataController.ANTECEDENT);
        ArrayList<HashMap<String, String>> consAtomAndValue = anteAndConsEachValues.get(IKADataController.CONSEQUENT);

        ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();
        int index = 0;

        for(HashMap<String, String> atomAndValue : antcAtomAndValue){
            for(Map.Entry<String, String> atom : atomAndValue.entrySet()){
                antecendentData.add(new AtomRow(atom.getKey(), atom.getValue()));
                ComboBox<String> box = antecedentValueMap.get(index);
                if(box ==null){
                    box = new ComboBox<String>();
                    ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomValueList(atom.getKey()));
                    data.add("");
                    box.setItems(data);
                    antecedentValueMap.put(index, box);
                }
                if(atom.getValue() != null)
                    box.getSelectionModel().select(atom.getValue());
                index++;
            }
        }
        index = 0;
        for(HashMap<String, String> atomAndValue : consAtomAndValue){
            for(Map.Entry<String, String> atom : atomAndValue.entrySet()){
                consequentData.add(new AtomRow(atom.getKey(),atom.getValue()));
                ComboBox<String> box = consequentValueMap.get(index);
                if(box ==null){
                    box = new ComboBox<String>();
                    ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomValueList(atom.getKey()));
                    data.add("");
                    box.setItems(data);
                    consequentValueMap.put(index, box);
                }
                if(atom.getValue() != null)
                    box.getSelectionModel().select(atom.getValue());
                index++;
            }
        }

        antecedentTableView.setItems(antecendentData);
        consequentTableView.setItems(consequentData);
    }
    /**
    private void refreshAtomTableView(Rule selectedCompleteRule){
        ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        if(selectedCompleteRule != null){
            for(Atom atm : selectedCompleteRule.getAntecedents()){
                antecendentData.add(new AtomRow(atm.getName(), atm.getName()));
            }
            for(Atom atm : selectedCompleteRule.getConsequents()){
                consequentData.add(new AtomRow(atm.getName(), atm.getName()));
            }
        }

        antecedentTableView.setItems(antecendentData);
        consequentTableView.setItems(consequentData);
    }

    private void refreshAutoCompleteAfterSelectionOrEnter(ComboBox<String> comboBox){
        ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAllAtomList());
        comboBox.setItems(data);
        refreshCompletionRule();
    }
    **/

    /**
     * Complete(:=Previous) Rule Table을 갱신하기 위한 메소드.
     */
    public void refreshCompletionRule(){
        AppTestLog.printLog("Refresh Comlletion Rule Table");

        ArrayList<String> antecedentList = new ArrayList<String>();
        ArrayList<String> consequentList = new ArrayList<String>();

        antecedentList.addAll(antecedentTableView.getItems().stream()
                .map(row -> ((AtomRow) row).getAtom()).collect(Collectors.toList()));
        consequentList.addAll(consequentTableView.getItems().stream()
                .map(row -> ((AtomRow) row).getAtom()).collect(Collectors.toList()));

        ArrayList<String> newCompleteRuleList = IKADataController.getInstance()
                .getRuleCompletionList(antecedentList, consequentList);

        ObservableList<PreviousRuleRow> data = FXCollections.observableArrayList();

        data.addAll(newCompleteRuleList.stream()
                .map(PreviousRuleRow::new).collect(Collectors.toList()));

        completeRuleTable.setItems(data);
        clearPatientInformation();
    }

    /**
     * UI상 Previous Rule을 선택 했을 때 환자 정보, 환자 상세 정보 그리고 소견이
     * 갱신되어야 하기 때문에 이전에 들어있던 값들을 다 Clear 처리 해준다.
     */
    private void clearPatientInformation(){
        patientTable.getItems().clear();
        patientDetailTable.setRoot(new TreeItem<>());
        opinionTextArea.clear();
    }
    /**
     * Modal 로 띄울때 전달해줘야할 작성자 이름.
     * @param author
     */
    public void setAuthor(String author){
        authorLabel.setText(author);
    }

    /**
     * Table의 List를 뿌려주기 위한 요소.
     * @param patientID
     */
    public void setPatientID(Long patientID){
        this.patientID = patientID;
    }

    /**
     * Table의 List를 뿌려주기 위한 요소.
     * @param opinionIndex
     */
    public void setOpinionIndex(int opinionIndex){
        this.indexOfOpinion = opinionIndex;
    }

    /**
     * Table의 List를 뿌려주기 위한 요소.
     * @param dataController
     * @param ruleId
     */
    public void setRule(IKADataController dataController, String ruleId){
        if(ruleId != null)
            _selectedRule = dataController.getRule(Long.valueOf(ruleId));
        initTable();
        refreshCompletionRule();
    }
}
