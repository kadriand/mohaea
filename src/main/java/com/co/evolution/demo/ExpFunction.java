package com.co.evolution.demo;

import java.util.Arrays;
import java.util.Comparator;

public class ExpFunction {

    public static void main(String... args) {
        args = new String[]{"ab", "1234"};
        System.out.println(combineStrings(args));
    }

    public static String combineStrings(String... strings) {
        StringBuilder resultingString = new StringBuilder();
        int maxSizeString = Arrays.stream(strings).max(Comparator.comparingInt(String::length)).get().length();
        for (int i = 0; i < maxSizeString; i++) {
            for (String text : strings)
                resultingString.append(i >= text.length() ? " " : text.charAt(i));
        }
        return resultingString.toString();
    }


}
