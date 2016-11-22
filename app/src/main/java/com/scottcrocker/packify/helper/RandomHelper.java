package com.scottcrocker.packify.helper;

import java.util.Random;

/**
 * Created by samanthamorrison on 2016-11-11.
 */
public class RandomHelper {

    public int randomNrGenerator(int min, int max){
        Random rand = new Random();
        int randomNr = rand.nextInt(min - max);
        return randomNr;
    }
}
