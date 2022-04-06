package com.smoc.cloud.book.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.smoc.cloud.book.model.BookExcelModelListener;
import com.smoc.cloud.common.smoc.customer.qo.BookExcelModel;
import com.smoc.cloud.common.smoc.message.model.MessageTemplateExcelModel;
import com.smoc.cloud.message.model.MessageTemplateExcelModelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 读取、导出excel、txt
 */
@Slf4j
public class FileUtils {

    public static List<BookExcelModel> readFile(MultipartFile file, String type) {

        List<BookExcelModel> list = new ArrayList<>();

        try {
            InputStream in = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if (".xls".equals(fileType) || ".xlsx".equals(fileType)) {
                list = readExcelFile(in);
            } else if (".txt".equals(fileType)) {
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //去重
        if (list.size() > 0) {
            list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getMobile().trim()))), ArrayList::new));
        }

        return list;

    }

    public static List<BookExcelModel> readExcelFile(InputStream inputStream) {

        BookExcelModelListener excelModelListener = new BookExcelModelListener();
        ExcelReader excelReader = EasyExcel.read(inputStream, BookExcelModel.class, excelModelListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
        List<BookExcelModel> list = excelModelListener.getExcelModelList();
        return list;
    }

    public static List<String> readMessageTemplateExcel(File file) {
        InputStream inputStream = null;
        List<String> list = new ArrayList<String>();
        try {
            inputStream = new FileInputStream(file);
            MessageTemplateExcelModelListener excelModelListener = new MessageTemplateExcelModelListener();
            ExcelReader excelReader = EasyExcel.read(inputStream, MessageTemplateExcelModel.class, excelModelListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
            excelReader.finish();
            list = excelModelListener.getExcelModelList();
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> readMessageTemplateTxt(File desFile) {
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(desFile));
            String tempString = null;
            int i = 0;
            while ((tempString = br.readLine()) != null) {
                if(i>=3){
                    break;
                }
                if (StringUtils.isEmpty(tempString)) {
                    continue;
                }

                list.add(tempString.trim());
                i++;
            }
        } catch (Exception e) {
            log.error("[短信群发预览][preview]数据::{}", e.getMessage());
            e.printStackTrace();
        } finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
