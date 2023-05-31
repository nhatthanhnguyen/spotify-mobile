package com.ptit.spotify.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utils {
    @SuppressLint("DefaultLocale")
    public static String formatTime(int milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static int generateRandomNumberInRange(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYear(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        try {
            Date dateTime = format.parse(time);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            assert dateTime != null;
            return yearFormat.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
