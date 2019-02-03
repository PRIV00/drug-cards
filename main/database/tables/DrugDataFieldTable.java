package main.database.tables;

import main.data.DrugField;
import main.data.Drug;
import main.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrugDataFieldTable extends LinkedTable<DrugField> {

    public DrugDataFieldTable(Database db, String tableName) {
        super(db, tableName, "id INTEGER", new String[] {
          "field TEXT", // Contains the relevant info.
          "drugID INTEGER"
        },
                new String[] {
                        "FOREIGN KEY (drugID) REFERENCES Drugs(drugID)"
        });
    }

    public void deleteDataByDrug(Drug drug) throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();
        String sql = "DELETE FROM " + getTableName() + " WHERE drugID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, drug.getId());
            preparedStatement.executeUpdate();
        }
    }

    public List<DrugField> getDataByDrug(Drug drug) throws SQLException {
        return getDataByKey(0, drug.getId());
    }

    public List<DrugField> getAllData() throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();
        String sql = "SELECT * FROM " + getTableName();
        List<DrugField> drugFields = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next())
                    drugFields.add(new DrugField(rs.getInt("id"), rs.getString("field"), rs.getInt("drugID")));
            }
        }
        return drugFields;
    }

    public List<Drug> queryDrugsByField(String fieldFilter) throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();
        String sql = "WITH FilteredFields (drugID) \n" +
                "AS (\n" +
                "SELECT drugID \n" +
                "FROM " + getTableName() +
                " WHERE " + getTableName() + ".field LIKE ('%' || ? || '%')\n" +
                ") \n" +
                "SELECT FilteredFields.drugID, \n" +
                "       Drugs.name, \n" +
                "       Drugs.oral, \n" +
                "       Drugs.sublingual, \n" +
                "       Drugs.intravenus, \n" +
                "       Drugs.intramuscular, \n" +
                "       Drugs.subcutaneous, \n" +
                "       Drugs.rectal, \n" +
                "       Drugs.action \n" +
                "FROM Drugs \n" +
                "INNER JOIN FilteredFields \n" +
                "ON Drugs.drugID = FilteredFields.drugID";
        List<Drug> drugs = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fieldFilter);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    drugs.add(new Drug(rs.getInt(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4),
                            rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7),
                            rs.getBoolean(8), rs.getString(9)));
                }
            }
        }
        return drugs;
    }
}
