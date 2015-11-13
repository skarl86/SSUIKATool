package ssu.gui.controller;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ssu.object.AtomManager;
import ssu.object.patient.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by NCri on 2015. 11. 13..
 */
public class IKAPaneController implements IKAPaneInterface {

    public class PatientRow{
        private final StringProperty subject;
        private final StringProperty textValue;

        private PatientRow(String subject, String textValue){
            this.subject = new SimpleStringProperty(subject);
            this.textValue = new SimpleStringProperty(textValue);
        }

        public void setSubject(String sub){ subject.set(sub); }

        public String getSubject(){ return subject.get(); }

        public void setTextValue(String textVal){ textValue.set(textVal); }

        public String getTextValue(){ return textValue.get(); }

//        public StringProperty subjectProperty() {
//            return this.subject;
//        }
//        public StringProperty textValueProperty() {
//            return this.textValue;
//        }
    }
    public class PatientDetailRow{
        private final StringProperty testName;
        private final StringProperty numValue;
        private final StringProperty textValue;

        private PatientDetailRow(String testName, String numValue, String textValue){
            this.testName= new SimpleStringProperty(testName);
            this.numValue = new SimpleStringProperty(numValue);
            this.textValue = new SimpleStringProperty(textValue);
        }

        public void setTestName(String tName){ testName.set(tName); }
        public String getTestName(){ return testName.get(); }

        public void setNumValue(String nValue){ numValue.set(nValue); }
        public String getNumValue(){ return numValue.get(); }

        public void setTextValue(String tValue){ textValue.set(tValue); }
        public String getTextValue(){ return textValue.get(); }
    }

    private TreeView _patientTreeView;

    private TableView<PatientRow> _patientTableView;
    private TableColumn<PatientRow, String> _subjectColumn;
    private TableColumn<PatientRow, String> _textValueColumn;

    private TableView<PatientDetailRow> _patientDetailTable;
    private TableColumn<PatientDetailRow, String> _testNameColumn;
    private TableColumn<PatientDetailRow, String> _numValueColumn;
    private TableColumn<PatientDetailRow, String> _testTextValueColumn;

    private TextArea _opinionTextArea;

    /**
     * 싱글톤 및 멀티 쓰레드 대비.
     */
    private volatile static IKAPaneController uniqueInstance;

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static IKAPaneController getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (AtomManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new IKAPaneController();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }

    @Override
    public void createPatientTree(TreeView patientTreeView, Map<String, List<IKADataController.PatientListElement>> patientMap) {
        if(_patientTreeView == null)
            _patientTreeView = patientTreeView;

        for( Map.Entry<String, List<IKADataController.PatientListElement>> elem : patientMap.entrySet() ){
            TreeItem<String> root = new TreeItem(elem.getKey());
            for(IKADataController.PatientListElement pat : elem.getValue()){
                TreeItem<String> itemChild = new TreeItem(pat.regId + " (" + pat.name + ")");
                itemChild.setExpanded(false);
                root.getChildren().add(itemChild);
            }
            patientTreeView.setRoot(root);
        }
    }

    @Override
    public void createPatientDefaultList(TableView patientTableView, TableColumn<IKAPaneController.PatientRow, String> subjectColumn, TableColumn<IKAPaneController.PatientRow, String> textValueColumn) {
        if(_patientTableView == null)
            _patientTableView = patientTableView;
        if(_subjectColumn == null)
            _subjectColumn = subjectColumn;
        if(_textValueColumn == null)
            _textValueColumn = textValueColumn;
    }

    @Override
    public void refreshPatientDefaultList(IKADataController dataController, Long patientId){

        IKADataController.PatientDefaultListElement elm = dataController.getPatientsDefaultList(patientId);

        _subjectColumn.setCellValueFactory(
                new PropertyValueFactory<PatientRow, String>("subject")
        );
        _textValueColumn.setCellValueFactory(
                new PropertyValueFactory<PatientRow, String>("textValue")
        );

        final ObservableList<PatientRow> data = FXCollections.observableArrayList(
                new PatientRow("환자명", elm.name),
                new PatientRow("나이", String.valueOf(elm.age)),
                new PatientRow("성별", elm.gender)
        );

        _patientTableView.setItems(data);
    }
    @Override
    public void refreshPatientDetailList(IKADataController dataController, Long patientId){
        ArrayList<IKADataController.PatientDetailListElement> elmList = (ArrayList)dataController.getPatientsDetailList(patientId);

        _testNameColumn.setCellValueFactory(
                new PropertyValueFactory<PatientDetailRow, String>("testName")
        );
        _numValueColumn.setCellValueFactory(
                new PropertyValueFactory<PatientDetailRow, String>("numValue")
        );
        _testTextValueColumn.setCellValueFactory(
                new PropertyValueFactory<PatientDetailRow, String>("textValue")
        );

        final ObservableList<PatientDetailRow> data = FXCollections.observableArrayList();

        for(IKADataController.PatientDetailListElement elm : elmList){
            data.add(new PatientDetailRow(elm.testName, elm.testValue, elm.testTextValue));
        }

        _patientDetailTable.setItems(data);
    }
    @Override
    public void createPatientDetailList(TableView patientDetailTable, TableColumn testNameColumn, TableColumn numValueColumn, TableColumn textValueColumn) {
        if(_patientDetailTable == null)
            _patientDetailTable = patientDetailTable;
        if(_testNameColumn == null)
            _testNameColumn = testNameColumn;
        if(_numValueColumn== null)
            _numValueColumn = numValueColumn;
        if(_testTextValueColumn== null)
            _testTextValueColumn = textValueColumn;
    }

    @Override
    public void createPatientOpinionList(TextArea opinionTextArea) {
        if(_opinionTextArea == null)
            _opinionTextArea = opinionTextArea;
    }

    @Override
    public void refreshPatientOpinionList(IKADataController dataController, Long patientId){
        ArrayList<String> opinionList = (ArrayList) dataController.getPatientOpinion(patientId);
        System.out.println("환자 소견 갯수 : " + opinionList.size());
        _opinionTextArea.setText(opinionList.get(0));
    }

    @Override
    public void createPatientOpinionReferenceList(Long PatientId) {

    }
}
