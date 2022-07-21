package com.smoc.cloud.api.remote.cmcc.response;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CmccResponseData<T> implements Serializable {

    /**
     * 移动物联网卡api对接响应编码  0表示成功，其余为失败
     */
    private String status;

    /**
     * 移动物联网卡api对接提示信息 是对status的描述
     */
    private String message;

    /**
     * 返回结果，根据业务不动，T为不同的业务数据
     */
    private T result;
}
