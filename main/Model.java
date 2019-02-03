package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.data.Drug;
import main.data.DrugField;
import main.database.Database;

import java.util.*;


public class Model {

    private Drug focused;
    private ObservableList<Drug> drugList;
    private Database db;

    Model() {
        this.db = new Database();
        drugList = FXCollections.observableArrayList(db.getAllDrugs());
        if (drugList.size() > 0)
            focused = drugList.get(0);
    }

    public Drug getFocused() {
        return focused;
    }

    public void setFocused(Drug focused) {
        this.focused = focused;
    }

    public void saveChanges() {
        db.commit();
    }

    public void undoChanges() {
        db.rollback();
        drugList = FXCollections.observableList(db.getAllDrugs());
    }

    public void insertDrug(Drug drug) {
        db.insertDrug(drug);
        drugList.add(drug);
        focused = drug;
    }

    public void updateDrug(Drug drug) {
        db.updateDrug(drug);
    }

    public void removeDrug(Drug drug) {
        db.deleteDrug(drug);
        drugList.remove(drug);
    }

    public ObservableList<Drug> getDrugList() {
        return drugList;
    }

    public ObservableList<Drug> getDrugListAll() {
        drugList = FXCollections.observableArrayList(db.getAllDrugs());
        return drugList;
    }

    /**
     * Returns a list of Drugs based on the user selections that are found in the view. Essentially filters by
     * every possible variable.
     */
    public void filterDrugList(String nameFilter, boolean oral, boolean intravenus, boolean rectal, boolean sublingual,
                               boolean subcutaneous, boolean intramuscular, boolean inclusiveRoutes,
                               List<DrugField> brandNames, List<DrugField> drugClasses, List<DrugField> indications,
                               List<DrugField> contraindications, List<DrugField> sideEffects,
                               List<DrugField> considerations) {
        List<Drug> drugs = new ArrayList<>(db.getDrugsByName(nameFilter));


        if (!(!oral && !intravenus && !rectal  && !sublingual && !subcutaneous && !intramuscular)) {
            if (inclusiveRoutes)
                drugs.retainAll(db.getDrugsByRoutesInclusive(oral, sublingual, intravenus, intramuscular, subcutaneous, rectal));
            else
                drugs.retainAll(db.getDrugsByRoutesExclusive(oral, sublingual, intravenus, intramuscular, subcutaneous, rectal));
        }

        if (brandNames != null) {
            for (DrugField brand : brandNames)
                drugs.retainAll(db.getDrugsByBrandName(brand.getField()));
        }
        if (drugClasses != null) {
            for (DrugField drugClass : drugClasses)
                drugs.retainAll(db.getDrugsByDrugClass(drugClass.getField()));
        }
        if (indications != null) {
            for (DrugField indic : indications)
                drugs.retainAll(db.getDrugsByIndication(indic.getField()));
        }
        if (contraindications != null) {
            for (DrugField contra : contraindications)
                drugs.retainAll(db.getDrugsByContraindications(contra.getField()));
        }
        if (sideEffects != null) {
            for (DrugField effect : sideEffects)
                drugs.retainAll(db.getDrugsBySideEffects(effect.getField()));
        }
        if (considerations != null) {
            for (DrugField cons : considerations)
                drugs.retainAll(db.getDrugsByConsideration(cons.getField()));
        }

        for (Drug d : drugs) {
            db.updateDrugFieldsFromTable(d);
        }

        drugList = FXCollections.observableArrayList(drugs);
    }
}
