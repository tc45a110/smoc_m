package com.smoc.cloud.intelligence.remote.response;

import com.smoc.cloud.intelligence.remote.request.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
public class ResponseDataUtil<T> implements Serializable {

    //请求处理结果  0:成功
    private Integer subCode;

    private String message;

    /**
     * 返回的数据
     */
    private T data;

    private PageInfo pageInfo;
}
