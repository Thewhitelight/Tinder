package cn.libery.tinder.model;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author shizhiqiang on 2018/8/9.
 * @description
 */
public class BestCombination {
    /**
     * 使用定金金额
     */
    public double bookingAmount;
    /**
     * 使用优惠券金额
     */
    public double couponAmount;
    /**
     * 使用的定金
     */
    public Booking booking;
    /**
     * 使用的优惠券
     */
    public Coupon coupon;

    public BestCombination(Booking booking, Coupon coupon, double bookingAmount, double couponAmount) {
        this.booking = booking;
        this.coupon = coupon;
        this.bookingAmount = bookingAmount;
        this.couponAmount = couponAmount;
    }

    /**
     * 计算最划算的组合
     *
     * @param totalPrice 订单金额
     * @param coupons    排好序的优惠券列表
     * @param bookings   定金列表
     * @return 最优组合
     */
    public static BestCombination calculatorBestCombination(double totalPrice, List<Coupon> coupons, List<Booking> bookings) {
        //TreeMap已对key进行排序,用来装填所有的组合结果,key为支付金额
        SortedMap<Double, BestCombination> map = new TreeMap<>();
        double couponPrice;

        for (Coupon c : coupons) {
            for (Booking b : bookings) {
                double tempPrice = totalPrice;
                if (c.every == 1) {
                    //每满券相当于多张券 张数*优惠金额
                    couponPrice = Math.floor(tempPrice / c.spending) * c.discount;
                } else {
                    couponPrice = c.discount;
                }
                //订单金额-所有优惠金额
                tempPrice = tempPrice - couponPrice;

                //支付金额
                tempPrice = tempPrice - b.amount;
                //当定金面值为正值且支付金额为负值时，排除此价格进入最优价格队列
                if (!(tempPrice < 0 && b.amount > 0)) {
                    if (map.containsKey(tempPrice)) {
                        //当支付金额相同时,如果组合内的定金大于将要插入的定金金额(意味着所使用的优惠券小)，则替换原有的组合
                        BestCombination best = map.get(tempPrice);
                        if (best.bookingAmount >= b.amount) {
                            map.put(tempPrice, new BestCombination(b, c, b.amount, couponPrice));
                        }
                    } else {
                        map.put(tempPrice, new BestCombination(b, c, b.amount, couponPrice));
                    }
                }

            }
        }
        BestCombination bestCombination;
        if (map.size() <= 0) {
            //组合队列为空，则无最优组合
            bestCombination = null;
        } else {
            if (map.firstKey() >= 0) {
                //当组合队列第一个值大于零，则取第一个，此时为最优惠
                bestCombination = map.get(map.firstKey());
            } else {
                double key = 0;
                for (Map.Entry<Double, BestCombination> e : map.entrySet()) {
                    System.out.println(e.getKey() + " " + e.getValue().toString());
                    if (e.getValue().booking.amount > 0) {
                        if (e.getKey() >= 0) {
                            break;
                        } else {
                            //定金大于零则说明优惠券使用了每满且类似10减20的类型 当支付金额小于零时
                            key = e.getKey();
                        }
                    } else {
                        if (e.getKey() > 0) {
                            break;
                        } else {
                            //定金等于0 且支付金额小于等于零
                            key = e.getKey();
                        }
                    }
                }
                bestCombination = map.get(key);
            }
        }
        return bestCombination;
    }

    @Override
    public String toString() {
        return "BestCombination{" +
                "bookingAmount=" + bookingAmount +
                ", couponAmount=" + couponAmount +
                ", booking=" + booking.toString() +
                ", coupon=" + coupon.toString() +
                '}';
    }
}
