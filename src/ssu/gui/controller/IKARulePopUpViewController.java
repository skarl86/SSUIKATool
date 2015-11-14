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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import ssu.gui.IKATool;
import ssu.object.AtomManager;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

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
    private String COMBOBOX = "COMBOBOX";

    private Rule _selectedRule;

    @FXML TableView antecedentTableView;
    @FXML TableView conseqeuntTableView;

    @FXML TableColumn antecedentColumn;
    @FXML TableColumn consequentColumn;

    @FXML ComboBox completeRuleComboBox;

    @FXML GridPane mainView;

    @FXML TextField authorTextField;

    private Long patientID;
    private int indexOfOpinion;

    class CellFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String>>{

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
                        if(item.equals(COMBOBOX)){
                            HBox box = new HBox();

                            final ComboBox comboBox = new ComboBox();
                            comboBox.setVisible(true);
                            comboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
                                @Override
                                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                    comboBox.show();
                                    comboBox.setItems(FXCollections.observableArrayList(IKADataController.getInstance().getAtomCompletionList(newValue)));
                                }
                            });

                            comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                                @Override
                                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                    boolean isContains = false;
//                                    for(AtomRow item : getTableView().getItems()){
//                                        if(item.getAtom().equals(newValue.toString())) {
//                                            isContains = true;
//                                            break;
//                                        }
//                                    }
//                                    if(!isContains)
                                    getTableView().getItems().add(new AtomRow(newValue.toString()));

                                    setCompletionRule();

                                }
                            });
                            comboBox.setEditable(true);

                            comboBox.setPrefSize(220,30);

                            box.getChildren().add(comboBox);
                            setGraphic(box);

                        }else{
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
                        }

                        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("[INTERACTION] DATA DELETE!!!!!!");
                                getTableView().getItems().remove(getTableRow().getIndex());
                                setCompletionRule();
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

    public class AtomRow {
        private final StringProperty atom;

        private AtomRow(String atom){
            this.atom = new SimpleStringProperty(atom);
        }
        public void setAtom(String atom){ this.atom.set(atom);}
        public String getAtom() { return atom.get(); }
    }

    @FXML
    protected void handleClickOK(ActionEvent event){
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("확인");
//        alert.setHeaderText(completeRuleComboBox.getPromptText());
//        alert.setContentText("위 Rule을 정말 저장하시겠습니까?");
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK){
//            // 데이터 삭제.
//            Stage stage = (Stage) mainView.getScene().getWindow();
//            stage.close();
//
////            IKADataController.getInstance().ruleEditDialogOK();
//        } else {
//            // 취소.
//        }

        ArrayList<String> antList = new ArrayList<String>();
        ArrayList<String> consqList = new ArrayList<String>();

        for(Object obj : antecedentTableView.getItems()){
            AtomRow item = (AtomRow) obj;
            if(!item.getAtom().equals(COMBOBOX))
                antList.add(item.getAtom());
        }
        for(Object obj : conseqeuntTableView.getItems()){
            AtomRow item = (AtomRow) obj;
            if(!item.getAtom().equals(COMBOBOX))
                consqList.add(item.getAtom());
        }

        IKADataController.getInstance().ruleEditDialogOK(antList, consqList, authorTextField.getText());
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
        completeRuleComboBox.setItems(FXCollections.observableArrayList());
        completeRuleComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("Selected");
                if(newValue != null){
                    Rule completionRule = IKADataController.getInstance().getRuleByFormalFormat(newValue.toString());
                    ArrayList<String> antList = new ArrayList<String>();
                    ArrayList<String> consqList = new ArrayList<String>();
                    for(Atom atom :completionRule.getAntecedents()){
                        antList.add(atom.getName());
                    }
                    for(Atom atom :completionRule.getConsequents()){
                        consqList.add(atom.getName());
                    }

                    // CompleteRule Combobox 를 선택하면, 선택한 룰로 바꿔준다.
                    refreshTable(antList, consqList);
                    completeRuleComboBox.getSelectionModel().clearSelection();
                }
            }
        });
    }
    private void refreshTable(ArrayList<String> antecedentList, ArrayList<String> conseqeuntList){
        // 현재 테이블에 있는 Antecedent 값을
        // 새로운 값을 넣어주기 위해서 다 지움.

        final ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        final ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        antecendentData.add(new AtomRow(COMBOBOX));
        consequentData.add(new AtomRow(COMBOBOX));

        for(String atom : antecedentList){
            antecendentData.add(new AtomRow(atom));
        }
        for(String atom : conseqeuntList){
            consequentData.add(new AtomRow(atom));
        }
//        for(Object obj : antecedentTableView.getItems()){
//            AtomRow item = (AtomRow) obj;
//            if(!item.getAtom().equals(COMBOBOX)){
//                antecedentTableView.getItems().remove(item);
//            }
//        }
//        for(Object obj : conseqeuntTableView.getItems()){
//            AtomRow item = (AtomRow) obj;
//            if(!item.getAtom().equals(COMBOBOX)){
//                antecedentTableView.getItems().remove(item);
//            }
//        }
        antecedentTableView.setItems(antecendentData);
        conseqeuntTableView.setItems(consequentData);

    }
    private void initTable(){
        antecedentColumn.setCellValueFactory(
                new PropertyValueFactory<AtomRow, String>("atom")
        );

        consequentColumn.setCellValueFactory(
                new PropertyValueFactory<AtomRow, String>("atom")
        );

        final ObservableList<AtomRow> antecendentData = FXCollections.observableArrayList();
        final ObservableList<AtomRow> consequentData = FXCollections.observableArrayList();

        antecendentData.add(new AtomRow(COMBOBOX));
        consequentData.add(new AtomRow(COMBOBOX));

        if(_selectedRule != null){
            for(Atom atm : _selectedRule.getAntecedents()){
                antecendentData.add(new AtomRow(atm.getName()));
            }
            for(Atom atm : _selectedRule.getConsequents()){
                consequentData.add(new AtomRow(atm.getName()));
            }
        }
        // SETTING THE CELL FACTORY FOR THE ALBUM ART
        antecedentColumn.setCellFactory(new CellFactor());

        // SETTING THE CELL FACTORY FOR THE ALBUM ART
        consequentColumn.setCellFactory(new CellFactor());

        antecedentTableView.setItems(antecendentData);
        conseqeuntTableView.setItems(consequentData);

        setCompletionRule();


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

    private void setCompletionRule(){
        ArrayList antecedentList = new ArrayList();
        ArrayList consequentList = new ArrayList();
        for(Object row : antecedentTableView.getItems()){
            if(!((AtomRow)row).getAtom().equals(COMBOBOX))
                antecedentList.add(((AtomRow)row).getAtom());
        }
        for(Object row : conseqeuntTableView.getItems()){
            if(!((AtomRow)row).getAtom().equals(COMBOBOX))
                consequentList.add(((AtomRow)row).getAtom());
        }

        completeRuleComboBox.setItems(FXCollections.observableArrayList(IKADataController
                .getInstance()
                .getRuleCompletionList(antecedentList, consequentList)));

        if(completeRuleComboBox.getItems().size() > 0){
            completeRuleComboBox.setPromptText(completeRuleComboBox.getItems().get(0).toString());
        }else{
            completeRuleComboBox.setPromptText("");
        }

    }
}
