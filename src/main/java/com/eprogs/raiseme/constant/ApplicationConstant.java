package com.eprogs.raiseme.constant;

public class ApplicationConstant {

    public static final String JWT_SECRET_KEY = "dl/TcfiQMl14lCA6fJpip5eHO4SiSoAWGqbV+iLWcR0=";
    public static final String JWT_SECRET_DEFAULT_VALUE = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    public static final long JWT_TOKEN_VALIDITY_IN_MILLIS = 1000 * 60 * 60 * 10;
    public static final String JWT_HEADER = "Authorization";
    public static final long SIGNED_URL_DURATION_SECONDS = 3600;
    public static final long SIGNED_URL_DURATION_7_DAYS = 604800;
    public static final long MAX_PUBLIC_FILE_SIZE = 1024 * 1024 * 2; // 2MB , make sure to update in FE also if changed
    public static final int DANGER_ZONE_DAYS_UNTIL_DEADLINE = 45;
    public static final long DANGER_ZONE_COMPLETION_PERCENTAGE_THREHOLD = 70;
    public static final String EXPIRINGSOON = "expiringSoon";
    public static final String EXPIRED = "expired";
    public static final String ACTIVE = "active";
}
