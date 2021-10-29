package com.smoc.cloud.common.gateway.utils;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    //响应头
    public static final Map<String, String> responseHeader;

    static {
        responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", "application/json; charset=UTF-8");
        responseHeader.put("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        responseHeader.put("transfer-encoding", "chunked");
        responseHeader.put("X-Content-Type-Options", "nosniff");
        responseHeader.put("X-XSS-Protection", "1; mode=block");
        responseHeader.put("X-Frame-Options", "DENY");
        responseHeader.put("Pragma", "no-cache");
        responseHeader.put("Expires", "0");
    }
}
