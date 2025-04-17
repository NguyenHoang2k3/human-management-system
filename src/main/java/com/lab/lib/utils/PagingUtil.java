package com.lab.lib.utils;

public class PagingUtil {
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "15";
    public static final int DEFAULT_SIZE_INT = Integer.parseInt(DEFAULT_SIZE);

    private PagingUtil() {
    }

    public static Integer getOffset(Integer page, Integer perPage) {
        return (page - 1) * (perPage == null ? DEFAULT_SIZE_INT : perPage);
    }

    public static Integer getTotalPage(Long totalRecord, Integer perPage) {
        return (int) Math.ceil((double) totalRecord / (perPage == null ? DEFAULT_SIZE_INT : perPage));
    }
}
