package com.oman.forward.pattern.behavior;

public class QueryUtils {

    public static void findUserInfo(String[] names, Strategy strategy) {
        String sql = strategy.getSQL(names);
        System.out.println(sql);
    }
}
