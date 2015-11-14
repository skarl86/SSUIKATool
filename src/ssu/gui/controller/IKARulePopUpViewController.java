package ssu.gui.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by NCri on 2015. 11. 14..
 */
public class IKARulePopUpViewController implements Initializable{
    class CellFactor implements Callback<TableColumn<AtomRow, String>, TableCell<AtomRow, String>>{

        @Override
        public TableCell<AtomRow, String> call(TableColumn<AtomRow, String> param) {
            TableCell<AtomRow,String> cell = new TableCell<AtomRow,String>(){
                //                    ImageView imageview = new ImageView();
                Button deleteButton = new Button("-");
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item!=null){
                        if(item.equals(LAST_ROW)){
                            HBox box = new HBox();
                            ObservableList<String> options =
                                    FXCollections.observableArrayList(
                                            "Option 1",
                                            "Option 2",
                                            "Option 3"
                                    );
                            final ComboBox comboBox = new ComboBox(options);
                            comboBox.setPrefSize(getTableRow().getLayoutBounds().getWidth(),getTableRow().getBoundsInLocal().getHeight());
                            box.setPrefSize(getTableRow().getLayoutBounds().getWidth(),getTableRow().getBoundsInLocal().getHeight());
                            box.getChildren().add(comboBox);
                            setGraphic(box);
                        }else{
                            HBox box= new HBox();
//                            box.setSpacing(50) ;
                            HBox hbox = new HBox();
                            hbox.getChildren().add(new Label(item));
                            hbox.setAlignment(Pos.CENTER);
                            box.getChildren().addAll(hbox, deleteButton);

                            box.setSpacing(10);

                            //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                            setGraphic(box);
                        }

                        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("[INTERACTION] DATA DELETE!!!!!!");
                                getTableView().getItems().remove(getTableRow().getIndex());
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

    /** A table cell containing a button for adding a new person. */
    private class AddPersonCell extends TableCell<AtomRow, String> {
        // a button for adding a new person.
        final Button addButton       = new Button("Add");
        // pads and centers the add button in the cell.
        final StackPane paddedButton = new StackPane();
        // records the y pos of the last button press so that the add person dialog can be shown next to the cell.
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        /**
         * AddPersonCell constructor
         * @param stage the stage in which the table is placed.
         * @param table the table to which a new person can be added.
         */
        AddPersonCell(final TableView table) {
            paddedButton.setPadding(new Insets(3));
            paddedButton.getChildren().add(addButton);
            addButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    buttonY.set(mouseEvent.getScreenY());
                }
            });
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    table.getSelectionModel().select(getTableRow().getIndex());
                }
            });
        }

        /** places an add button in the row only if the row is not empty. */
//        @Override protected void updateItem(Boolean item, boolean empty) {
//            super.updateItem(item, empty);
//            if (!empty) {
//                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//                setGraphic(paddedButton);
//            }
//        }
    }

    private String LAST_ROW = "LAST_ROW";

    private Rule _selectedRule;

    @FXML TableView antecedentTableView;
    @FXML TableView conseqeuntTableView;

    @FXML TableColumn antecedentColumn;
    @FXML TableColumn consequentColumn;


    @FXML
    protected void handleClickOK(ActionEvent event){
        System.out.println(event);
    }

    @FXML
    protected void handleclickCancel(ActionEvent event){
        System.out.println(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("[UI] Initialize PopViewController");
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

        for(Atom atm : _selectedRule.getAntecedents()){
            antecendentData.add(new AtomRow(atm.getName()));
        }
        for(Atom atm : _selectedRule.getConsequents()){
            consequentData.add(new AtomRow(atm.getName()));
        }
        antecendentData.add(new AtomRow("LAST_ROW"));
        consequentData.add(new AtomRow("LAST_ROW"));

        // SETTING THE CELL FACTORY FOR THE ALBUM ART
        antecedentColumn.setCellFactory(new CellFactor());

        // SETTING THE CELL FACTORY FOR THE ALBUM ART
        consequentColumn.setCellFactory(new CellFactor());

        antecedentTableView.setItems(antecendentData);
        conseqeuntTableView.setItems(consequentData);
    }

    public void setRule(IKADataController dataController, String ruleId){
        _selectedRule = dataController.getRule(Long.valueOf(ruleId));
        initTable();
    }
}
