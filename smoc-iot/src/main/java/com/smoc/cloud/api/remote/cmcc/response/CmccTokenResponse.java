package com.smoc.cloud.api.remote.cmcc.response;


import lombok.Getter;
import lombok.Setter;

/**
 * 移动物联网卡接口对接 token
 */
@Setter
@Getter
public class CmccTokenResponse{

   private String token;

   //token 剩余有效时间
   private String ttl;

}
