package com.blacksheep;

import java.util.Arrays;

/**
 * This class contains the implementation edit distance algorithm
 */
public class CalculateEditDistance {

    /***
     * This method calculates the minimum number of changes required to change one file to another
     * @param comment1 : Comment from source file 1
     * @param comment2 : Comment from source file 1
     * @return The minimum number of changes required to change one file to another
     */
    public int calculate(String comment1, String comment2) {
        int[][] dp = new int[comment1.length() + 1][comment2.length() + 1];

        for (int i = 0; i <= comment1.length(); i++) {
            for (int j = 0; j <= comment2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                                    + costOfSubstitution(comment1.charAt(i - 1), comment2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[comment1.length()][comment2.length()];
    }

    /***
     * This method checks if the 2 characters are same or not
     * @param a : character from source file 1
     * @param b : character from source file 2
     * @return 1 if the characters are different, 0 otherwise
     */
    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    /***
     * Returns the minimum of all the numbers
     * @param numbers : all the numbers
     * @return minimum of numbers
     */
    private int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }
}