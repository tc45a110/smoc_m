package com.smoc.cloud.http.utils;

public class Int64UUID {

    public static final long dx =  30*386*12*30*24*3600*1000; // starting at 2000 year
    public static long lastUUID = System.currentTimeMillis() - dx;

    public static synchronized long random(){
        long uuid = System.currentTimeMillis() - dx;
        while(uuid == lastUUID)
            uuid = System.currentTimeMillis() - dx;
        lastUUID = uuid;
        return uuid;
    }

    public static void main(String[] args) {
        System.out.println(Int64UUID.random());
    }

}