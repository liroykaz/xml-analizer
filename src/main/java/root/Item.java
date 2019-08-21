package root;

public class Item {

    private Integer num;
    private Double value;

    public Item(Integer num, Double value) {
        this.value = value;
        this.num = num;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
