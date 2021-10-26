package com.smoc.cloud.admin.sso.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * token
 * 参照：UsernamePasswordAuthenticationToken
 */
public class TokenAuthenticationToken extends MpmAuthenticationToken {

    public TokenAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public TokenAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
