package codingchallenge.model;

public class Row implements Comparable<Row> {

     private Integer id;
     private Integer availableCount;

    public Row(int id, int availableCount) {
        this.id = id;
        this.availableCount = availableCount;
    }

    public Row(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public void incrementAvailableCount(int numSeats) {
        this.setAvailableCount(this.getAvailableCount() + numSeats);
    }

    public void decrementAvailableCount(int numSeats) {
        this.setAvailableCount(this.getAvailableCount() - numSeats);
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", availableCount=" + availableCount +
                '}';
    }

    public int compareTo(Row r) {
        if(this.availableCount == r.availableCount)
            return 0;
        else return this.availableCount > r.availableCount ? 1 : -1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row row = (Row) o;

        return id.equals(row.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
