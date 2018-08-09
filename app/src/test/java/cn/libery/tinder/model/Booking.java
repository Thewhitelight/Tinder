package cn.libery.tinder.model;

/**
 * @author shizhiqiang on 2018/8/9.
 * @description
 */
public class Booking {
    /**
     * 定金Id
     */
    public String id;
    /**
     * 定金金额
     */
    public double amount;

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                '}';
    }
}
