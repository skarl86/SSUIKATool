package ssu.gui.controller.factory;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.gui.controller.entity.PreviousRuleRow;
import ssu.util.AppTestLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NCri on 2015. 11. 30..
 * Complete(:=Previous) Rule Table의 Cell 생성시 Call되는 Callback 객체.
 */

public class PreviousRuleCallFactor extends RulePopUpViewCallFactor implements Callback<TableColumn<PreviousRuleRow, String>, TableCell<PreviousRuleRow, String>> {

    public PreviousRuleCallFactor(IKARulePopUpViewController context, TableView tableView){
        super(context, tableView);
    }

    @Override
    public TableCell<PreviousRuleRow, String> call(TableColumn<PreviousRuleRow, String> param) {
        TableCell<PreviousRuleRow, String> cell = new TableCell<PreviousRuleRow, String>(){
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setGraphic(null);
            }
        };

        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                AppTestLog.printLog("double clicked!");
                TableCell c = (TableCell) event.getSource();
                AppTestLog.printLog("Cell text: " + c.getText());

                TableView.TableViewSelectionModel<PreviousRuleRow> selectionModel
                        = tableView.getSelectionModel();
                ObservableList selectedCells = selectionModel.getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                HashMap<String, ArrayList<HashMap<String, String>>> atomAndValue =
                        IKADataController.getInstance().getAtomAndValue(c.getText());
                context.refreshAtomTableView(atomAndValue);
            }
        });
        return cell;
    }
}
