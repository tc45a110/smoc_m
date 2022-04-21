package com.smoc.cloud.http.utils;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;

public class FileBASE64Utils {

    /**
     * BASE64解码成File文件
     *
     * @param base64
     * @param destPath 磁盘路径
     * @param fileName 文件名
     */
    public static void base64ToFile(String base64, String destPath, String fileName) throws Exception {
        File file = null;
        //创建文件目录
        String filePath = destPath;
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
//            byte[] bytes = Base64.getDecoder().decode(base64);
            byte[] bytes = Base64.getMimeDecoder().decode(base64);
            file = new File(filePath + "/" + fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将本地图片转换成Base64编码字符串
     *
     * @param file 文件目录路径
     * @return
     */
    public static String getFileToBase64(String file) {
        //将文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream inputStream = null;
        byte[] buffer = null;
        //读取图片字节数组
        try {
            inputStream = new FileInputStream(file);
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            buffer = new byte[count];
            inputStream.read(buffer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对字节数组Base64编码
        return new BASE64Encoder().encode(buffer);
    }

    /**
     * 精确计算base64字符串文件大小（单位：KB）
     * @param base64String
     * @return
     */
    public static double base64FileSize(String base64String) {
        /**检测是否含有base64,文件头)*/
        if (base64String.lastIndexOf(",") > -1) {
            base64String = base64String.substring(base64String.lastIndexOf(",") + 1);
        }
        /** 获取base64字符串长度(不含data:audio/wav;base64,文件头) */
        int size0 = base64String.length();
        if (size0 > 10) {
            /** 获取字符串的尾巴的最后10个字符，用于判断尾巴是否有等号，正常生成的base64文件'等号'不会超过4个 */
            String tail = base64String.substring(size0 - 10);
            /** 找到等号，把等号也去掉,(等号其实是空的意思,不能算在文件大小里面) */
            int equalIndex = tail.indexOf("=");
            if (equalIndex > 0) {
                size0 = size0 - (10 - equalIndex);
            }
        }
        /** 计算后得到的文件流大小，单位为字节 */
        return (size0 - ((double) size0 / 8) * 2)/1024;
    }

}