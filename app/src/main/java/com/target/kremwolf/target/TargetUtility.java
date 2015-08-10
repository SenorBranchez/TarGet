package com.target.kremwolf.target;

/**
 * Created by kremwolf on 30.07.2015.
 */
public class TargetUtility {

    public static String bytesToString(byte[] input) {

        int offset = 0;
        String ret = null;

        for(byte b : input) {
            if(b != 0) {
                offset++;
            }
            else break;
        }

        try {
            ret = new String(input, 0, offset, "UTF-8");
        }
        catch(Exception e) {

        }

        return ret;
    }
}
