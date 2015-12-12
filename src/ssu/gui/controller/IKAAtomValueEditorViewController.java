package ssu.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import ssu.gui.controller.entity.AtomRow;
import ssu.util.AppTestLog;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2015-12-12.
 */
public class IKAAtomValueEditorViewController extends IKAController implements Initializable {

    @FXML
    TableView<AtomRow> atomTableView;
    @FXML
    TableColumn<AtomRow,String> atomValueColumn;

    @FXML
    Label atomNameLbl;

    @FXML
    ScrollPane mainPane;

    @FXML
    TextField atomValueTxt;

    String atomName = "";

    ArrayList<AtomRow> atomValueList;
    ObservableList data;

    IKARulePopUpViewController context;
    int index;
    String tableID = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppTestLog.printLog("[UI] Initialize IKAAtomValueEditorViewController");

        atomValueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AtomRow, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AtomRow, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(p.getValue().getValue());
                } else {
                    return new SimpleStringProperty("<E>");
                }
            }
        });

        atomNameLbl.setText(atomName);



    }

    public void setValueList(ArrayList<String> valueList){
        atomValueList = new ArrayList<>();
        valueList.forEach(s -> atomValueList.add(new AtomRow(atomName, s)));
        AppTestLog.printLog("List Size " + atomValueList.size());
        data = FXCollections.observableList(atomValueList);
        atomTableView.setItems(data);
    }

    public  void setAtomName(String atomName){
        atomNameLbl.setText(atomName+" Value List");
        this.atomName = atomName;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void setID(String tableID){
        this.tableID = tableID;
    }

    public void setContext(IKARulePopUpViewController context){
        this.context = context;
    }

    @FXML
    protected void atomValueAddBtnClick(ActionEvent event){
        data.add(new AtomRow(atomName,atomValueTxt.getText()));

    }

    @FXML
    protected void atomValueDeleteBtnClick(ActionEvent event) {
        data.remove(atomTableView.getSelectionModel().getSelectedIndex());
    }

    @FXML
    protected void okBtnClick(ActionEvent event) {
        ArrayList<String> tempArr = new ArrayList<String>();
        for (Object o : data.toArray()) {
            tempArr.add(((AtomRow)o).getValue());
        }

        IKADataController.getInstance().updateAtomValueList(atomName,tempArr);

        context.refreshValueComboBox(tableID,index, tempArr, atomName);

        Stage stage = (Stage) mainPane.getScene().getWindow();


        stage.close();


        //Model로 수정된 Value 값 넘김 (atomName, tempArr)

    }

}
