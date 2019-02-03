package main.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import main.Model;
import main.javafxcontrols.DrugFieldHBoxContainer;
import main.data.Drug;
import main.data.DrugField;

import java.io.IOException;
import java.util.List;

public class Controller {

    @FXML Button undoAllButton;
    @FXML Button saveAllButton;

    @FXML Button clearFiltersButton;
    @FXML TextField filterTextField;

    @FXML CheckBox filterRoutesCheckBox;
    @FXML RadioButton allRoutesRadioButton;
    @FXML GridPane routesGridPane;

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

    @FXML CheckBox filterBrandNamesCheckBox;
    @FXML CheckBox filterDrugClassesCheckBox;
    @FXML CheckBox filterIndicationsCheckBox;
    @FXML CheckBox filterContraindicationsCheckBox;
    @FXML CheckBox filterSideEffectsCheckBox;
    @FXML CheckBox filterConsiderationsCheckBox;

    @FXML Button addBrandNameButton;
    @FXML Button addDrugClassButton;
    @FXML Button addIndicationButton;
    @FXML Button addContraindicationButton;
    @FXML Button addSideEffectButton;
    @FXML Button addConsiderationButton;

    @FXML Button filterDrugListButton;

    @FXML Button addDrugButton;
    @FXML Button removeDrugButton;
    @FXML ListView<Drug> drugListView;

    @FXML Button editDrugButton;
    @FXML VBox informationVBox;

    @FXML Label counterLabel;

    // Below are to hold data for each new tilers that is added. Going to see if I can use the HBox to my advantage by
    // accessing the children, checking if they are either TextField or Button to perform operations.
    private DrugFieldHBoxContainer brandNamesFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer drugClassesFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer indicationsFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer contraindicationsFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer sideEffectsFilters = new DrugFieldHBoxContainer();
    private DrugFieldHBoxContainer considerationsFilters = new DrugFieldHBoxContainer();

    private Model model;

    @FXML
    public void initialize() {
        model = Main.getModel();
        drugListView.setItems(model.getDrugList());

        brandNamesContainer.getChildren().add(brandNamesFilters);
        drugClassesContainer.getChildren().add(drugClassesFilters);
        indicationsContainer.getChildren().add(indicationsFilters);
        contraindicationsContainer.getChildren().add(contraindicationsFilters);
        sideEffectsContainer.getChildren().add(sideEffectsFilters);
        considerationsContainer.getChildren().add(considerationsFilters);

        saveAllButton.setDisable(true);
        routesGridPane.setDisable(true);

        if (model.getDrugList().size() == 1)
            counterLabel.setText("1 Drug Card");
        else
            counterLabel.setText(model.getDrugList().size() + " Drug Cards");

        initListeners();
        try {
            drugListView.getSelectionModel().selectFirst();
        } catch (NullPointerException ignore) {}
    }

