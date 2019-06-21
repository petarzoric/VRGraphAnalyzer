package com.petarzoric.vr_feedback.Utility;

/**
 * As the Database stores durations in seconds format, sometimes we need to convert this format.
 * This class takes care of converting formats.
 */

 public class DurationConverter {

    public static String getDurationInMMss(int dur){
        int mins = dur / 60;
        int secs = dur % 60;
        String minutes = Integer.toString(mins);
        String seconds = Integer.toString(secs);
        if(secs < 10){
            seconds = "0"+seconds;
        }
        String toReturn = minutes+":"+seconds;
        return toReturn;
    }
}
