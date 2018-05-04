package com.netcracker.binomial.divisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class DivisorAmountSolution {

    public static final int MODULO = 1000000007;
    public static final int MODULO_UPPER_BOUND_POWER = getModuloUpperBoundPower();
    public static final int SHIFT_SIZE = 63 - MODULO_UPPER_BOUND_POWER;

    public static int getModuloUpperBoundPower() {
        int power = 0;
        int upperBound = 1;
        while (upperBound < MODULO) {
            upperBound = upperBound << 1;
            power++;
        }
        return power;
    }

    public static void main(String[] args) {
        //input
        Scanner scanner = new Scanner(System.in);

        String inputLine = scanner.nextLine();
        String[] inputineNumbers = inputLine.split("\\s");
        int k = parseInt(inputineNumbers[0]);
        int n = parseInt(inputineNumbers[1]);

        primeList = generatePrimes(510000);

        Map<Integer, Integer> factorMap = factorBinCoef(k, n);
        int divisorAmount = getDivisorAmount(factorMap);
        divisorAmount = divisorAmount % MODULO;
        System.out.println(divisorAmount);
    }

    private static int getDivisorAmount(Map<Integer, Integer> factorMap) {
        long result = 1;
        for (Entry<Integer, Integer> entry : factorMap.entrySet()) {
            Integer value = entry.getValue();
            result = (result * (value+1)) % MODULO;
        }
        return Long.valueOf(result).intValue();
    }

    private static final int twoInPower(int power) {
        if (power < SHIFT_SIZE) {
            return 1 << power;
        } else {
            long result = 1;
            int remainingPower = power;
            while (remainingPower > SHIFT_SIZE) {
                result = (result << SHIFT_SIZE) % MODULO;
                remainingPower -= SHIFT_SIZE;
            }
            result = (result << remainingPower) % MODULO;
            return Long.valueOf(result).intValue();
        }
    }

    private static List<Integer> primeList;

    private static List<Integer> generatePrimes(int limit) {
        final int numPrimes = countPrimesUpperBound(limit);
        List<Integer> primes = new ArrayList<Integer>(numPrimes);
        boolean[] isComposite = new boolean[limit];   // all false
        final int sqrtLimit = (int) Math.sqrt(limit); // floor
        for (int i = 2; i <= sqrtLimit; i++) {
            if (!isComposite[i]) {
                primes.add(i);
                for (int j = i * i; j < limit; j += i) // `j+=i` can overflow
                    isComposite[j] = true;
            }
        }
        for (int i = sqrtLimit + 1; i < limit; i++)
            if (!isComposite[i])
                primes.add(i);
        return primes;
    }

    static int countPrimesUpperBound(int max) {
        return max > 1 ? (int) (1.25506 * max / Math.log((double) max)) : 0;
    }

    private static Map<Integer, Integer> factorBinCoef(int k, int n) {
        Map<Integer, Integer> top = factorFactorial(n);
        Map<Integer, Integer> left = factorFactorial(k);
        Map<Integer, Integer> right = factorFactorial(n - k);

        Map<Integer, Integer> minusLeft = minus(top, left);
        return minus(minusLeft, right);
    }

    private static Map<Integer, Integer> factorFactorial(int n) {
        Map<Integer, Integer> factorMap = new HashMap<Integer, Integer>();

        outer:
        for (Integer prime : primeList) {
            int p = prime.intValue();
            if (p > n) {
                break outer;
            }
            double currentFraction = n;
            int powerForP = 0;
            while (currentFraction >= 1.0) {
                currentFraction /= p;
                powerForP += (int) currentFraction;
            }
            factorMap.put(p, powerForP);
        }

        return factorMap;
    }

    private static Map<Integer, Integer> minus(Map<Integer, Integer> one,
                                               Map<Integer, Integer> two) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (Entry<Integer, Integer> entry : one.entrySet()) {
            Integer p = entry.getKey();
            Integer powerOne = entry.getValue();
            Integer powerTwo = two.get(p);
            if (powerTwo != null) {
                int difference = powerOne - powerTwo;
                if (difference > 0) {
                    result.put(p, difference);
                }
            } else {
                result.put(p, powerOne);
            }
        }
        return result;
    }

    private static Map<Integer, Integer> intersect(Map<Integer, Integer> one,
                                                   Map<Integer, Integer> two) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();

        Set<Integer> commonP = one.keySet();
        commonP.retainAll(two.keySet());

        for (Integer p : commonP) {
            Integer powerOne = one.get(p);
            Integer powerTwo = two.get(p);
            if (powerOne < powerTwo) {
                result.put(p, powerOne);
            } else {
                result.put(p, powerTwo);
            }
        }

        return result;
    }

    // Hardcoded prime arrays

    private static int[][] getPrimeMatrix() {
        int[][] primeMatrix = new int[6][];

        return primeMatrix;
    }
}
