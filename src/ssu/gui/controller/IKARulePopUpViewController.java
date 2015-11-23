package ssu.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;
import ssu.util.AppTestLog;

import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.util.*;

/**
 * Created by NCri on 2015. 11. 14..
 */
public class IKARulePopUpViewController implements Initializable{
    private Rule _selectedRule;
    private final static int COMBO_TYPE_ANT = 0;
    private final static int COMBO_TYPE_COSEQ = 1;
    private final static String VALUE_COMBOBOX_ID = "valueList";
    private final static String ANTECEDENT_TABLE = "antecendentTable";
    private final static String CONSEQUENT_TABLE = "conseqeuntTable";

    @FXML TableView<AtomRow> antecedentTableView;
    @FXML TableView<AtomRow> consequentTableView;
    @FXML TableView<CompleteRuleRow> completeRuleTable;

    @FXML TableColumn<AtomRow, String> antecedentColumn;
    @FXML TableColumn<AtomRow, String> antecedentValueColumn;
    @FXML TableColumn<AtomRow, String> consequentColumn;
    @FXML TableColumn<AtomRow, String> consequentValueColumn;

    @FXML TableColumn<CompleteRuleRow, String> completeRuleColumn;

    @FXML ComboBox<String> antecedentComboBox;
    @FXML ComboBox<String> consequentComboBox;

    @FXML GridPane mainView;

    @FXML Label authorLabel;

    @FXML HBox splitTopBox;


    private Long patientID;
    private int indexOfOpinion;
    private String newInputValue;

    private HashMap<Integer, ComboBox<String>> antecedentValueMap = new HashMap<Integer, ComboBox<String>>();
    private HashMap<Integer, ComboBox<String>> consequentValueMap = new HashMap<Integer, ComboBox<String>>();

    // Atom Row Entity
    public class AtomRow {
        private final StringProperty atom;
        private final StringProperty value;

        public StringProperty atomProperty(){ return atom; }
        public StringProperty valueProperty(){ return value; }

        private AtomRow(String atom, String value){
            this.atom = new SimpleStringProperty(atom);
            this.value = new SimpleStringProperty(value);
        }
        public void setAtom(String atom){ this.atom.set(atom);}
        public String getAtom() { return atom.get(); }
        public void setValue(String value){ this.value.set(value);}
        public String getValue() { return value.get(); }
    }

    // Antecedent Row Entity
    public class CompleteRuleRow {
        private final StringProperty completeRule;

        public StringProperty completeRuleProperty(){ return completeRule; }

        private CompleteRuleRow(String atom){
            this.completeRule = new SimpleStringProperty(atom);
        }
        public void setCompleteRule(String completeRule){ this.completeRule.set(completeRule);}
        public String getCompleteRule() { return completeRule.get(); }
    }

    class ValueCallFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String>>{

