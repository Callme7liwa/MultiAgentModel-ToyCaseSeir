package Utils;

public class CustomRandom {

    private static final MersenneTwister mersenneTwister = new MersenneTwister();

    private static final CustomRandom instance = new CustomRandom();

    public static CustomRandom getInstance() {
        return instance;
    }

    public int generateInt(int seed){
        return mersenneTwister.nextInt(seed);
    }

    public  double generateRandomDoubleValue() {
        return mersenneTwister.nextDouble();
    }

    public  double negExp(double inMean) {
        return -inMean * Math.log(1 - generateRandomDoubleValue());
    }
}
