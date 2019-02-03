package main.database;

import main.data.DrugField;
import main.data.Drug;
import main.database.tables.DrugDataFieldTable;
import main.database.tables.DrugsTable;
import main.database.tables.Table;

import java.sql.*;
import java.util.*;

/**
 * API for querying data from the database tables for use by the program model. Creates meaningful and complete objects
 * for the model to then use.
 */
public class Database {

    private Connection connection;
    private String url;

    private DrugsTable drugsTable;
    private DrugDataFieldTable[] drugDataFieldTables;
    private DrugDataFieldTable brandNameTable;
    private DrugDataFieldTable drugClassTable;
    private DrugDataFieldTable indicationsTable;
    private DrugDataFieldTable contraindicationsTable;
    private DrugDataFieldTable sideEffectsTable;
    private DrugDataFieldTable considerationsTable;

    public Database() {
        //url = "jdbc:sqlite:C:/Users/Bryn/IdeaProjects/DrugCards/src/main/save/data.db";
        url = "jdbc:sqlite:data.db";

        drugsTable = new DrugsTable(this);
        brandNameTable = new DrugDataFieldTable(this, "BrandNames");
        drugClassTable = new DrugDataFieldTable(this, "DrugClasses");
        indicationsTable = new DrugDataFieldTable(this, "Indications");
        contraindicationsTable = new DrugDataFieldTable(this, "Contraindications");
        sideEffectsTable = new DrugDataFieldTable(this, "SideEffects");
        considerationsTable = new DrugDataFieldTable(this, "Considerations");

        drugDataFieldTables = new DrugDataFieldTable[] { brandNameTable, drugClassTable, indicationsTable,
                contraindicationsTable, sideEffectsTable, considerationsTable };

        createTables(new Table[] {drugsTable, brandNameTable, drugClassTable, indicationsTable, contraindicationsTable,
                                    sideEffectsTable, considerationsTable});
    }

