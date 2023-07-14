package com.github.pastalapate.spawner_utilities.utils;

public class MathUtils {
    public static int map(int X, int A, int B, int C, int D) {
        return (X-A)/(B-A) * (D-C) + C;
    }
}
