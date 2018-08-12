package cn.libery.tinder.model;

/**
 * @author shizhiqiang on 2018/8/10.
 * @description
 */
public class Coin {
    public String id;
    /**
     * 面值
     */
    public double denomination;

    @Override
    public String toString() {
        return "Coin{" +
                "id='" + id + '\'' +
                ", denomination=" + denomination +
                '}';
    }
}
