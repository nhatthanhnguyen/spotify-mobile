package com.ptit.spotify.dto.data;

public class HomeGreetingData {
    private String greetingStr;

    public HomeGreetingData() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            LocalTime localTime = LocalTime.now();
//            int hour = localTime.getHour();
//            greetingStr = "Good evening";
//            if (hour >= 5 && hour < 12) {
//                greetingStr = "Good morning";
//            }
//            if (hour < 18) {
//                greetingStr = "Good afternoon";
//            }
            greetingStr = "Good afternoon";
        }
    }

    public String getGreetingStr() {
        return greetingStr;
    }

    public void setGreetingStr(String greetingStr) {
        this.greetingStr = greetingStr;
    }
}
