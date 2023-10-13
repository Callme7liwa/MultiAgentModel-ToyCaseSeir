package Utils;

import java.util.Random;

public class CustomRandom {

    private static Random random ;

    private static CustomRandom instance;

    private CustomRandom() {
        random = new Random();
    }

    public static CustomRandom getInstance() {
        if (instance == null) {
            synchronized (CustomRandom.class) {
                if (instance == null) {
                    instance = new CustomRandom();
                }
            }
        }
        return instance;
    }

    public  double generateRandomValue() {
        return random.nextDouble();
    }

    public  double negExp(double inMean) {
        return -inMean * Math.log(1 - generateRandomValue());
    }

}
