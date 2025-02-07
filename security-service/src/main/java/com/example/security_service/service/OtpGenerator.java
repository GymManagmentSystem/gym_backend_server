package com.example.security_service.service;


import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OtpGenerator {
    private static final long OTP_EXPIRY_TIME = TimeUnit.MINUTES.toMillis(5);

    public static String[] generateOtp(){
        String [] otpArray=new String[2];

        StringBuilder otp = new StringBuilder();
        Random rand = new Random();

        // Generate a random OTP
        for (int i = 0; i < 6; i++) {
            otp.append(rand.nextInt(10));  // Generates a random number from 0-9
        }
        otpArray[0] = otp.toString();
        otpArray[1]=String.valueOf(System.currentTimeMillis()+OTP_EXPIRY_TIME);
        return otpArray;
    }
}
