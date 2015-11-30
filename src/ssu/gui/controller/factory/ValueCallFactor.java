package ssu.gui.controller.factory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.gui.controller.entity.AtomRow;
import ssu.util.AppTestLog;

/**
 * Created by NCri on 2015. 11. 30..
 * Conditions(Consequent, Antecedent)의 Value Table의 Cell이 생성될 때 불리는 Callback 객체.
 */
public class ValueCallFactor extends RulePopUpViewCallFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String>> {
    public ValueCallFactor(IKARulePopUpViewController context) { super(context); }

    @Override
    public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
        TableCell<AtomRow, String> cell = new TableCell<AtomRow, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    AppTestLog.printLog("Initialize Value Column");
                    AppTestLog.printLog(getTableView().getId());
                    AppTestLog.printLog(String.valueOf(getIndex()));
                    AppTestLog.printLog(String.format("Value : [%s]", item));

                    HBox box = new HBox();
                    ComboBox<String> valueList = context.getAtomComboBox(getTableView(), getIndex());

                    valueList.setId(IKARulePopUpViewController.VALUE_COMBOBOX_ID);
                    ObservableList<String> data =
                            FXCollections.observableArrayList(IKADataController.getInstance().getAtomValueList(item));
                    data.add("");
                    valueList.setItems(data);

                    // Value가 존재하면 Value Item을 선택.
                    if (item.length() >= 1) {
                        valueList.getSelectionModel().select(item);
                    }

                    box.getChildren().add(valueList);
                    box.setAlignment(Pos.CENTER);

                    setGraphic(box);
                } else {
                    setGraphic(null);
                }
            }
        };

        return cell;
    }
}
