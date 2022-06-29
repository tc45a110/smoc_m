package com.smoc.cloud.scheduler.batch.filters.service.utils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class SensitiveImport {

    private String ENCODING = "UTF-8";

    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;

        //文件路径
        File file = new File("/Users/wujihui/Desktop/work/smoc_cloud/smoc-filters/src/main/java/com/smoc/cloud/filters/utils/wfc.dic");
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);

        File writeFile = new File(("/Users/wujihui/Desktop/work/smoc_cloud/smoc-filters/src/main/java/com/smoc/cloud/filters/utils/sensitive.sql"));
        if (!writeFile.exists()) {
            writeFile.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(writeFile);
            if (file.isFile() && file.exists()) {
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                int i = 0;
                StringBuffer stringBuffer = new StringBuffer();
                while ((txt = bufferedReader.readLine()) != null) {
                    i++;
                    String line ="insert into smoc.filter_key_words_info(ID,KEY_WORDS_BUSINESS_TYPE,BUSINESS_ID,KEY_WORDS_TYPE,KEY_WORDS,CREATED_BY,CREATED_TIME)  values('" + i + "','SYSTEM','BLACK','BLACK','" + txt + "','SYSTEM','2022-05-21 13:54:06');";
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                }
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } else {
                throw new Exception("文件不存在");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();
        }
        return set;
    }

    public static void main(String[] args) throws Exception {
//        SensitiveImport sensitiveImport = new SensitiveImport();
//        sensitiveImport.readSensitiveWordFile();
        Integer num = new Integer("02");
        System.out.println(num);
    }
}
