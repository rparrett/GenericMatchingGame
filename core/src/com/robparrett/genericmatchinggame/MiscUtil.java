package com.robparrett.genericmatchinggame;

import java.util.Random;

/**
 * Created by Rob on 4/24/2015.
 */
public class MiscUtil {
    /**
     * Given an int, return a string like "1st" or "13th"
     *
     * @param i Int to ordinalize
     * @return Ordinalized string
     */
    public static String ordinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }

    /**
     * Shuffle an array or a portion of an array
     *
     * @param array  The array to be shuffled
     * @param length The last index of the portion of the array to be shuffled
     */
    public static void shuffleArray(int[] array, int length) {
        int index, temp;
        Random random = new Random();
        for (int i = length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
