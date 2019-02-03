package main.database.tables;

import main.data.Data;
import main.data.DrugField;
import main.database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class LinkedTable<T extends Data> extends Table<T> {

    private String[] foreignKeys;

    LinkedTable(Database db, String tableName, String primaryKey, String[] columns, String[] foreignKeys) {
        super(db, tableName, primaryKey, columns);
        this.foreignKeys = foreignKeys;
    }

    @Override
    public String getCreateTableSQL() {
        return  "CREATE TABLE IF NOT EXISTS " + getTableName() + " (\n" +
                getPrimaryKey() + ", \n" +
                String.join(", \n", getColumns()) + ", " +
                String.join(", \n", foreignKeys) +
                ")";
    }

    List<DrugField> getDataByKey(int index, int key) throws SQLException {
        getDb().connect();
        Connection connection = getDb().getConnection();
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getColumnName(foreignKeys[index]) + " = " + key;
        List<DrugField> data = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next())
                    data.add(new DrugField(rs.getInt(getColumnName(getPrimaryKey())), rs.getString(getColumnName(getColumns()[0])),
                            rs.getInt(getColumnName(getColumns()[1]))));
            }
        }
        return data;
    }
}
