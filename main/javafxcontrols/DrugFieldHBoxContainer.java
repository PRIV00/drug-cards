package main.javafxcontrols;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import main.data.DrugField;

import java.util.ArrayList;
import java.util.List;

public class DrugFieldHBoxContainer extends VBox {

    public DrugFieldHBoxContainer() {
        super(5.0);
        setAlignment(Pos.TOP_CENTER);
    }

    public void addDrugField() {
        getChildren().add(new DrugFieldHBox(this));
    }

    public void addDrugField(DrugField drugField) {
        getChildren().add(new DrugFieldHBox(this, drugField));
    }

    public List<DrugField> getDrugFields() {
        List<DrugField> drugFields = new ArrayList<>();
        for (Node x : getChildren()) {
            drugFields.add(((DrugFieldHBox) x).getDrugField());
        }
        return drugFields;
    }

    public void clearFields() {
        getChildren().clear();
    }
}
