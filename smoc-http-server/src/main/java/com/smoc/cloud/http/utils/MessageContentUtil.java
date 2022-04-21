package com.smoc.cloud.http.utils;


/**
 * 短信拆分
 */
public class MessageContentUtil {


    /**
     * 短信拆分条数
     *
     * @param content
     * @return
     */
    public static int splitSMSContentNumber(String content) {
        return (int) (content.length() <= 70 ? 1 : Math.ceil((double) content
                .length() / 67));
    }

    /**
     * 国际短信拆分条数
     *
     * @param content
     * @return
     */
    public static int splitInternationalSMSContentNumber(String content) {
        boolean flag = isPureASCII(content);
        //如果是纯英文则按照
        if (flag) {
            return (int) (content.length() <= 140 ? 1 : Math.ceil((double) content
                    .length() / 134));
        } else {
            return (int) (content.length() <= 70 ? 1 : Math.ceil((double) content
                    .length() / 67));
        }

    }


    private static String CHARSET_ASCII = "US-ASCII";

    /**
     * 判断字符串是否只有ASCII的字符
     *
     * @param content
     * @return
     */
    public static boolean isPureASCII(String content) {
        try {
            if (content.equals(new String(content.getBytes(CHARSET_ASCII), CHARSET_ASCII)))
                return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
