package com.banba.digitalclock.util;

import java.util.Calendar;

/**
 * Created by Ernan on 16/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class TimeToWords {
    public static final String[] UNITS = {"zero", "one",
            "two", "three", "four", "five",
            "six", "seven", "eight", "nine"};
    public static final String[] TENS = {"zero", "ten",
            "twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"};
    public static final String[] TEENS = {"ten", "eleven",
            "twelve", "thirteen", "fourteen",
            "fifteen", "sixteen", "seventeen",
            "eighteen", "nineteen"};
    private static final StringBuilder builder =
            new StringBuilder();

    public synchronized static String[] timeToWords(
            Calendar date) {
        builder.setLength(0);
        int h = date.get(Calendar.HOUR_OF_DAY);
        int m = date.get(Calendar.MINUTE);
        if (h == 0) {
            builder.append("midnight");
        } else {
            toWords(h, builder, false, " ");
        }
        if (m == 0) {
            if (h > 0) {
                builder.append(":o'clock");
            }
        } else {
            builder.append(":");
            toWords(m, builder, true, ":");
        }
        return builder.toString().split(":");
    }

    private static void toWords(final int number,
                                StringBuilder sb,
                                boolean leadingZero,
                                String separator) {
        int num = number;
        int tens = num / 10;
        if (leadingZero || tens > 0) {
            if (tens == 1) {
                sb.append(TEENS[num - 10]);
                num = 0;
            } else {
                sb.append(TENS[tens]);
            }
        }
        int units = num % 10;
        if (units > 0) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(UNITS[units]);
        }
    }
}
