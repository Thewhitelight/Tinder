package cn.libery.tinder;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import cn.libery.tinder.model.Coin;

/**
 * @author shizhiqiang on 2018/8/9.
 * @description
 */
public class CoinTest {

    /**
     * 获取最优组合
     *
     * @param target 下单金额
     * @param coins  所有代币
     * @return 最优组合中的代币
     */
    private List<Coin> getBestCombination(double target, List<Coin> coins) {
        List<Coin> result = new ArrayList<>(5);
        List<ArrayList<Coin>> combinations = new ArrayList<>(10);

        //所有代币组合
        combinations = combinationSum(coins, target, combinations);

        TreeMap<Integer, List<Coin>> lastResult = new TreeMap<>();
        //根据组合中的代币 计算出总和按升序排列
        for (List<Coin> arrays : combinations) {
            int temp = 0;
            for (Coin c : arrays) {
                temp += c.denomination;
            }
            lastResult.put(temp, arrays);
        }
        result.addAll(lastResult.lastEntry().getValue());
        return result;
    }

    private List<ArrayList<Coin>> combinationSum(List<Coin> coins,
                                                 double target,
                                                 List<ArrayList<Coin>> result) {
        List<ArrayList<Coin>> res = new ArrayList<>(10);
        if (coins == null || coins.size() == 0) return new ArrayList<>(1);
        List<Coin> realCoins = new ArrayList<>(coins.size());
        for (Coin c : coins) {
            if (c.denomination <= target) {
                realCoins.add(c);
            }
        }
        Collections.sort(realCoins, new Comparator<Coin>() {
            @Override
            public int compare(Coin c1, Coin c2) {
                return Double.compare(c1.denomination, c2.denomination);
            }
        });
        return dfs(realCoins, 0, target, new ArrayList<Coin>(10), res, result);
    }

    private List<ArrayList<Coin>> dfs(List<Coin> candidates,
                                      int startIndex,
                                      double target,
                                      List<Coin> combination,
                                      List<ArrayList<Coin>> res, List<ArrayList<Coin>> result) {

        result.add(new ArrayList<>(combination));

        if (target == 0) {
            res.add(new ArrayList<>(combination));
            return result;
        }

        for (int i = startIndex, size = candidates.size(); i < size; i++) {
            if (i != startIndex && candidates.get(i).denomination == candidates.get(i - 1).denomination) {
                continue;
            }
            if (target < candidates.get(i).denomination) {
                break;
            }
            combination.add(candidates.get(i));
            dfs(candidates, i + 1, target - candidates.get(i).denomination, combination, res, result);
            combination.remove(combination.size() - 1);
        }
        return result;
    }

    @Test
    public void allTest() {
        biTest1();
        biTest2();
        biTest3();
        biTest4();
        biTest5();
        biTest6();
        biTest7();
    }

    @Test
    public void biTest1() {
        List<Coin> result;
        List<Coin> bis = new ArrayList<>();
        bis.add(get1000());
        bis.add(get500());
        bis.add(get500());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        result = getBestCombination(1000, bis);
        Assert.assertEquals(result.size(), 1);
        for (Coin b : result) {
            Assert.assertEquals(b.denomination, 1000, 0.01);
        }
    }

