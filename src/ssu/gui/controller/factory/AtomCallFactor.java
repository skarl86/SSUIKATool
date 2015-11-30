package ssu.gui.controller.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.gui.controller.entity.AtomRow;
import ssu.util.AppTestLog;

/**
 * Created by NCri on 2015. 11. 30..
 * Conditions(Consequent, Antecedent) Table의 Cell이 생성될 때 불리는 Callback 객체.
 */

public class AtomCallFactor extends RulePopUpViewCallFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String> > {
    public AtomCallFactor(IKARulePopUpViewController context){
        super(context);
    }
    @Override
    public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
        return new TableCell<AtomRow, String>(){
            Button deleteButton = new Button("X");

//                Image imageDecline = new Image(getClass().getResourceAsStream("../resources/deleteCellImg.png"));

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(item!=null){
                    AppTestLog.printLog("Initialize atom Column");
                    HBox box= new HBox();
                    HBox hbox = new HBox();
                    hbox.getChildren().add(new Label(item));
                    hbox.setAlignment(Pos.CENTER);
                    box.getChildren().addAll(hbox, deleteButton);
                    box.setSpacing(12);

                    //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                    setGraphic(box);

                    deleteButton.setOnAction(event -> {
                        AppTestLog.printLog("DATA DELETE!!!!!!");
                        getTableView().getItems().remove(getTableRow().getIndex());
                        context.refreshCompletionRule();
                    });

                }else{
                    setGraphic(null);
                }
            }
        };
    }
}

