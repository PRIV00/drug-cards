package main.database.tables;

import main.data.Drug;
import main.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrugsTable extends Table<Drug> {

    public DrugsTable(Database db) {
        super(db, "Drugs", "drugID INTEGER", new String[] {
                "name TEXT",
                "oral BOOLEAN",
                "sublingual BOOLEAN",
                "intravenus BOOLEAN",
                "intramuscular BOOLEAN",
                "subcutaneous BOOLEAN",
                "rectal BOOLEAN",
                "action TEXT"
        });
    }

    public List<Drug> queryAllData() throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();
        String sql = "SELECT * FROM Drugs";
        List<Drug> drugs = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next())
                    drugs.add(new Drug(rs.getInt("drugID"), rs.getString("name"), rs.getBoolean("oral"), rs.getBoolean("sublingual"),
                            rs.getBoolean("intravenus"), rs.getBoolean("intramuscular"), rs.getBoolean("subcutaneous"),
                            rs.getBoolean("rectal"), rs.getString("action")));
            }
        }
        return drugs;
    }

    public List<Drug> queryByName(String filter) throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();
        String sql = "SELECT * FROM Drugs WHERE name LIKE ?";
        List<Drug> drugs = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, '%' + filter + '%');
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next())
                    drugs.add(new Drug(rs.getInt("drugID"), rs.getString("name"), rs.getBoolean("oral"), rs.getBoolean("sublingual"),
                            rs.getBoolean("intravenus"), rs.getBoolean("intramuscular"), rs.getBoolean("subcutaneous"),
                            rs.getBoolean("rectal"), rs.getString("action")));
            }
        }
        return drugs;
    }

    public List<Drug> queryByRoutes(boolean oral, boolean sublingual, boolean intravenus, boolean intramuscular,
                                    boolean subcutaneous, boolean rectal) throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();

        String sql = "SELECT * FROM Drugs WHERE oral = ? AND sublingual = ? AND intravenus = ? AND intramuscular = ? AND " +
                "subcutaneous = ? AND rectal = ?";

        List<Drug> drugs = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, oral);
            preparedStatement.setBoolean(2, sublingual);
            preparedStatement.setBoolean(3, intravenus);
            preparedStatement.setBoolean(4, intramuscular);
            preparedStatement.setBoolean(5, subcutaneous);
            preparedStatement.setBoolean(6, rectal);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next())
                    drugs.add(new Drug(rs.getInt("drugID"), rs.getString("name"), rs.getBoolean("oral"), rs.getBoolean("sublingual"),
                            rs.getBoolean("intravenus"), rs.getBoolean("intramuscular"), rs.getBoolean("subcutaneous"),
                            rs.getBoolean("rectal"), rs.getString("action")));
            }
        }

        return drugs;
    }

    private List<Drug> queryByColumn(int columnIndex, Object queryBy) throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();

        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getColumnName(getColumns()[columnIndex]) + " = ?";

        List<Drug> resultList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            if (queryBy instanceof String)
                preparedStatement.setString(1, (String) queryBy);
            else if (queryBy instanceof Integer)
                preparedStatement.setInt(1, (Integer) queryBy);
            else if (queryBy instanceof Boolean)
                preparedStatement.setBoolean(1, (Boolean) queryBy);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next())
                    resultList.add(new Drug(rs.getInt("drugID"), rs.getString("name"), rs.getBoolean("oral"), rs.getBoolean("sublingual"),
                            rs.getBoolean("intravenus"), rs.getBoolean("intramuscular"), rs.getBoolean("subcutaneous"),
                            rs.getBoolean("rectal"), rs.getString("action")));
            }
        }
        return resultList;
    }

    public List<Drug> queryByOral(boolean oral) throws SQLException {
        return queryByColumn(1, oral);
    }


    public List<Drug> queryBySublingual(boolean sublingual) throws SQLException {
        return queryByColumn(2, sublingual);
    }


    public List<Drug> queryByIntravenus(boolean intravenus) throws SQLException {
        return queryByColumn(3, intravenus);
    }


    public List<Drug> queryByIntramuscular(boolean intramuscular) throws SQLException {
        return queryByColumn(4, intramuscular);
    }


    public List<Drug> queryBySubcutaneous(boolean subcutaneous) throws SQLException {
        return queryByColumn(5, subcutaneous);
    }


    public List<Drug> queryByRectal(boolean rectal) throws SQLException {
        return queryByColumn(6, rectal);
    }
}
