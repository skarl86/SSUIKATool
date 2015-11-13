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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    public class PatientReferenceRow{
        private final StringProperty ruleId;
        private final StringProperty rule;
        private final StringProperty author;
        private final StringProperty madeDate;
        private final StringProperty modifiedDate;

        private PatientReferenceRow(String ruleId, String rule, String author, String madeData, String modifiedDate){
            this.ruleId= new SimpleStringProperty(ruleId);
            this.rule = new SimpleStringProperty(rule);
            this.author= new SimpleStringProperty(author);
            this.madeDate= new SimpleStringProperty(madeData);
            this.modifiedDate = new SimpleStringProperty(modifiedDate);
        }
        public String getRuleId() { return ruleId.get(); }
        public StringProperty ruleIdProperty() { return ruleId; }
        public void setRuleId(String ruleId) { this.ruleId.set(ruleId); }

        public String getRule() { return rule.get(); }
        public StringProperty ruleProperty() { return rule; }
        public void setRule(String rule) { this.rule.set(rule); }

        public String getAuthor() { return author.get(); }
        public StringProperty authorProperty() { return author; }
        public void setAuthor(String author) { this.author.set(author); }

        public String getMadeDate() { return madeDate.get(); }
        public StringProperty madeDateProperty() { return madeDate; }
        public void setMadeDate(String madeDate) { this.madeDate.set(madeDate); }

        public String getModifiedDate() { return modifiedDate.get(); }
        public StringProperty modifiedDateProperty() { return modifiedDate; }
        public void setModifiedDate(String modifiedDate) { this.modifiedDate.set(modifiedDate); }
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

    private TableView<PatientReferenceRow> _ruleReferenceTableView;
    private TableColumn<PatientReferenceRow, String> _ruleIdColumn;
    private TableColumn<PatientReferenceRow, String> _ruleColumn;
    private TableColumn<PatientReferenceRow, String> _authorColumn;
    private TableColumn<PatientReferenceRow, String> _madeDateColumn;
    private TableColumn<PatientReferenceRow, String> _modifiedDateColumn;

    private int _opinionIndex = 0;
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
        _opinionTextArea.setText(opinionList.get(_opinionIndex));
    }

    @Override
    public void createPatientOpinionReferenceList(TableView ruleReferenceTableView, TableColumn ruleIdColumn,
                                                  TableColumn ruleColumn, TableColumn authorColumn,
                                                  TableColumn madeDateColumn, TableColumn modifiedDateColumn) {
        if(_ruleReferenceTableView == null)
            _ruleReferenceTableView = ruleReferenceTableView;
        if(_ruleIdColumn == null)
            _ruleIdColumn = ruleIdColumn;
        if(_ruleColumn == null)
            _ruleColumn = ruleColumn;
        if(_authorColumn == null)
            _authorColumn = authorColumn;
        if(_madeDateColumn == null)
            _madeDateColumn = madeDateColumn;
        if(_modifiedDateColumn == null)
            _modifiedDateColumn = modifiedDateColumn;
    }

    @Override
    public void refreshPatientOpinionReferenceList(IKADataController dataController, Long patientId){
        ArrayList<IKADataController.OpinionReferenceList> elmList = (ArrayList)dataController.getOpinionReferenceList(patientId);

        _ruleIdColumn.setCellValueFactory(
                new PropertyValueFactory<PatientReferenceRow, String>("ruleId")
        );
        _ruleColumn.setCellValueFactory(
                new PropertyValueFactory<PatientReferenceRow, String>("rule")
        );
        _authorColumn.setCellValueFactory(
                new PropertyValueFactory<PatientReferenceRow, String>("author")
        );
        _madeDateColumn.setCellValueFactory(
                new PropertyValueFactory<PatientReferenceRow, String>("madeDate")
        );
        _modifiedDateColumn.setCellValueFactory(
                new PropertyValueFactory<PatientReferenceRow, String>("modifiedDate")
        );

        final ObservableList<PatientReferenceRow> data = FXCollections.observableArrayList();

        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");

        for(IKADataController.OpinionReferenceList opn : elmList){
            data.add(new PatientReferenceRow(String.valueOf(opn.ruleId), opn.rule, opn.author, format.format(opn.madeDate), format.format(opn.modifiedDate)));
        }

        _ruleReferenceTableView.setItems(data);
    }

    public void nextOpinion(IKADataController dataController, Long patientId){
        ArrayList<String> opinionList = (ArrayList) dataController.getPatientOpinion(patientId);

        _opinionIndex += 1;

        if(_opinionIndex > opinionList.size() - 1) {
            _opinionIndex = 0;
            _opinionTextArea.setText(opinionList.get(_opinionIndex));
        }else{
            _opinionTextArea.setText(opinionList.get(_opinionIndex));
        }
    }

    public void previousOpinion(IKADataController dataController, Long patientId){
        ArrayList<String> opinionList = (ArrayList) dataController.getPatientOpinion(patientId);

        _opinionIndex -= 1;

        if(_opinionIndex < 0) {
            _opinionIndex = opinionList.size() - 1;
            _opinionTextArea.setText(opinionList.get(_opinionIndex));
        }else{
            _opinionTextArea.setText(opinionList.get(_opinionIndex));
        }
    }
}