    public void connect() {
        if (!isClosed())
            return;
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        connect();
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        connect();
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables(Table[] tables) {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement stmt = connection.createStatement()){
            stmt.execute("PRAGMA foreign_keys = ON");
            for (Table t : tables) {
                stmt.execute(t.getCreateTableSQL());
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isClosed() {
        try (Statement statement = connection.createStatement()){
            statement.execute("SELECT 1");
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public void insertDrug(Drug drug) {
        try {
            drugsTable.insertData(drug);
            for (DrugField brandName : drug.getBrandNames()) {
                brandName.setForeignKey(drug.getId());
                brandNameTable.insertData(brandName);
            }

            for (DrugField drugClass : drug.getClasses()) {
                drugClass.setForeignKey(drug.getId());
                drugClassTable.insertData(drugClass);
            }

            for (DrugField indication : drug.getIndications()) {
                indication.setForeignKey(drug.getId());
                indicationsTable.insertData(indication);
            }

            for (DrugField contraindication : drug.getContraindications()) {
                contraindication.setForeignKey(drug.getId());
                contraindicationsTable.insertData(contraindication);
            }

            for (DrugField sideEffect : drug.getSideEffects()) {
                sideEffect.setForeignKey(drug.getId());
                sideEffectsTable.insertData(sideEffect);
            }

            for (DrugField consideration : drug.getConsiderations()) {
                consideration.setForeignKey(drug.getId());
                considerationsTable.insertData(consideration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes any DrugFields from the database that are not found in the injected drug.
     *
     * @param drug The drug to get the current drug fields from.
     */
    private void updateTableFromDrugFieldList(Drug drug) {
        try {
            boolean noMatch = true;
            for (DrugField field : brandNameTable.getDataByDrug(drug)) {
                for (DrugField df : drug.getBrandNames()) {
                    if (df.getId() == field.getId())
                        noMatch = false;
                }
                if (noMatch) {
                    brandNameTable.deleteData(field);
                }
                noMatch = true;
            }

            for (DrugField field : drugClassTable.getDataByDrug(drug)) {
                for (DrugField df : drug.getClasses()) {
                    if (df.getId() == field.getId())
                        noMatch = false;
                }
                if (noMatch) {
                    drugClassTable.deleteData(field);
                }
                noMatch = true;
            }

            for (DrugField field : indicationsTable.getDataByDrug(drug)) {
                for (DrugField df : drug.getIndications()) {
                    if (df.getId() == field.getId())
                        noMatch = false;
                }
                if (noMatch) {
                    indicationsTable.deleteData(field);
                }
                noMatch = true;
            }

            for (DrugField field : contraindicationsTable.getDataByDrug(drug)) {
                for (DrugField df : drug.getContraindications()) {
                    if (df.getId() == field.getId())
                        noMatch = false;
                }
                if (noMatch) {
                    contraindicationsTable.deleteData(field);
                }
                noMatch = true;
            }

            for (DrugField field : sideEffectsTable.getDataByDrug(drug)) {
                for (DrugField df : drug.getSideEffects()) {
                    if (df.getId() == field.getId())
                        noMatch = false;
                }
                if (noMatch) {
                    sideEffectsTable.deleteData(field);
                }
                noMatch = true;
            }

            for (DrugField field : considerationsTable.getDataByDrug(drug)) {
                for (DrugField df : drug.getConsiderations()) {
                    if (df.getId() == field.getId())
                        noMatch = false;
                }
                if (noMatch) {
                    considerationsTable.deleteData(field);
                }
                noMatch = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateDrug(Drug drug) {
        try {
            drugsTable.updateData(drug);

            for (DrugField brandName : drug.getBrandNames()) {
                if (brandName.getId() == 0) {
                    brandName.setForeignKey(drug.getId());
                    System.out.println("ID: " + brandName.getId() + ", FIELD: " + brandName.getField() +
                            ", FOREIGN_KEY: " + brandName.getForeignKey());
                    brandNameTable.insertData(brandName);
                }
                else
                    brandNameTable.updateData(brandName);
            }

            for (DrugField drugClass : drug.getClasses()) {
                if (drugClass.getId() == 0) {
                    drugClass.setForeignKey(drug.getId());
                    drugClassTable.insertData(drugClass);
                }
                else
                    drugClassTable.updateData(drugClass);
            }

            for (DrugField indication : drug.getIndications()) {
                if (indication.getId() == 0) {
                    indication.setForeignKey(drug.getId());
                    indicationsTable.insertData(indication);
                }
                else
                    indicationsTable.updateData(indication);
            }

            for (DrugField contraindication : drug.getContraindications()) {
                if (contraindication.getId() == 0) {
                    contraindication.setForeignKey(drug.getId());
                    contraindicationsTable.insertData(contraindication);
                }
                else
                    contraindicationsTable.updateData(contraindication);
            }

            for (DrugField sideEffect : drug.getSideEffects()) {
                if (sideEffect.getId() == 0) {
                    sideEffect.setForeignKey(drug.getId());
                    sideEffectsTable.insertData(sideEffect);
                }
                else
                    sideEffectsTable.updateData(sideEffect);
            }

            for (DrugField consideration : drug.getConsiderations()) {
                if (consideration.getId() == 0) {
                    consideration.setForeignKey(drug.getId());
                    considerationsTable.insertData(consideration);
                }
                else
                    considerationsTable.updateData(consideration);
            }

            updateTableFromDrugFieldList(drug);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDrug(Drug drug) {
        try {
            drugsTable.deleteData(drug);
            for (DrugDataFieldTable t : drugDataFieldTables) {
                t.deleteDataByDrug(drug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the attribute lists of the Drug object, queries the appropriate table to fetch information.
     *
     * @param drug The drug to update.
     */
    public void updateDrugFieldsFromTable(Drug drug) {
        try {
            drug.setBrandNames(brandNameTable.getDataByDrug(drug));
            drug.setClasses(drugClassTable.getDataByDrug(drug));
            drug.setIndications(indicationsTable.getDataByDrug(drug));
            drug.setContraindications(contraindicationsTable.getDataByDrug(drug));
            drug.setSideEffects(sideEffectsTable.getDataByDrug(drug));
            drug.setConsiderations(considerationsTable.getDataByDrug(drug));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Drug> getAllDrugs() {
        try {
            List<Drug> drugs = drugsTable.queryAllData();

            for (Drug d : drugs) {
                updateDrugFieldsFromTable(d);
            }
            return drugs;
        } catch (SQLException e) {
            return null;
        }
    }

    public void insertBrandName(DrugField brandName) {
        try {
            brandNameTable.insertData(brandName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Drug> getDrugsByName(String filteredName) {
        try {
            return drugsTable.queryByName(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByBrandName(String filteredName) {
        try {
            return brandNameTable.queryDrugsByField(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByDrugClass(String filteredName) {
        try {
            return drugClassTable.queryDrugsByField(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByIndication(String filteredName) {
        try {
            return indicationsTable.queryDrugsByField(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByContraindications(String filteredName) {
        try {
            return contraindicationsTable.queryDrugsByField(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsBySideEffects(String filteredName) {
        try {
            return sideEffectsTable.queryDrugsByField(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByConsideration(String filteredName) {
        try {
            return considerationsTable.queryDrugsByField(filteredName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByRoutesInclusive(boolean oral, boolean sublingual, boolean intravenus, boolean intramuscular,
                                                boolean subcutaneous, boolean rectal) {
        try {
            return drugsTable.queryByRoutes(oral, sublingual, intravenus, intramuscular, subcutaneous, rectal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drug> getDrugsByRoutesExclusive(boolean oral, boolean sublingual, boolean intravenus, boolean intramuscular,
                                                boolean subcutaneous, boolean rectal) {
        Set<Drug> drugs = new HashSet<>();
        try {
            if (oral)
                drugs.addAll(drugsTable.queryByOral(true));
            if (sublingual)
                drugs.addAll(drugsTable.queryBySublingual(true));
            if (intravenus)
                drugs.addAll(drugsTable.queryByIntravenus(true));
            if (intramuscular)
                drugs.addAll(drugsTable.queryByIntramuscular(true));
            if (subcutaneous)
                drugs.addAll(drugsTable.queryBySubcutaneous(true));
            if (rectal)
                drugs.addAll(drugsTable.queryByRectal(true));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(drugs);
    }
}