        @Override
        public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
            TableCell<AtomRow ,String> cell = new TableCell<AtomRow,String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null){
                        AppTestLog.printLog("Initialize Value Column");
                        AppTestLog.printLog(getTableView().getId());
                        AppTestLog.printLog(String.valueOf(getIndex()));
                        AppTestLog.printLog(String.format("Value : [%s]", item));

                        HBox box= new HBox();
                        ComboBox<String> valueList = getAtomComboBox(getTableView(), getIndex());

                        valueList.setId(VALUE_COMBOBOX_ID);
                        ObservableList<String> data =
                                FXCollections.observableArrayList(IKADataController.getInstance().getAtomValueList(item));
                        data.add("");
                        valueList.setItems(data);

                        // Value가 존재하면 Value Item을 선택.
                        if(item.length() >= 1){
                            valueList.getSelectionModel().select(item);
                        }

                        box.getChildren().add(valueList);
                        box.setAlignment(Pos.CENTER);

                        setGraphic(box);
                    }else{
                        setGraphic(null);
                    }
                }
            };

            return cell;
        }
    }

    class AtomCallFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String> >{

        @Override
        public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
            return new TableCell<AtomRow, String>(){
                Button deleteButton = new Button();

                Image imageDecline = new Image(getClass().getResourceAsStream("../resources/deleteCellImg.png"));

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item!=null){
                        AppTestLog.printLog("Initialize atom Column");
                        HBox box= new HBox();
//                            box.setSpacing(50) ;
                        HBox hbox = new HBox();
                        hbox.getChildren().add(new Label(item));
                        hbox.setAlignment(Pos.CENTER);
                        deleteButton.setGraphic(new ImageView(imageDecline));
                        deleteButton.setBackground(Background.EMPTY);
                        box.getChildren().addAll(hbox, deleteButton);
                        box.setSpacing(2);

                        //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                        setGraphic(box);

                        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                AppTestLog.printLog("DATA DELETE!!!!!!");
                                getTableView().getItems().remove(getTableRow().getIndex());
                                refreshCompletionRule();
                            }
                        });

                    }else{
                        setGraphic(null);
                    }
                }
            };
        }
    }

    @FXML
    protected void handleClickOK(ActionEvent event){
        ArrayList<String> antList = makeAtomList(antecedentTableView);
        ArrayList<String> consqList = makeAtomList(consequentTableView);

        if(isOKException(antList, consqList)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("에러");
            alert.setHeaderText("잘못 된 Rule.");
            alert.setContentText("Rule이 잘못되었습니다. 확인해 주세요.");

            alert.showAndWait();
        }else{
            IKADataController.getInstance().ruleEditDialogOK(antList, consqList, authorLabel.getText(), patientID,indexOfOpinion);
            IKAPaneController.getInstance().refreshPatientOpinionReferenceList(
                    IKADataController.getInstance(),patientID, indexOfOpinion);

            Stage stage = (Stage) mainView.getScene().getWindow();
            stage.close();
        }
    }

    private ComboBox<String> getAtomComboBox(TableView tableView, int index){
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
    private ArrayList<String> makeAtomList(TableView<AtomRow> tableView){
        ArrayList<String> atomList = new ArrayList<String>();
        for(int i = 0 ; i < tableView.getItems().size(); i++){
            AtomRow item = tableView.getItems().get(i);

            String atomValue = getAtomValue(tableView.getId(), i);

            if(atomValue.length() == 0){
                atomList.add(item.getAtom());
            }else{
                atomList.add(item.getAtom()+"_"+atomValue);
            }
        }

        return atomList;
    }

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

    @FXML
    protected void handleclickCancel(ActionEvent event) throws IOException {
        System.out.println(event);
        Stage stage = (Stage) mainView.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppTestLog.printLog("[UI] Initialize PopViewController");
        initAutoCompleteComboBox(antecedentComboBox, antecedentTableView);
        initAutoCompleteComboBox(consequentComboBox, consequentTableView);
    }

    private void initAutoCompleteComboBox(final ComboBox<String> comboBox, final TableView<AtomRow> tableView){
        ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAllAtomList());
        comboBox.setItems(new SortedList<String>(data, Collator.getInstance()));

        comboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            // ComboBox에 텍스트를 입력할 때 마다 불리는 이벤트.
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(comboBox.getEditor().getText().length() == 0)) {
                    comboBox.show();
                    newInputValue = newValue;
                    ObservableList<String> data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomCompletionList(newValue));
                    comboBox.setItems(data);
                }
            }
        });

        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            // ComboBox에 리스트 중에서 선택 했을 때.
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null){
                    AppTestLog.printLog("Selection");
                    tableView.getItems().add(new AtomRow(newInputValue, ""));
                    refreshAutoCompleteAfterSelectionOrEnter(comboBox);
                    comboBox.getEditor().clear();
                    comboBox.getSelectionModel().clearSelection();
                }
            }
        });

        comboBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            // ComboBox에 텍스트를 입력한 후 "Enter"를 선택했을때,
            @Override
            public void handle(KeyEvent event) {
                AppTestLog.printLog("Enter");
                if(event.getCode() == KeyCode.ENTER){
                    tableView.getItems().add(new AtomRow(newInputValue, ""));
                    refreshAutoCompleteAfterSelectionOrEnter(comboBox);
                    comboBox.getEditor().clear();
                    comboBox.getSelectionModel().clearSelection();

                }
            }
        });
    }

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
                new PropertyValueFactory<CompleteRuleRow, String>("completeRule")
        );

        ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        String[] atomValue = null;

        if(_selectedRule != null){
            for(Atom atm : _selectedRule.getAntecedents()){
                atomValue = IKADataController.getInstance().getAtomAndValue(atm);
                antecendentData.add(new AtomRow(atomValue[0], atomValue[1]));
            }
            for(Atom atm : _selectedRule.getConsequents()){
                atomValue = IKADataController.getInstance().getAtomAndValue(atm);
                consequentData.add(new AtomRow(atomValue[0], atomValue[1]));
            }
        }

        antecedentColumn.setCellFactory(new AtomCallFactor());
        consequentColumn.setCellFactory(new AtomCallFactor());

        antecedentValueColumn.setCellFactory(new ValueCallFactor());
        consequentValueColumn.setCellFactory(new ValueCallFactor());

        antecedentTableView.setItems(antecendentData);
        antecedentTableView.setId(ANTECEDENT_TABLE);
        consequentTableView.setItems(consequentData);
        consequentTableView.setId(CONSEQUENT_TABLE);

        completeRuleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if( newSelection != null){
                if(completeRuleTable.getSelectionModel().getSelectedItem() != null) {
                    TableView.TableViewSelectionModel<CompleteRuleRow> selectionModel = completeRuleTable.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();
                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                    Object val = tablePosition.getTableColumn().getCellData(newSelection);

                    AppTestLog.printLog("Selected Complete Rule. : " + val.toString());
                    HashMap<String, ArrayList<HashMap<String, String>>> atomAndValue =
                            IKADataController.getInstance().getAtomAndValue(val.toString());
//                    Rule selectedCompleteRule = IKADataController.getInstance().getRuleByFormalFormat(val.toString());
//                    refreshAtomTableView(selectedCompleteRule);
                    refreshAtomTableView(atomAndValue);
                }
            }
        });
    }

    private boolean isOKException(ArrayList<String> antecedentList, ArrayList<String> conseqeuntList){ return antecedentList.isEmpty() || conseqeuntList.isEmpty(); }
    private boolean isDuplicateAtom(ArrayList antecedentList, ArrayList conseqeuntList) {
        return true;
    }

    private void refreshAtomTableView(HashMap<String, ArrayList<HashMap<String, String>>> anteAndConsEachValues){
        ArrayList<HashMap<String, String>> antcAtomAndValue = anteAndConsEachValues.get(IKADataController.ANTECEDENT);
        ArrayList<HashMap<String, String>> consAtomAndValue = anteAndConsEachValues.get(IKADataController.CONSEQUENT);

        ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();
        int index = 0;

        for(HashMap<String, String> atomAndValue : antcAtomAndValue){
            for(Map.Entry<String, String> atom : atomAndValue.entrySet()){
                antecendentData.add(new AtomRow(atom.getKey(),""));
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
                consequentData.add(new AtomRow(atom.getKey(),""));
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

    private void refreshCompletionRule(){
        AppTestLog.printLog("Refresh Comlletion Rule Table");

        ArrayList<String> antecedentList = new ArrayList<String>();
        ArrayList<String> consequentList = new ArrayList<String>();

        for(Object row : antecedentTableView.getItems()){
            antecedentList.add(((AtomRow)row).getAtom());
        }
        for(Object row : consequentTableView.getItems()){
            consequentList.add(((AtomRow)row).getAtom());
        }

        ArrayList<String> newCompleteRuleList = IKADataController.getInstance()
                .getRuleCompletionList(antecedentList, consequentList);

        ObservableList<CompleteRuleRow> data = FXCollections.observableArrayList();

        for(String rule : newCompleteRuleList){
            data.add(new CompleteRuleRow(rule));
        }

        completeRuleTable.setItems(data);
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
