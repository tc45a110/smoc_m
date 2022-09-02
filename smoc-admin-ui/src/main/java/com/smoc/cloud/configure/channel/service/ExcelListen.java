package com.smoc.cloud.configure.channel.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelListen extends AnalysisEventListener<ExcelPriceData> {

    public List<ExcelPriceData> result = new ArrayList<>();
    // 一行一行读取 excel 内容
    @Override
    public void invoke(ExcelPriceData excelData, AnalysisContext analysisContext) {
        //log.info("[excelData]:{}",new Gson().toJson(excelData));
        result.add(excelData);
    }

    // 读取表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //System.out.println("表头：" + headMap);
    }
    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
