package com.smoc.cloud.admin.oauth2.entity;

import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

/**
 * 定义 OAuth2RefreshToken
 * 2019/4/15 14:34
 **/
@Setter
public class MpmOAuth2RefreshToken implements OAuth2RefreshToken {

    private String refreshToken;

    @Override
    public String getValue() {
        return refreshToken;
    }
}
