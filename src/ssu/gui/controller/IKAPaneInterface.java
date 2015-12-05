package ssu.gui.controller;

import javafx.scene.control.*;
import ssu.gui.controller.entity.PatientDetailRow;

import java.util.List;
import java.util.Map;

/**
 * Created by NCri on 2015. 11. 13..
 */
public interface IKAPaneInterface {

    public void createPatientTree(TreeView patientTreeView, Map<String, List<IKADataController.PatientListElement>> patientMap);
    public void createPatientDefaultList(TableView patientTableView, TableColumn<IKAPaneController.PatientRow, String> subjectColumn, TableColumn<IKAPaneController.PatientRow, String> textValueColumn);
    public void refreshPatientDefaultList(IKADataController dataController, Long patientId);
    public void refreshPatientDetailList(IKADataController dataController, Long patientId);
    public void createPatientDetailList(
            TreeTableView<String> patientDetailTable, TreeTableColumn<PatientDetailRow, String> testNameColumn,
            TreeTableColumn<PatientDetailRow, String> numValueColumn, TreeTableColumn<PatientDetailRow, String> textValueColumn);
    public void createPatientOpinionList(TextArea opinionTextArea);
    public void createOpinionList(IKADataController dataController, ListView<String> listView, Long patientId);
    //public void refreshPatientOpinionList(IKADataController dataController, Long patientId);
    public void refreshPatientOpinionList(IKADataController dataController, Long patientId);
    public void createPatientOpinionReferenceList(TableView ruleReferenceTableView, TableColumn ruleIdColumn,
                                                  TableColumn ruleColumn, TableColumn authorColumn,
                                                  TableColumn madeDateColumn, TableColumn modifiedDateColumn);
    public void refreshPatientOpinionReferenceList(IKADataController dataController, Long patientId, int indexOfOpinion);
    public void deleteRuleReferenceList(IKADataController dataController, TableView tableView, Long patientId);
    public void nextOpinion(IKADataController dataController, Long patientId);
    public void previousOpinion(IKADataController dataController, Long patientId);
    public void refreshOpinionPageLabel(IKADataController dataController, Label pageLabel, Long patientID);
}
