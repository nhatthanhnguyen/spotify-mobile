package com.ptit.spotify.dto.data;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeGreetingData {
    private String greetingStr;

    public HomeGreetingData() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime localTime = LocalTime.now();
            int hour = localTime.getHour();
            greetingStr = "Good evening";
            if (hour >= 5 && hour < 12) {
                greetingStr = "Good morning";
            }
            if (hour < 18) {
                greetingStr = "Good afternoon";
            }
        }
    }
}
