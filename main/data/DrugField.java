package main.data;

public class DrugField extends Data {

    private String field;
    private int foreignKey;

    /**
     * This constructor used for when a blank field is needed just to temporarily hold some field String.
     */
    public DrugField() {
        super(0);
        foreignKey = 0;
    }

    public DrugField(Data data) {
        super(0);
        this.field = "";
        foreignKey = data.getId();
    }

    public DrugField(int id, String field, int foreignKey) {
        super(id);
        this.field = field;
        this.foreignKey = foreignKey;
    }

    @Override
    public String toString() {
        return field;
    }

    @Override
    public Object getFromMethod(String attributeName) {
        switch (attributeName) {
            case "id": return getId();
            case "field": return getField();
            case "drugID": return getForeignKey();
            default: return null;
        }
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(int foreignKey) {
        this.foreignKey = foreignKey;
    }
}
