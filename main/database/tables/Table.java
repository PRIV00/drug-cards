package main.database.tables;

import main.data.Data;
import main.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Table<T extends Data> {

    private Database db;

    private final String tableName;
    private final String primaryKey;

    private String insertSQL;
    private String updateSQL;
    private String deleteSQL;

    private final String[] columns;

    Table(Database db, String tableName, String primaryKey, String[] columns) {
        this.db = db;
        this.tableName = tableName;
        this.primaryKey = primaryKey + " PRIMARY KEY";

        this.columns = columns; // All columns excluding primary key column

        this.insertSQL = getInsertSQL();
        this.updateSQL = getUpdateSQL();
        this.deleteSQL = getDeleteSQL();
    }

    private String getInsertSQL() {
        String valueStore = "";
        // column.size() - 1 because we don't want to include a ? for primary key in our inserts.
        for (int i = 0; i < columns.length - 1; i++) {
            if (i == 0)
                valueStore = "?";
            valueStore = valueStore.concat(", ?");
        }

        List<String> columnNames = new ArrayList<>();
        for (String s : columns) {
            columnNames.add(getColumnName(s));
        }

        return "INSERT OR IGNORE INTO " + tableName + " (\n" + String.join(", \n", columnNames) + "\n) \n" + "VALUES (" +
                valueStore + ")";
    }

    private String getDeleteSQL() {
        return "DELETE FROM " + tableName + " WHERE " + getColumnName(primaryKey) + " = ?";
    }

    private String getUpdateSQL() {
        List<String> columnSets = new ArrayList<>();
        for (String s : columns) {
            String col = s.substring(0, s.indexOf(" "));
            columnSets.add(col + " = ?");
        }
        return  "UPDATE " + tableName + " SET " + String.join(", ", columnSets) +
                " WHERE " + getColumnName(primaryKey) + " = ?";
    }

    String getColumnName(String columnFull) {
        if (columnFull.substring(0, 1).equals("F")) // For foreign key columns.
            return columnFull.substring(13, columnFull.indexOf(")"));
        else
            return columnFull.substring(0, columnFull.indexOf(" "));
    }

    public String getCreateTableSQL() {
        return  "CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                primaryKey + ", \n" +
                String.join(", \n", columns) + ")";
    }

    public final void insertData(T data) throws SQLException {
        db.connect();
        Connection connection = db.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < columns.length; i++) {
                    Object columnData = data.getFromMethod(getColumnName(columns[i]));
                    if (columnData instanceof Integer) {
                        preparedStatement.setInt(i + 1, (Integer) columnData);
                    } else if (columnData instanceof Boolean) {
                        preparedStatement.setBoolean(i + 1, (Boolean) columnData);
                    } else if (columnData instanceof String) {
                        preparedStatement.setString(i + 1, (String) columnData);
                    }
                }

                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        data.setId(generatedKeys.getInt(1));
                    else
                        throw new SQLException("No key was generated");
                }
        }
    }

    public final void updateData(T data) throws SQLException {
        db.connect();
        Connection connection = db.getConnection();

        String idColumnName = getColumnName(primaryKey);

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            for (int i = 1; i <= columns.length; i++) {
                Object columnData = data.getFromMethod(getColumnName(columns[i - 1]));
                if (columnData instanceof Integer) {
                    preparedStatement.setInt(i, (Integer) columnData);
                } else if (columnData instanceof Boolean) {
                    preparedStatement.setBoolean(i, (Boolean) columnData);
                } else if (columnData instanceof String) {
                    preparedStatement.setString(i, (String) columnData);
                }
            }
            preparedStatement.setInt(columns.length + 1, (Integer) data.getFromMethod(idColumnName));
            preparedStatement.executeUpdate();
        }
    }
/*
    public final void updateDataList(List<T> dataList) throws SQLException {
        if (dataList == null)
            return;
        db.connect();
        Connection connection = db.getConnection();

        String idColumnName = getColumnName(primaryKey);

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            for (T data : dataList) {
                for (int i = 1; i <= columns.length; i++) {
                    Object columnData = data.getFromMethod(getColumnName(columns[i - 1]));
                    if (columnData instanceof Integer) {
                        preparedStatement.setInt(i, (Integer) columnData);
                    } else if (columnData instanceof Boolean) {
                        preparedStatement.setBoolean(i, (Boolean) columnData);
                    } else if (columnData instanceof String) {
                        preparedStatement.setString(i, (String) columnData);
                    }
                }
                preparedStatement.setInt(columns.length + 1, (Integer) data.getFromMethod(idColumnName));
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    } */

    public final void deleteData(T data) throws SQLException {
        db.connect();
        Connection connection = db.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, data.getId());
            preparedStatement.executeUpdate();
        }
    }

    String getTableName() {
        return tableName;
    }

    String getPrimaryKey() {
        return primaryKey;
    }

    String[] getColumns() {
        return columns;
    }

    Database getDb() {
        return db;
    }
}
