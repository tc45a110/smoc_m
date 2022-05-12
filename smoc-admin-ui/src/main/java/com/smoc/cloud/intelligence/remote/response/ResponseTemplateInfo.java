package com.smoc.cloud.intelligence.remote.response;


import com.smoc.cloud.intelligence.remote.request.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ResponseTemplateInfo {

    //模板ID
    private String tplId;

    //模板名称
    private String tplName;

    //板式
    private String cardId;

    //场景类型
    private String scene;

    //模板状态
    private Integer tplState;

    //审核状态
    private Integer auditState;

    //厂商信息
    private List<Map<String, String>> factoryInfo;

    //模板动态参数个 数
//    private Integer paramCnt;

    //模板动态参数列 表
    private List<ResponseParamArr> paramArr;

    //单位（字节）
    private Integer degreeSize;

    //编辑器宽度
    private Integer editorWidth;

    //短信示例
    private String description;

    //模板协议 最大支持 10 页协议
    private String pages;

    //创建人
    private String creator;

    //修改人
    private String updater;

    //创建时间
    private String createTime;

    //修改时间
    private String updateTime;

    //业务id
    private String bizId;

    //模板掩码
    private String tplMask;

    //预览地址
    private String previewUrl;

    //iframe中的 templateId
    private String templateId;

    private String tplDesc;

    //文档中写的字段是
    private Integer paramcnt;


}
