package com.example.metamesh.config;

import java.util.Date;

public class DateConfig {

    public static Date newDate() {
        return new Date(System.currentTimeMillis() + 3600 * 1000);
    }
}
