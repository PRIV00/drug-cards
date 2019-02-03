package main.javafxcontrols;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.data.DrugField;


public class DrugFieldHBox extends HBox {

    private TextField drugFieldTextField;
    private Button removeButton;
    private DrugField drugField;

    public DrugFieldHBox(DrugFieldHBoxContainer container) {
        super(5.0);
        initialize(container);
        drugField = new DrugField();
    }

    public DrugFieldHBox(DrugFieldHBoxContainer container, DrugField drugField) {
        super(5.0);
        initialize(container);
        drugFieldTextField.setText(drugField.getField());
        this.drugField = drugField;
    }

    private void initialize(DrugFieldHBoxContainer container) {
        setAlignment(Pos.CENTER);
        drugFieldTextField = new TextField();
        drugFieldTextField.setMinWidth(200.0);
        drugFieldTextField.setPrefWidth(200.0);
        drugFieldTextField.setMaxWidth(200.0);
        removeButton = new Button("x");

        drugFieldTextField.setOnKeyReleased(event -> drugField.setField(drugFieldTextField.getText()));
        removeButton.setOnAction(event -> container.getChildren().remove(this));

        getChildren().addAll(drugFieldTextField, removeButton);
    }

    public DrugField getDrugField() {
        return drugField;
    }

    public void setDrugField(DrugField drugField) {
        this.drugField = drugField;
        drugFieldTextField.setText(drugField.getField());
    }
}
