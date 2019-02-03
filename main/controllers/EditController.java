package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import main.Model;
import main.javafxcontrols.DrugFieldHBoxContainer;
import main.data.Drug;
import main.data.DrugField;

public class EditController {

    @FXML TextField nameTextField;

    @FXML CheckBox oralCheckBox;
    @FXML CheckBox intravenusCheckBox;
    @FXML CheckBox rectalCheckBox;
    @FXML CheckBox sublingualCheckBox;
    @FXML CheckBox subcutaneousCheckBox;
    @FXML CheckBox intramuscularCheckBox;

    @FXML VBox brandNamesContainer;
    @FXML VBox drugClassesContainer;
    @FXML VBox indicationsContainer;
    @FXML VBox contraindicationsContainer;
    @FXML VBox sideEffectsContainer;
    @FXML VBox considerationsContainer;

    @FXML Button addBrandNameButton;
    @FXML Button addDrugClassButton;
    @FXML Button addIndicationButton;
    @FXML Button addContraindicationButton;
    @FXML Button addSideEffectButton;
    @FXML Button addConsiderationButton;

    @FXML TextArea actionTextArea;

    @FXML Button okButton;

    private DrugFieldHBoxContainer brandNamesFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer drugClassesFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer indicationsFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer contraindicationsFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer sideEffectsFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer considerationsFilters = new DrugFieldHBoxContainer();

    private Model model = Main.getModel();
    private Drug drug;

    void initialize(Button saveButton, ListView<Drug> drugListView) {
        brandNamesContainer.getChildren().add(brandNamesFilters);
        drugClassesContainer.getChildren().add(drugClassesFilters);
        indicationsContainer.getChildren().add(indicationsFilters);
        contraindicationsContainer.getChildren().add(contraindicationsFilters);
        sideEffectsContainer.getChildren().add(sideEffectsFilters);
        considerationsContainer.getChildren().add(considerationsFilters);

        addBrandNameButton.setOnAction(event -> brandNamesFilters.addDrugField());

        addDrugClassButton.setOnAction(event -> drugClassesFilters.addDrugField());

        addIndicationButton.setOnAction(event -> indicationsFilters.addDrugField());

        addContraindicationButton.setOnAction(event -> contraindicationsFilters.addDrugField());

        addSideEffectButton.setOnAction(event -> sideEffectsFilters.addDrugField());

        addConsiderationButton.setOnAction(event -> considerationsFilters.addDrugField());

        okButton.setOnAction(event -> saveDrug(drug, saveButton, drugListView));
    }

    void loadDrug(Drug drug) {
        this.drug = drug;
        nameTextField.setText(drug.getName());

        oralCheckBox.setSelected(drug.isOral());
        sublingualCheckBox.setSelected(drug.isSublingual());
        intravenusCheckBox.setSelected(drug.isIntravenus());
        intramuscularCheckBox.setSelected(drug.isIntramuscular());
        rectalCheckBox.setSelected(drug.isRectal());
        subcutaneousCheckBox.setSelected(drug.isSubcutaneous());

        for (DrugField brandName : drug.getBrandNames()) {
            brandNamesFilters.addDrugField(brandName);
        }

        for (DrugField drugClass : drug.getClasses()) {
            drugClassesFilters.addDrugField(drugClass);
        }

        for (DrugField indication : drug.getIndications()) {
            indicationsFilters.addDrugField(indication);
        }

        for (DrugField contraindication : drug.getContraindications()) {
            contraindicationsFilters.addDrugField(contraindication);
        }

        for (DrugField sideEffect : drug.getSideEffects()) {
            sideEffectsFilters.addDrugField(sideEffect);
        }

        for (DrugField consideration : drug.getConsiderations()) {
            considerationsFilters.addDrugField(consideration);
        }

        actionTextArea.setText(drug.getAction());
    }

    private void saveDrug(Drug drug, Button saveButton, ListView<Drug> drugListView) {
        drug.setName(nameTextField.getText());
        drug.setOral(oralCheckBox.isSelected());
        drug.setSublingual(sublingualCheckBox.isSelected());
        drug.setIntravenus(intravenusCheckBox.isSelected());
        drug.setIntramuscular(intramuscularCheckBox.isSelected());
        drug.setRectal(rectalCheckBox.isSelected());
        drug.setSubcutaneous(subcutaneousCheckBox.isSelected());

        drug.setBrandNames(brandNamesFilters.getDrugFields());
        drug.setClasses(drugClassesFilters.getDrugFields());
        drug.setIndications(indicationsFilters.getDrugFields());
        drug.setContraindications(contraindicationsFilters.getDrugFields());
        drug.setSideEffects(sideEffectsFilters.getDrugFields());
        drug.setConsiderations(considerationsFilters.getDrugFields());

        drug.setAction(actionTextArea.getText());

        if (drug.getId() == 0) {
            model.insertDrug(drug);
        } else {
            model.updateDrug(drug);
        }

        drugListView.refresh();
        drugListView.getSelectionModel().select(drug);
        saveButton.setDisable(false);
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
