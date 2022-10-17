package it.unitn.padalino.assignment2;

import java.util.concurrent.ThreadLocalRandom;

public class GameLogic {

    public static String[] randomPick(String[] list) {

        int[] ints = ThreadLocalRandom.current().ints(0, list.length).distinct().limit(3).toArray();

        String ret1 = list[ints[0]];
        String ret2 = list[ints[1]];
        String ret3 = list[ints[2]];

        String[] ret = {ret1, ret2, ret3};

        return ret;

    }
}
