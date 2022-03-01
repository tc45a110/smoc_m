package com.smoc.cloud.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.complaint.model.ComplaintExcelModelListener;
import com.smoc.cloud.filter.utils.ExcelModelListener;
import com.smoc.cloud.filter.utils.KeywordsExcelModelListener;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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

    public static List<ExcelModel> readFile(MultipartFile file, String type) {

        List<ExcelModel> list = new ArrayList<>();

        try {
            InputStream in = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if (".xls".equals(fileType) || ".xlsx".equals(fileType)) {
                list =  readExcel(in, type);
            } else if (".txt".equals(fileType)) {
                list =  readerTxt(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //去重
        if(list.size()>0){
            list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getColumn1().trim()))), ArrayList::new));
        }

        return list;

    }

    /**
     * 读取TXT
     *
     * @param inputStream
     * @return
     */
    private static List<ExcelModel> readerTxt(InputStream inputStream) {

        try {
            List<ExcelModel> list = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] s = lineTxt.split(",");
                ExcelModel book = new ExcelModel();
                if (!StringUtils.isEmpty(s[0].trim()) && Utils.isPhone(s[0].trim())) {
                    book.setColumn1(s[0].trim());
                } else {
                    break;
                }

                if (s.length == 2) {
                    book.setColumn2(s[1].trim());
                }

                list.add(book);
            }
            bufferedReader.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 异步EXCEL
     * 1:白黑名单 2：关键词
     */
    public static List<ExcelModel> readExcel(InputStream inputStream, String type) {
        try {
            List<ExcelModel> list = new ArrayList<>();
            if ("1".equals(type)) {
                ExcelModelListener excelModelListener = new ExcelModelListener();
                ExcelReader excelReader = EasyExcel.read(inputStream, ExcelModel.class, excelModelListener).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                excelReader.read(readSheet);
                excelReader.finish();
                list = excelModelListener.getExcelModelList();

            }

            if ("2".equals(type)) {
                KeywordsExcelModelListener keywordsExcelModelListener = new KeywordsExcelModelListener();
                ExcelReader excelReader = EasyExcel.read(inputStream, ExcelModel.class, keywordsExcelModelListener).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                excelReader.read(readSheet);
                excelReader.finish();
                list = keywordsExcelModelListener.getExcelModelList();
            }

            return list;
        } catch (Exception e) {
            log.error("读取excel表格失败:", e);
        }
        return null;
    }

    private static void readEasyExcel(InputStream inputStream, ExcelModelListener excelModelListener) {
        ExcelReader excelReader = EasyExcel.read(inputStream, ExcelModel.class, excelModelListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
    }

    /**
     * 导出excel、txt
     *
     * @param excelname
     * @param expType
     */
    public static void downFiles(String excelname, String expType, List<ExcelModel> list, HttpServletRequest request, HttpServletResponse response) {
        //txt
        if ("1".equals(expType)) {
            String fileName = encodeFilename(excelname + ".txt", request);
            downTxt(fileName, list, request, response);
        }
        //excel
        if ("2".equals(expType)) {
            ServletOutputStream outputStream = null;
            try {
                outputStream = getOutputStream(excelname, response);
                ExcelWriterBuilder writeBook = EasyExcel.write(outputStream);
                ExcelWriterSheetBuilder sheet = writeBook.sheet(excelname);
                sheet.doWrite(list);
            } catch (Exception e) {
                log.error("导出excel表格失败:", e);
            }
        }
    }

    private static void downTxt(String fileName, List<ExcelModel> list, HttpServletRequest request, HttpServletResponse response) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ExcelModel info = list.get(i);
            data.append(info.getColumn1());
            if (!StringUtils.isEmpty(info.getColumn2())) {
                data.append("," + info.getColumn2());
            }
            data.append("\r\n");
        }

        try {
            OutputStream ouputStream = response.getOutputStream();
            response.setContentType("application/csv;charset=GBK");
            request.setCharacterEncoding("GBK");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            ouputStream.write(data.toString().getBytes("GBK"));
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ServletOutputStream getOutputStream(String fileName,
                                                       HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            //设置响应的类型
            response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
            //设置响应的编码格式
            response.setCharacterEncoding("utf8");
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("导出excel表格失败:", e);
            throw new Exception("导出excel表格失败!", e);
        }
    }

    /**
     * 设置下载文件中文件的名称
     *
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
         * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
         * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
         * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
         */
        String agent = request.getHeader("USER-AGENT");
        try {

            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
                return MimeUtility.encodeText(filename, "UTF-8", "B");

            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }

    public static List<ComplaintExcelModel> readComplaintExcelFile(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("导出excel表格失败:", e);
        }
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType) || ".xlsx".equals(fileType)) {
            ComplaintExcelModelListener excelModelListener = new ComplaintExcelModelListener();
            ExcelReader excelReader = EasyExcel.read(inputStream, ComplaintExcelModel.class, excelModelListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
            excelReader.finish();
            List<ComplaintExcelModel> list = excelModelListener.getExcelModelList();
            list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCarrier().trim()+":"+o.getReportNumber().trim()+":"+o.getReportContent().trim()))), ArrayList::new));
            return list;
        }

        return null;
    }
}
