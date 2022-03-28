package com.smoc.cloud.scheduler.init;

import com.google.gson.Gson;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class InitService {

    public void initSql(List<? extends InitModel> list) {

        try {
//           File file = new File("E:\\data\\txt\\sql.txt");
//           if (!file.exists()) {
//               file.createNewFile();
//           }

            FileWriter out = new FileWriter("E:\\data\\txt\\sql.txt", true);

            List<String> sql = new ArrayList<>();

            String today = DateTimeUtils.getDateFormat(new Date());

            for(int i=0;i<31;i++) {
                String date = DateTimeUtils.checkOption(today,-i);
                for (InitModel obj : list) {
                    Integer customerSubmitSum = new Random().nextInt(9999999);
                    Integer successSubmitSum = customerSubmitSum - new Random().nextInt(999);
                    Integer failureSubmitSum = new Random().nextInt(999);
                    Integer messageSuccessSum = successSubmitSum - new Random().nextInt(999);
                    Integer messageFailureSum = new Random().nextInt(999);
                    StringBuffer sqlBuffer = new StringBuffer("insert into message_daily_statistics(ID,BUSINESS_ACCOUNT,CHANNEL_ID,CARRIER,CUSTOMER_SUBMIT_NUM,SUCCESS_SUBMIT_NUM,FAILURE_SUBMIT_NUM,MESSAGE_SUCCESS_NUM,MESSAGE_FAILURE_NUM,MESSAGE_NO_REPORT_NUM,MESSAGE_DATE,MESSAGE_SIGN,CHANNEL_BATCH_DATE,ACCOUNT_BATCH_DATE,CREATED_TIME,ENTERPRISE_FLAG,BUSINESS_TYPE,PRICE_AREA_CODE,AREA_CODE)");
                    sqlBuffer.append(" values(");
                    sqlBuffer.append(" '" + UUID.uuid32() + "',");
                    sqlBuffer.append(" '" + obj.getBusinessAccount() + "',");
                    sqlBuffer.append(" '" + obj.getChannelId() + "',");
                    sqlBuffer.append(" '" + obj.getCarrier() + "',");
                    sqlBuffer.append(" " + customerSubmitSum + ",");
                    sqlBuffer.append(" " + successSubmitSum + ",");
                    sqlBuffer.append(" " + failureSubmitSum + ",");
                    sqlBuffer.append(" " + messageSuccessSum + ",");
                    sqlBuffer.append(" " + messageFailureSum + ",");
                    sqlBuffer.append(" 0,");
                    sqlBuffer.append(" '" + date + "',");
                    sqlBuffer.append(" '【星语网络】',");
                    sqlBuffer.append(" '0',");
                    sqlBuffer.append(" '0',");
                    sqlBuffer.append(" now(),");
                    sqlBuffer.append(" '" + obj.getBusinessAccount().substring(0, 3) + "',");
                    sqlBuffer.append(" 'BUSINESS_TYPE',");
                    sqlBuffer.append(" 'ALL',");
                    sqlBuffer.append(" 'AREA_CODE'");
                    sqlBuffer.append(" );");
                    sql.add(sqlBuffer.toString());
                    out.write(sqlBuffer.toString());
                    out.write("\r\n");
                    //刷新IO内存流
                    out.flush();
                }
            }

            out.close();
            log.info("[sql]:{}", sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
