package main.data;

public abstract class Data<T extends Object> {

    private int id;

    Data(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int x = 6;
        int result = 1;
        result = x * result + getId();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Data))
            return false;

        Data s = (Data) o;

        return id == s.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract T getFromMethod(String attributeName);
}
