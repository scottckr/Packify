package com.scottcrocker.packify.helper;

import java.util.Random;

/**
 * RandomHelper class, uses Random class.
 */
public class RandomHelper {

    /**
     * Generates a random number from 0 to an input value.
     *
     * @param max Max value for the random number.
     * @return Returns a random number from 0 to 'max'.
     */
    int randomNrGenerator(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }
}
