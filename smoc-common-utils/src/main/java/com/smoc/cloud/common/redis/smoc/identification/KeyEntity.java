package com.smoc.cloud.common.redis.smoc.identification;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KeyEntity {

    private String md5HmacKey;
    private String aesKey;
    private String aesIv;
}
