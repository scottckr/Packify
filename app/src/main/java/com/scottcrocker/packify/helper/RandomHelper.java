package com.scottcrocker.packify.helper;

import java.util.Random;

/**
 * Created by samanthamorrison on 2016-11-11.
 */
public class RandomHelper {

    public int randomNrGenerator(int max){
        Random rand = new Random();
        return rand.nextInt(max);
    }
}
