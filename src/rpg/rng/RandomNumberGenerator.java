package rpg.rng;

import java.util.Random;

public class RandomNumberGenerator {
    private final Random random = new Random();

    public int getRandomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
