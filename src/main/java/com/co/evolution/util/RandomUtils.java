package com.co.evolution.util;

import java.util.HashSet;
import java.util.Random;

public class RandomUtils {

    public static double nextDouble(double min, double max)
    {
        Random rnd = new Random();
        return (rnd.nextDouble() * (max - min)) + min;
    }

    public static double nextGaussian(double sigma, double mean)
    {
        Random rnd = new Random();
        return sigma * rnd.nextGaussian() + mean;
    }

    public static int nextInt(int max)
    {
        Random rnd = new Random();
        return rnd.nextInt(max);
    }

    public static HashSet<Integer> getDifferentRandomIntegers(int max, int size)
    {
        HashSet<Integer> hs =  new HashSet<>();
        while (hs.size() < size)
            hs.add(nextInt(max));
        return hs;
    }

    public static int nextIntegerWithDefinedDistribution(double[] distribution)
    {
        double[] acumm = new double[distribution.length];
        acumm[0] = distribution[0];
        for (int i = 1; i < distribution.length; i++) {
            acumm[i] = acumm[i-1] + distribution[i];
        }
        double num = nextDouble(0,1);

        for (int i = 0; i < distribution.length; i++) {
            if(num < acumm[i])
                return i;
        }
        return 0;

    }



}
