package ssu.gui.controller.factory;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ssu.gui.controller.IKAAtomValueEditorViewController;
import ssu.gui.controller.IKADataController;
import ssu.gui.controller.IKARulePopUpViewController;
import ssu.gui.controller.entity.AtomRow;
import ssu.util.AppTestLog;

import java.io.IOException;

/**
 * Created by NCri on 2015. 11. 30..
 * Conditions(Consequent, Antecedent) Table의 Cell이 생성될 때 불리는 Callback 객체.
 */

public class AtomCallFactor extends RulePopUpViewCallFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String> > {
    public IKARulePopUpViewController context;
    public AtomCallFactor(IKARulePopUpViewController context){
        super(context);
        this.context = context;
    }
    @Override
    public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
        return new TableCell<AtomRow, String>(){
            Button deleteButton = new Button("X");
            Button addAtomValueButton = new Button("+");

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
                    box.getChildren().addAll(hbox, deleteButton,addAtomValueButton);
                    box.setSpacing(12);



                    //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                    setGraphic(box);

                    deleteButton.setOnAction(event -> {
                        AppTestLog.printLog("DATA DELETE!!!!!!");
                        getTableView().getItems().remove(getTableRow().getIndex());
                        context.refreshCompletionRule();
                    });

                    addAtomValueButton.setOnAction(event -> {
                        AppTestLog.printLog("Call ID "+getTableView().getId());

                        AppTestLog.printLog("edit Atom Value");

                        Stage dialogStage = new Stage();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../AtomValueEditorView.fxml"));
                        Parent root = null;

                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        IKAAtomValueEditorViewController controller = loader.getController();

                        controller.setAtomName(item);
                        controller.setIndex(getTableRow().getIndex());
                        controller.setID(getTableView().getId());
                        controller.setValueList(IKADataController.getInstance().getAtomValueList(item));
                        controller.setContext(context);


                        dialogStage.setScene(new Scene(root));
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.initOwner(
                                ((Node)event.getSource()).getScene().getWindow() );

                        dialogStage.show();

                    });

                }else{
                    setGraphic(null);
                }
            }
        };
    }
}

