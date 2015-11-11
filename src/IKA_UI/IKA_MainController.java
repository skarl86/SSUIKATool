package IKA_UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

public class IKA_MainController {

    @FXML   // fx:id="addButton"
    private Button addButton;

    @FXML
    void sayHello(ActionEvent event) {
        addButton.setText("Hello, World");
    }
}
