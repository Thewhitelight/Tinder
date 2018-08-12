package cn.libery.tinder;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.libery.tinder.model.BestCombination;
import cn.libery.tinder.model.Booking;
import cn.libery.tinder.model.Coupon;

/**
 * @author shizhiqiang on 2018/8/9.
 * @description
 */
public class BestOfferTest {

    @Test
    public void bestPath() {
        test();
        BestPath1();
        BestPath2();
        BestPath3();
        BestPath4();
        BestPath5();
        BestPath6();
        BestPath7();
        BestPath8();
        BestPath9();
        BestPath10();
        BestPath11();
        BestPath12();
        BestPath13();
        BestPath14();
    }

    @Test
    public void BestPath14() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCouponM500r400());
        coupons.add(getCoupon700r900());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking100());
        bookings.add(getBooking200());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));
        Assert.assertTrue(b.booking.amount == 100 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 900);
    }

    @Test
    public void BestPath13() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r400());
        coupons.add(getCoupon600r50());
        coupons.add(getCouponM250r300());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking300());
        bookings.add(getBooking400());
        bookings.add(getBooking650());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));
        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 1 && b.coupon.spending == 250 && b.coupon.discount == 300);
    }

    @Test
    public void BestPath12() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCouponM500r600());
        coupons.add(getCouponM250r300());

        List<Booking> bookings = new ArrayList<>();

        BestCombination b = BestCombination.calculatorBestCombination(700, initCoupon(700, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 1 && b.coupon.spending == 500 && b.coupon.discount == 600);
    }

    @Test
    public void BestPath11() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r1100());
        coupons.add(getCoupon500r1100());
        coupons.add(getCoupon500r900());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking100());

        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 1100);
    }

    @Test
    public void BestPath10() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon1000r1050());
        coupons.add(getCoupon700r1100());
        coupons.add(getCoupon500r1100());
        coupons.add(getCoupon500r900());

        List<Booking> bookings = new ArrayList<>();

        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 0 && b.coupon.spending == 1000 && b.coupon.discount == 1050);
    }

    @Test
    public void BestPath9() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r400());
        coupons.add(getCoupon700r399());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking300());
        bookings.add(getBooking400());
        bookings.add(getBooking600());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 600 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 400);
    }

    @Test
    public void BestPath8() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r400());
        coupons.add(getCoupon600r50());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking300());
        bookings.add(getBooking400());
        bookings.add(getBooking650());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 400 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 400);
    }

    @Test
    public void BestPath7() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCouponM500r400());
        coupons.add(getCoupon700r900());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking100());
        bookings.add(getBooking200());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 100 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 900);
    }

    @Test
    public void BestPath6() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r400());
        coupons.add(getCoupon600r50());
        coupons.add(getCouponM250r300());
        coupons.add(getCouponM250r260());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking300());
        bookings.add(getBooking400());
        bookings.add(getBooking650());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 1 && b.coupon.spending == 250 && b.coupon.discount == 260);
    }

    @Test
    public void BestPath5() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCouponM250r300());
        coupons.add(getCouponM500r600());

        List<Booking> bookings = new ArrayList<>();

        BestCombination b = BestCombination.calculatorBestCombination(700, initCoupon(700, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 1 && b.coupon.spending == 500 && b.coupon.discount == 600);
    }

    @Test
    public void BestPath4() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r1100());
        coupons.add(getCoupon500r1100());
        coupons.add(getCoupon500r900());

        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking100());

        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 1100);
    }

    @Test
    public void BestPath3() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon1000r1050());
        coupons.add(getCoupon700r1100());
        coupons.add(getCoupon500r1100());
        coupons.add(getCoupon500r900());
        List<Booking> bookings = new ArrayList<>();

        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 0 && b.coupon.every == 0 && b.coupon.spending == 1000 && b.coupon.discount == 1050);
    }

    @Test
    public void BestPath2() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r400());
        coupons.add(getCoupon700r399());
        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking300());
        bookings.add(getBooking400());
        bookings.add(getBooking600());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 600 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 400);
    }

    @Test
    public void BestPath1() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(getCoupon700r400());
        coupons.add(getCoupon600r50());
        List<Booking> bookings = new ArrayList<>();
        bookings.add(getBooking300());
        bookings.add(getBooking400());
        bookings.add(getBooking650());
        BestCombination b = BestCombination.calculatorBestCombination(1000, initCoupon(1000, coupons), initBooking(bookings));

        Assert.assertTrue(b.booking.amount == 400 && b.coupon.every == 0 && b.coupon.spending == 700 && b.coupon.discount == 400);
    }

    @NonNull
    private Booking getBooking650() {
        Booking result = new Booking();
        result.amount = 650;
        return result;
    }

    @NonNull
    private Booking getBooking600() {
        Booking result = new Booking();
        result.amount = 600;
        return result;
    }

    @NonNull
    private Booking getBooking400() {
        Booking result = new Booking();
        result.amount = 400;
        return result;
    }

    @NonNull
    private Booking getBooking300() {
        Booking result = new Booking();
        result.amount = 300;
        return result;
    }

    @NonNull
    private Booking getBooking200() {
        Booking result = new Booking();
        result.amount = 200;
        return result;
    }

    @NonNull
    private Booking getBooking100() {
        Booking result = new Booking();
        result.amount = 100;
        return result;
    }

    @NonNull
    private Coupon getCoupon600r50() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 600;
        result.discount = 50;
        return result;
    }

    @NonNull
    private Coupon getCoupon700r400() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 700;
        result.discount = 400;
        return result;
    }

    @NonNull
    private Coupon getCoupon700r399() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 700;
        result.discount = 399;
        return result;
    }

    @NonNull
    private Coupon getCoupon1000r1050() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 1000;
        result.discount = 1050;
        return result;
    }

    @NonNull
    private Coupon getCoupon700r1100() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 700;
        result.discount = 1100;
        return result;
    }

    @NonNull
    private Coupon getCoupon500r1100() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 500;
        result.discount = 1100;
        return result;
    }

    @NonNull
    private Coupon getCoupon500r900() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 500;
        result.discount = 900;
        return result;
    }

    @NonNull
    private Coupon getCoupon700r900() {
        Coupon result = new Coupon();
        result.every = 0;
        result.spending = 700;
        result.discount = 900;
        return result;
    }

    @NonNull
    private Coupon getCouponM500r600() {
        Coupon result = new Coupon();
        result.every = 1;
        result.spending = 500;
        result.discount = 600;
        return result;
    }

    @NonNull
    private Coupon getCouponM250r300() {
        Coupon result = new Coupon();
        result.every = 1;
        result.spending = 250;
        result.discount = 300;
        return result;
    }

    private Coupon getCouponM250r260() {
        Coupon result = new Coupon();
        result.every = 1;
        result.spending = 250;
        result.discount = 260;
        return result;
    }

    @NonNull
    private Coupon getCouponM500r400() {
        Coupon result = new Coupon();
        result.every = 1;
        result.spending = 500;
        result.discount = 400;
        return result;
    }

    private List<Coupon> initCoupon(double totalPrice, List<Coupon> coupons) {
        List<Coupon> result = new ArrayList<>();
        if (coupons.size() > 0) {
            for (Coupon b : coupons) {
                //排除掉优惠金额比总价大的券
                if (b.spending <= totalPrice) {
                    result.add(b);
                }
            }
        }

        //依次根据是否每满、优惠满足金额、优惠金额排序
        Collections.sort(result, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {
                int compareSpending = Double.compare(o1.spending, o2.spending);
                if (compareSpending == 0) {
                    int compareEvery = Integer.compare(o2.every, o1.every);
                    if (compareEvery == 0) {
                        return Double.compare(o1.discount, o2.discount);
                    } else {
                        return compareSpending;
                    }
                } else {
                    return compareSpending;
                }
            }
        });

        Coupon emptyCoupon = new Coupon();
        emptyCoupon.discount = 0;
        emptyCoupon.spending = 0;
        emptyCoupon.id = "-1";
        emptyCoupon.every = 0;
        result.add(emptyCoupon);
        return result;
    }

    private List<Booking> initBooking(List<Booking> bookings) {
        List<Booking> result = new ArrayList<>();
        if (bookings.size() > 0) {
            result.addAll(bookings);
        }

        Booking emptyBooking = new Booking();
        emptyBooking.amount = 0;
        emptyBooking.id = "-1";
        result.add(emptyBooking);
        return result;
    }

    @Test
    public void test() {
        Coupon c1 = new Coupon();
        c1.every = 0;
        c1.spending = 10;
        c1.discount = 10;

        Coupon c2 = new Coupon();
        c2.every = 0;
        c2.spending = 20;
        c2.discount = 8;

        Coupon c3 = new Coupon();
        c3.every = 1;
        c3.spending = 10;
        c3.discount = 8;

        Coupon c4 = new Coupon();
        c4.every = 1;
        c4.spending = 5;
        c4.discount = 8;

        Coupon c5 = new Coupon();
        c5.every = 0;
        c5.spending = 5;
        c5.discount = 8;

        Coupon c6 = new Coupon();
        c6.every = 0;
        c6.spending = 5;
        c6.discount = 2;

        List<Coupon> coupons = new ArrayList<>(5);
        coupons.add(c1);
        coupons.add(c2);
        coupons.add(c3);
        coupons.add(c4);
        coupons.add(c5);
        coupons.add(c6);
        int totalPrice = 10;
        coupons = initCoupon(totalPrice, coupons);
        for (Coupon coupon : coupons) {
            System.out.println(coupon.toString());
        }

        BestCombination b = BestCombination.calculatorBestCombination(totalPrice, coupons, initBooking(new ArrayList<Booking>()));
        System.out.println(b.toString());
    }
}
