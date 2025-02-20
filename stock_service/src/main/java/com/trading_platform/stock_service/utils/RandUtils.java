package com.trading_platform.stock_service.utils;

public class RandUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
