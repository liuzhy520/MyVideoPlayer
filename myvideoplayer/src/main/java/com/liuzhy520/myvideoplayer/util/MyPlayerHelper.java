package com.liuzhy520.myvideoplayer.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by wayne on 10/18/16.
 * some tools of the video player
 */

public class MyPlayerHelper {

    /**
     * network speed caulculator
     *
     * @param preBytes last traffic use {@link android.net.TrafficStats}
     * @param curBytes currtent       use {@link android.net.TrafficStats}
     * @param time     time
     * @return speed this unit in B/s
     */
    public static double getNetWorkSpeed(long preBytes, long curBytes, long time) {
        double diff = curBytes - preBytes;
        double diffTime = (double) time / 1000;
        double result = 0;
        try {
            result = (diff / diffTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static DecimalFormat df = new DecimalFormat("#0.00");
    /**
     * @param currentSpeed speed int byte per second
     * @return String with unit such as  3.2Gb/s  200Mb/s  ,...
     */
    public static String getSpeedInAutoUnit(double currentSpeed) {
        String temp = "0.0 B/s";
        df.setRoundingMode(RoundingMode.HALF_UP);

        if (currentSpeed / (1024 * 1024 * 1024) > 1) {//GB
            temp = df.format(currentSpeed / (1024 * 1024 * 1024)) + "GB/s";
        } else if (currentSpeed / (1024 * 1024) > 1) {//MB
            temp = df.format(currentSpeed / (1024 * 1024)) + "MB/s";
        } else if (currentSpeed / (1024) > 1) {//Kb
            temp = df.format(currentSpeed / (1024)) + "KB/s";
        } else if (currentSpeed > 0) {//b
            temp = df.format(currentSpeed / (1024)) + "B/s";
        }

        return temp;
    }
}