    private void showEditWindow(Drug drugToLoad) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/edit.fxml"));
        Parent root = loader.load();
        EditController editController = loader.getController();
        editController.initialize(saveAllButton, drugListView);
        editController.loadDrug(drugToLoad);
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Drug Card");
        stage.show();
    }

    private boolean noFiltersSelected() {
        return  (filterTextField.getText().trim().equals("") && !oralCheckBox.isSelected() && !intravenusCheckBox.isSelected()
        && !intramuscularCheckBox.isSelected() && !rectalCheckBox.isSelected() && !sublingualCheckBox.isSelected() &&
                !subcutaneousCheckBox.isSelected() && brandNamesFilters.getDrugFields().size() == 0 &&
                drugClassesFilters.getDrugFields().size() == 0 && indicationsFilters.getDrugFields().size() == 0 &&
                contraindicationsFilters.getDrugFields().size() == 0 && sideEffectsFilters.getDrugFields().size() == 0 &&
                considerationsFilters.getDrugFields().size() == 0);
    }

    private void fillInformationVBox(Drug drug) {
        informationVBox.getChildren().clear();
        informationVBox.setStyle("-fx-background-radius: 20; -fx-background-color: #02B7E9");

        Insets padding = new Insets(10);
        Insets lrPadding = new Insets(5, 10, 5, 10);
        Insets lrtPadding = new Insets(10, 10, 5, 10);

        Text nameTextDynamic = new Text(drug.getName());
        nameTextDynamic.setStyle("-fx-fill: #0093bc; -fx-font-size: 30px;");
        TextFlow nameTextFlow = new TextFlow();
        nameTextFlow.setStyle("-fx-background-color: #AFEDFF; -fx-background-radius: 20 20 0 0;");
        nameTextFlow.setPadding(padding);
        nameTextFlow.getChildren().add(nameTextDynamic);
        informationVBox.getChildren().add(nameTextFlow);

        // Create a scrollpane to insert the rest of the information into in case it has a lot of information.
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(350.0);
        scrollPane.setMaxHeight(350.0);
        informationVBox.getChildren().add(scrollPane);

        // vBox to contain the extra info in the scroll pane.
        VBox vBox = new VBox();
        scrollPane.setContent(vBox);

        String boldStaticCSS = "-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: #AFEDFF;";
        String dynamicCSS = "-fx-font-size: 16px; -fx-fill: white;";

        if (drug.getBrandNames().size() > 0) {
            Text brandNamesLabelDynamic = new Text("\n" + drug.getDrugFieldsString(drug.getBrandNames()));
            brandNamesLabelDynamic.setStyle("-fx-fill: #02B7E9; -fx-font-size: 16px; -fx-font-style: italic;");
            nameTextFlow.getChildren().add(brandNamesLabelDynamic);
        }

        if (drug.getClasses().size() > 0) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrtPadding);
            Text drugClassesText = new Text("Drug Classes: ");
            drugClassesText.setStyle(boldStaticCSS);
            Text drugClassesTextDynamic = new Text(drug.getDrugFieldsString(drug.getClasses()));
            drugClassesTextDynamic.setStyle(dynamicCSS);
            tf.getChildren().addAll(drugClassesText, drugClassesTextDynamic);
            vBox.getChildren().add(tf);
        }

        if (!drug.noRoutes()) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrPadding);
            Text routesText = new Text("Routes: ");
            routesText.setStyle(boldStaticCSS);
            Text routesTextDynamic = new Text(drug.getRoutesString());
            routesTextDynamic.setStyle(dynamicCSS);
            tf.getChildren().addAll(routesText, routesTextDynamic);
            vBox.getChildren().add(tf);
        }

        if (drug.getIndications().size() > 0) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrPadding);
            Text indicText = new Text("Indications: ");
            indicText.setStyle(boldStaticCSS);
            Text indicDynamicText = new Text(drug.getDrugFieldsString(drug.getIndications()));
            indicDynamicText.setStyle(dynamicCSS);
            tf.getChildren().addAll(indicText, indicDynamicText);
            vBox.getChildren().add(tf);
        }

        if (drug.getContraindications().size() > 0) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrPadding);
            Text contraText = new Text("Contraindications: ");
            contraText.setStyle(boldStaticCSS);
            Text contraTextDynamic = new Text(drug.getDrugFieldsString(drug.getContraindications()));
            contraTextDynamic.setStyle(dynamicCSS);
            tf.getChildren().addAll(contraText, contraTextDynamic);
            vBox.getChildren().add(tf);
        }

        if (drug.getSideEffects().size() > 0) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrPadding);
            Text sideEffectText = new Text("Side Effects: ");
            sideEffectText.setStyle(boldStaticCSS);
            Text sideEffectDynamicText = new Text(drug.getDrugFieldsString(drug.getIndications()));
            sideEffectDynamicText.setStyle(dynamicCSS);
            tf.getChildren().addAll(sideEffectText, sideEffectDynamicText);
            vBox.getChildren().add(tf);
        }

        if (drug.getConsiderations().size() > 0) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrPadding);
            Text considerationsText = new Text("Considerations: ");
            considerationsText.setStyle(boldStaticCSS);
            Text considerationsDynamicText = new Text(drug.getDrugFieldsString(drug.getConsiderations()));
            considerationsDynamicText.setStyle(dynamicCSS);
            tf.getChildren().addAll(considerationsText, considerationsDynamicText);
            vBox.getChildren().add(tf);
        }

        if (!drug.getAction().equals("") && drug.getAction() != null) {
            TextFlow tf = new TextFlow();
            tf.setPadding(lrPadding);
            Text actionText = new Text("Action: ");
            actionText.setStyle(boldStaticCSS);
            Text actionDynamicText = new Text(drug.getAction());
            actionDynamicText.setStyle(dynamicCSS);
            tf.getChildren().addAll(actionText, actionDynamicText);
            vBox.getChildren().add(tf);
        }
    }

    private void initListeners() {
        model.getDrugList().addListener((ListChangeListener<Drug>) c -> {
            if (model.getDrugList().size() == 1)
                counterLabel.setText("1 Drug Card");
            else
            counterLabel.setText(model.getDrugList().size() + " Drug Cards");
        });

        undoAllButton.setOnAction(event -> {
            model.undoChanges();
            drugListView.setItems(model.getDrugList());
        });

        saveAllButton.setOnAction(event -> {
            model.saveChanges();
            saveAllButton.setDisable(true);
        });

        clearFiltersButton.setOnAction(event -> {
            filterTextField.setText("");
            oralCheckBox.setSelected(false);
            intravenusCheckBox.setSelected(false);
            rectalCheckBox.setSelected(false);
            sublingualCheckBox.setSelected(false);
            subcutaneousCheckBox.setSelected(false);
            intramuscularCheckBox.setSelected(false);

            brandNamesFilters.getChildren().clear();
            drugClassesFilters.getChildren().clear();
            indicationsFilters.getChildren().clear();
            contraindicationsFilters.getChildren().clear();
            sideEffectsFilters.getChildren().clear();
            considerationsFilters.getChildren().clear();
        });

        filterRoutesCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue)
                routesGridPane.setDisable(false);
            else if (oldValue)
                routesGridPane.setDisable(true);
        }));

        filterBrandNamesCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                addBrandNameButton.setDisable(false);
            } else if (oldValue) {
                brandNamesFilters.clearFields();
                addBrandNameButton.setDisable(true);
            }
        });

        addBrandNameButton.setOnAction(event -> brandNamesFilters.addDrugField());

        addDrugClassButton.setOnAction(event -> drugClassesFilters.addDrugField());

        addIndicationButton.setOnAction(event -> indicationsFilters.addDrugField());

        addContraindicationButton.setOnAction(event -> contraindicationsFilters.addDrugField());

        addSideEffectButton.setOnAction(event -> sideEffectsFilters.addDrugField());

        addConsiderationButton.setOnAction(event -> considerationsFilters.addDrugField());

        filterDrugListButton.setOnAction(event -> {
            if (noFiltersSelected()) {
                drugListView.setItems(model.getDrugListAll());
                return;
            }

            List<DrugField> brandNames = null;
            List<DrugField> drugClasses = null;
            List<DrugField> indications = null;
            List<DrugField> contraindications = null;
            List<DrugField> sideEffects = null;
            List<DrugField> considerations = null;
            if (filterBrandNamesCheckBox.isSelected())
                brandNames = brandNamesFilters.getDrugFields();
            if (filterDrugClassesCheckBox.isSelected())
                drugClasses = drugClassesFilters.getDrugFields();
            if (filterIndicationsCheckBox.isSelected())
                indications = indicationsFilters.getDrugFields();
            if (filterContraindicationsCheckBox.isSelected())
                contraindications = contraindicationsFilters.getDrugFields();
            if (filterSideEffectsCheckBox.isSelected())
                sideEffects = sideEffectsFilters.getDrugFields();
            if (filterConsiderationsCheckBox.isSelected())
                considerations = considerationsFilters.getDrugFields();

            model.filterDrugList(filterTextField.getText(), oralCheckBox.isSelected(), intravenusCheckBox.isSelected(),
                    rectalCheckBox.isSelected(), sublingualCheckBox.isSelected(), subcutaneousCheckBox.isSelected(),
                    intramuscularCheckBox.isSelected(), allRoutesRadioButton.isSelected(), brandNames, drugClasses,
                    indications, contraindications, sideEffects, considerations);
            try {
                drugListView.setItems(model.getDrugList());
                drugListView.getSelectionModel().selectFirst();
            } catch (NullPointerException ignore) {}
        });

        addDrugButton.setOnAction(event -> {
            try {
                showEditWindow(new Drug());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        removeDrugButton.setOnAction(event -> {
            try {
                model.removeDrug(model.getFocused());
                saveAllButton.setDisable(false);
            } catch (NullPointerException ignore) {}
        });

        drugListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            model.setFocused(newValue);
            try {
                fillInformationVBox(model.getFocused());
            } catch (NullPointerException ignore) {}
        }));

        editDrugButton.setOnAction(event -> {
            try {
                showEditWindow(model.getFocused());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
