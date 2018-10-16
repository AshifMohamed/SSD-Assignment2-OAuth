package com.ashif.oauth.git.Utils;

import java.util.UUID;

public class Random_Number {

    public static String generateUniqueNumber(){

        return UUID.randomUUID().toString();
    }
}
