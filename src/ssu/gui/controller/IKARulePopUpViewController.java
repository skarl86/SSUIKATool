package ssu.gui.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import np.com.ngopal.control.AutoFillTextBox;
import ssu.gui.IKATool;
import ssu.object.AtomManager;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;
import ssu.util.AppTestLog;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by NCri on 2015. 11. 14..
 */
public class IKARulePopUpViewController implements Initializable{
    private Rule _selectedRule;
    private final static int COMBO_TYPE_ANT = 0;
    private final static int COMBO_TYPE_COSEQ = 1;


    @FXML TableView antecedentTableView;
    @FXML TableView conseqeuntTableView;
    @FXML TableView completeRuleTable;

    @FXML TableColumn antecedentColumn;
    @FXML TableColumn antecedentValueColumn;
    @FXML TableColumn consequentColumn;
    @FXML TableColumn consequentValueColumn;

    @FXML ComboBox antecedentComboBox;
    @FXML ComboBox consequentComboBox;

    @FXML GridPane mainView;

    @FXML Label authorLabel;

    @FXML HBox splitTopBox;


    private Long patientID;
    private int indexOfOpinion;
    private String newInputValue;

    // Atom Row Entity
    public class AtomRow {
        private final StringProperty atom;
        private final StringProperty value;

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

        private CompleteRuleRow(String atom){
            this.completeRule = new SimpleStringProperty(atom);
        }
        public void setCoompleteRule(String completeRule){ this.completeRule.set(completeRule);}
        public String getCoompleteRule() { return completeRule.get(); }
    }

    class ValueCallFactor implements Callback<TableColumn<String, String>, TableCell<String, String>>{

        @Override
        public TableCell<String, String> call(TableColumn<String, String> param) {
            TableCell<String ,String> cell = new TableCell<String,String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null){
                        AppTestLog.printLog("Initialize Value Column");
                        HBox box= new HBox();
                        ComboBox valueList = new ComboBox();
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

    class AtomCallFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String>>{

        @Override
        public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
            TableCell<AtomRow,String> cell = new TableCell<AtomRow,String>(){
                //                    ImageView imageview = new ImageView();
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
                                System.out.println("[INTERACTION] DATA DELETE!!!!!!");
                                getTableView().getItems().remove(getTableRow().getIndex());
                                refreshCompletionRule();
                            }
                        });

                    }else{
                        setGraphic(null);
                    }
                }
            };
            return cell;
        }
    }

    @FXML
    protected void handleClickOK(ActionEvent event){
        ArrayList<String> antList = new ArrayList<String>();
        ArrayList<String> consqList = new ArrayList<String>();

        for(Object obj : antecedentTableView.getItems()){
            AtomRow item = (AtomRow) obj;
            antList.add(item.getAtom());
        }
        for(Object obj : conseqeuntTableView.getItems()){
            AtomRow item = (AtomRow) obj;
            consqList.add(item.getAtom());
        }

        IKADataController.getInstance().ruleEditDialogOK(antList, consqList, authorLabel.getText(), patientID,indexOfOpinion);
        IKAPaneController.getInstance().refreshPatientOpinionReferenceList(
                IKADataController.getInstance(),patientID, indexOfOpinion);

        Stage stage = (Stage) mainView.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleclickCancel(ActionEvent event) throws IOException {
        System.out.println(event);
        Stage stage = (Stage) mainView.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("[UI] Initialize PopViewController");

        initConsequentAutoCompleteComboBox();
        initAntecendentAutoCompleteComboBox();

    }

    private void refreshTable(ArrayList<String> antecedentList, ArrayList<String> conseqeuntList){

        final ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        final ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        for(String atom : antecedentList){
            antecendentData.add(new AtomRow(atom, ""));
        }
        for(String atom : conseqeuntList){
            consequentData.add(new AtomRow(atom, ""));
        }

        antecedentTableView.setItems(antecendentData);
        conseqeuntTableView.setItems(consequentData);

    }

    private void refreshCompletionRule(){
        ArrayList antecedentList = new ArrayList();
        ArrayList consequentList = new ArrayList();

        for(Object row : antecedentTableView.getItems()){
            antecedentList.add(((AtomRow)row).getAtom());
        }
        for(Object row : conseqeuntTableView.getItems()){
            consequentList.add(((AtomRow)row).getAtom());
        }

        ArrayList<String> newCompleteRuleList = IKADataController.getInstance()
                .getRuleCompletionList(antecedentList, consequentList);

        ObservableList data = FXCollections.observableArrayList();

        for(String rule : newCompleteRuleList){
            data.add(new CompleteRuleRow(rule));
        }

        completeRuleTable.setItems(data);


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
                new PropertyValueFactory<String, String>("value")
        );

        consequentValueColumn.setCellValueFactory(
                new PropertyValueFactory<String, String>("value")
        );

        final ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        final ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        if(_selectedRule != null){
            for(Atom atm : _selectedRule.getAntecedents()){
                antecendentData.add(new AtomRow(atm.getName(), "value"));
            }
            for(Atom atm : _selectedRule.getConsequents()){
                consequentData.add(new AtomRow(atm.getName(), "value"));
            }
        }

        antecedentColumn.setCellFactory(new AtomCallFactor());
        consequentColumn.setCellFactory(new AtomCallFactor());

        antecedentValueColumn.setCellFactory(new ValueCallFactor());
        consequentValueColumn.setCellFactory(new ValueCallFactor());

        antecedentTableView.setItems(antecendentData);
        conseqeuntTableView.setItems(consequentData);

        refreshCompletionRule();
    }
    private void initAntecendentAutoCompleteComboBox(){
        antecedentComboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            // ComboBox에 텍스트를 입력할 때 마다 불리는 이벤트.
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(antecedentComboBox.getEditor().getText().length() == 0)) {
                    newInputValue = newValue;
                    ObservableList data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomCompletionList(newValue));
                    antecedentComboBox.setItems(data);
                }
            }
        });
        antecedentComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            // ComboBox에 리스트 중에서 선택 했을 때.
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                AppTestLog.printLog("Selection");
                antecedentTableView.getItems().add(newInputValue);
            }
        });
        antecedentComboBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            // ComboBox에 텍스트를 입력한 후 "Enter"를 선택했을때,
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    antecedentTableView.getItems().add(newInputValue);
                }
            }
        });
    }
    private void initConsequentAutoCompleteComboBox(){
        consequentComboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            // ComboBox에 텍스트를 입력할 때 마다 불리는 이벤트.
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(consequentComboBox.getEditor().getText().length() == 0)) {
                    newInputValue = newValue;
                    ObservableList data = FXCollections.observableArrayList(IKADataController.getInstance().getAtomCompletionList(newValue));
                    consequentComboBox.setItems(data);
                }
            }
        });
        consequentComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            // ComboBox에 리스트 중에서 선택 했을 때.
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                AppTestLog.printLog("Selection");
                conseqeuntTableView.getItems().add(newInputValue);
            }
        });
        consequentComboBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            // ComboBox에 텍스트를 입력한 후 "Enter"를 선택했을때,
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    conseqeuntTableView.getItems().add(newInputValue);
                }
            }
        });
    }

    public void setAuthor(String author){
        authorLabel.setText(author);
    }
    public void setPatientID(Long patientID){
        this.patientID = patientID;
    }
    public void setOpinionIndex(int opinionIndex){
        this.indexOfOpinion = opinionIndex;
    }
    public void setRule(IKADataController dataController, String ruleId){
        if(ruleId != null)
            _selectedRule = dataController.getRule(Long.valueOf(ruleId));
        initTable();
    }


}
