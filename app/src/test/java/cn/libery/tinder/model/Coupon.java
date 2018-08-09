package cn.libery.tinder.model;

/**
 * @author shizhiqiang on 2018/8/9.
 * @description
 */
public class Coupon {
    /**
     * 优惠券id
     */
    public String id;
    /**
     * 优惠券满足金额
     */
    public double spending;
    /**
     * 是否每满 0为否
     */
    public int every;
    /**
     * 优惠金额
     */
    public double discount;

    @Override
    public String toString() {
        return "Coupon{" +
                "id='" + id + '\'' +
                ", spending=" + spending +
                ", every=" + every +
                ", discount=" + discount +
                '}';
    }
}
