package com.example.security_service.utility;

import org.springframework.stereotype.Component;

//this will create sepearet thread for each request and we assigne the user type by the frontend STAFF or MEMBER.Thread
//so base on that userTypeHolder holding the value and according to the value the table which is calling is changed on CustomerUserDetailService

@Component
public class UserContext {


    private static final ThreadLocal<String> userTypeHolder = new ThreadLocal<>();

    public static void setUserType(String userType) {
        userTypeHolder.set(userType);
    }

    public static String getUserType() {
        return userTypeHolder.get();
    }

    public static void clear() {
        userTypeHolder.remove();
    }

}