    @Test
    public void biTest2() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get500());
        bis.add(get500());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        List<Coin> bs = getBestCombination(1000, bis);
        int size = bs.size();
        Assert.assertEquals(size, 2);
        for (int i = 0; i < size; i++) {
            Coin b = bs.get(i);
            switch (i) {
                case 0:
                    Assert.assertTrue(b.denomination == 500);
                    break;
                case 1:
                    Assert.assertTrue(b.denomination == 500);
                    break;
            }
        }
    }

    @Test
    public void biTest3() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get1000());
        bis.add(get500());
        bis.add(get500());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        List<Coin> bs = getBestCombination(198, bis);
        int size = bs.size();
        Assert.assertEquals(size, 2);
        for (int i = 0; i < size; i++) {
            Coin b = bs.get(i);
            switch (i) {
                case 0:
                    Assert.assertTrue(b.denomination == 50);
                    break;
                case 1:
                    Assert.assertTrue(b.denomination == 100);
                    break;
            }
        }
    }

    @Test
    public void biTest4() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get1000());
        bis.add(get500());
        bis.add(get500());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        List<Coin> bs = getBestCombination(154, bis);
        Assert.assertEquals(bs.size(), 2);
        for (int i = 0, size = bs.size(); i < size; i++) {
            Coin b = bs.get(i);
            switch (i) {
                case 0:
                    Assert.assertTrue(b.denomination == 50);
                    break;
                case 1:
                    Assert.assertTrue(b.denomination == 100);
                    break;
            }
        }
    }

    @Test
    public void biTest5() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get1000());
        bis.add(get500());
        bis.add(get500());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        List<Coin> bs = getBestCombination(365, bis);
        Assert.assertEquals(bs.size(), 4);
        for (int i = 0, size = bs.size(); i < size; i++) {
            Coin b = bs.get(i);
            switch (i) {
                case 0:
                    Assert.assertTrue(b.denomination == 50);
                    break;
                case 1:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 2:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 3:
                    Assert.assertTrue(b.denomination == 100);
                    break;
            }
        }
    }

    private List<List<Coin>> fullCombination(List<Coin> lstSource) {
        int n = lstSource.size();
        int max = 1 << n;//2的n次方
        List<List<Coin>> lstResult = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            List<Coin> lstTemp = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                //i除以2的j次方
                if ((i >> j & 1) > 0) {
                    lstTemp.add(lstSource.get(j));
                }
            }
            lstResult.add(lstTemp);
        }
        lstResult.remove(0);
        return lstResult;
    }

    @Test
    public void biTest6() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        for (int i = 0; i < 8; i++) {
            bis.add(get20());
        }
        for (int i = 0; i < 9; i++) {
            bis.add(get10());
        }
        List<Coin> bs = getBestCombination(705, bis);
        Assert.assertEquals(bs.size(), 15);
        for (int i = 0, size = bs.size(); i < size; i++) {
            Coin b = bs.get(i);
            switch (i) {
                case 14:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 13:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 12:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 11:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 10:
                    Assert.assertTrue(b.denomination == 50);
                    break;
                case 9:
                    Assert.assertTrue(b.denomination == 50);
                    break;
                case 8:
                    Assert.assertTrue(b.denomination == 50);
                    break;
                case 7:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 6:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 5:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 4:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 3:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 2:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 1:
                    Assert.assertTrue(b.denomination == 20);
                    break;
                case 0:
                    Assert.assertTrue(b.denomination == 10);
                    break;
            }
        }

    }


    @Test
    public void biTest7() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get500());
        bis.add(get500());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        List<Coin> bs = getBestCombination(154, bis);
        Assert.assertEquals(bs.size(), 2);

        for (int i = 0, size = bs.size(); i < size; i++) {
            Coin b = bs.get(i);
            switch (i) {
                case 1:
                    Assert.assertTrue(b.denomination == 100);
                    break;
                case 0:
                    Assert.assertTrue(b.denomination == 50);
                    break;
            }
        }
    }

    @Test
    public void testFullCombination() {
        List<Coin> bis = new ArrayList<>();
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get100());
        bis.add(get50());
        bis.add(get50());
        bis.add(get50());
        for (int i = 0; i < 8; i++) {
            bis.add(get20());
        }
        for (int i = 0; i < 9; i++) {
            bis.add(get10());
        }
        System.out.println(fullCombination(bis).size());
    }

    @NonNull
    private Coin get1000() {
        Coin result = new Coin();
        result.denomination = 1000;
        return result;
    }

    @NonNull
    private Coin get500() {
        Coin result = new Coin();
        result.denomination = 500;
        return result;
    }

    @NonNull
    private Coin get100() {
        Coin result = new Coin();
        result.denomination = 100;
        return result;
    }

    @NonNull
    private Coin get50() {
        Coin result = new Coin();
        result.denomination = 50;
        return result;
    }

    @NonNull
    private Coin get20() {
        Coin result = new Coin();
        result.denomination = 20;
        return result;
    }

    @NonNull
    private Coin get10() {
        Coin result = new Coin();
        result.denomination = 10;
        return result;
    }

}
