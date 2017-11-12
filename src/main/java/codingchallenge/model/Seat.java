package codingchallenge.model;


public class Seat implements Cloneable {

    private int id;
    private Status status;

    public Seat(int id, Status status) {
        this.id = id;
        this.status = status;
    }

    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
