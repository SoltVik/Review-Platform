package platform.domain;

public class Rating {
    int user_id;
    int sum;
    int amount;
    double avarage;

    public Rating() {}

    public Rating(int user_id, int sum, int amount, double avarage) {
        this.user_id = user_id;
        this.sum = sum;
        this.amount = amount;
        this.avarage = avarage;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public double getAvarage() {
        return avarage;
    }

    public void setAvarage(double avarage) {
        this.avarage = avarage;
    }
}
